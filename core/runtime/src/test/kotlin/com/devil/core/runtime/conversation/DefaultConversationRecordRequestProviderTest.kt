package com.devil.core.runtime.conversation

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.common.TraceId
import com.devil.core.model.context.ContextEnvelope
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel
import com.devil.core.model.conversation.ConversationInput
import com.devil.core.model.conversation.ConversationIntakeRecord
import com.devil.core.model.conversation.ConversationIntakeResult
import com.devil.core.model.conversation.ConversationIntakeState
import com.devil.core.model.error.ErrorCode
import com.devil.core.model.error.UniversalErrorRecord
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DefaultConversationRecordRequestProviderTest {

    @Test
    fun `provide returns available request for accepted produced intake`() {
        val intake =
            createProducedIntake(
                traceValue =
                    "trace-conversation-record-provider-001",
                state =
                    ConversationIntakeState.ACCEPTED,
            )
        val provider: ConversationRecordRequestProvider =
            DefaultConversationRecordRequestProvider()

        val result = provider.provide(intake)

        assertEquals(intake.traceId, result.traceId)
        assertEquals(
            ConversationRecordRequestStatus.AVAILABLE,
            result.status,
        )
        assertEquals(
            requireNotNull(intake.intake),
            requireNotNull(result.request).intake,
        )
        assertEquals(
            ConversationIntakeState.ACCEPTED,
            result.request.intake.record.state,
        )
        assertNull(result.error)
    }

    @Test
    fun `provide returns available request for deferred produced intake`() {
        val intake =
            createProducedIntake(
                traceValue =
                    "trace-conversation-record-provider-002",
                state =
                    ConversationIntakeState.DEFERRED,
            )

        val result =
            DefaultConversationRecordRequestProvider()
                .provide(intake)

        assertEquals(
            ConversationRecordRequestStatus.AVAILABLE,
            result.status,
        )
        assertEquals(
            ConversationIntakeState.DEFERRED,
            requireNotNull(result.request).intake.record.state,
        )
        assertNull(result.error)
    }

    @Test
    fun `provide returns available request for rejected produced intake`() {
        val intake =
            createProducedIntake(
                traceValue =
                    "trace-conversation-record-provider-003",
                state =
                    ConversationIntakeState.REJECTED,
            )

        val result =
            DefaultConversationRecordRequestProvider()
                .provide(intake)

        assertEquals(
            ConversationRecordRequestStatus.AVAILABLE,
            result.status,
        )
        assertEquals(
            ConversationIntakeState.REJECTED,
            requireNotNull(result.request).intake.record.state,
        )
        assertNull(result.error)
    }

    @Test
    fun `provide returns unavailable for deferred intake authority result`() {
        val traceId =
            TraceId.from(
                "trace-conversation-record-provider-004",
            )

        val result =
            DefaultConversationRecordRequestProvider()
                .provide(
                    ConversationIntakeAuthorityResult.create(
                        traceId = traceId,
                        status =
                            ConversationIntakeAuthorityStatus.DEFERRED,
                    ),
                )

        assertEquals(traceId, result.traceId)
        assertEquals(
            ConversationRecordRequestStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `provide preserves failed conversation intake error`() {
        val traceId =
            TraceId.from(
                "trace-conversation-record-provider-005",
            )
        val error = createError(traceId)

        val result =
            DefaultConversationRecordRequestProvider()
                .provide(
                    ConversationIntakeAuthorityResult.create(
                        traceId = traceId,
                        status =
                            ConversationIntakeAuthorityStatus.FAILED,
                        error = error,
                    ),
                )

        assertEquals(traceId, result.traceId)
        assertEquals(
            ConversationRecordRequestStatus.FAILED,
            result.status,
        )
        assertNull(result.request)
        assertEquals(error, result.error)
    }

    private fun createProducedIntake(
        traceValue: String,
        state: ConversationIntakeState,
    ): ConversationIntakeAuthorityResult {
        val traceId = TraceId.from(traceValue)

        return ConversationIntakeAuthorityResult.create(
            traceId = traceId,
            status =
                ConversationIntakeAuthorityStatus.PRODUCED,
            intake =
                ConversationIntakeResult.create(
                    record =
                        ConversationIntakeRecord.create(
                            input =
                                ConversationInput.create(
                                    context =
                                        ContextEnvelope.create(
                                            traceId = traceId,
                                            schemaVersion =
                                                SchemaVersion.from(1),
                                            source =
                                                ContextSource.TEXT,
                                            trustLevel =
                                                ContextTrustLevel.VERIFIED,
                                            securityLevel =
                                                ContextSecurityLevel.RESTRICTED,
                                            observedAt =
                                                DevilTimestamp
                                                    .fromEpochMilliseconds(
                                                        1_754_000_206_000L,
                                                    ),
                                        ),
                                    content =
                                        "Bounded conversation record provider test input.",
                                ),
                            state = state,
                            rationale =
                                "Preserve the established conversation intake state.",
                        ),
                ),
        )
    }

    private fun createError(
        traceId: TraceId,
    ): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode =
                ErrorCode.from(
                    "CONVERSATION_INTAKE_FAILED",
                ),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_206_500L,
                ),
            summary =
                "Conversation intake failed.",
        )
    }
}
