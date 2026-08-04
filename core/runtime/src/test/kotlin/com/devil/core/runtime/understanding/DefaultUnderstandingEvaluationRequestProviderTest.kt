package com.devil.core.runtime.understanding

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
import com.devil.core.runtime.conversation.ConversationIntakeAuthorityResult
import com.devil.core.runtime.conversation.ConversationIntakeAuthorityStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DefaultUnderstandingEvaluationRequestProviderTest {

    @Test
    fun `provide returns available request for accepted intake`() {
        val intake = createProducedIntake(
            traceValue =
                "trace-understanding-provider-001",
            state = ConversationIntakeState.ACCEPTED,
        )
        val provider: UnderstandingEvaluationRequestProvider =
            DefaultUnderstandingEvaluationRequestProvider()

        val result = provider.provide(intake)

        assertEquals(intake.traceId, result.traceId)
        assertEquals(
            UnderstandingEvaluationRequestStatus.AVAILABLE,
            result.status,
        )
        assertEquals(
            requireNotNull(intake.intake),
            requireNotNull(result.request).conversationIntake,
        )
        assertNull(result.error)
    }

    @Test
    fun `provide returns unavailable for deferred intake state`() {
        val result =
            DefaultUnderstandingEvaluationRequestProvider()
                .provide(
                    createProducedIntake(
                        traceValue =
                            "trace-understanding-provider-002",
                        state =
                            ConversationIntakeState.DEFERRED,
                    ),
                )

        assertEquals(
            UnderstandingEvaluationRequestStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `provide returns unavailable for rejected intake state`() {
        val result =
            DefaultUnderstandingEvaluationRequestProvider()
                .provide(
                    createProducedIntake(
                        traceValue =
                            "trace-understanding-provider-003",
                        state =
                            ConversationIntakeState.REJECTED,
                    ),
                )

        assertEquals(
            UnderstandingEvaluationRequestStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `provide returns unavailable for deferred authority result`() {
        val traceId = TraceId.from(
            "trace-understanding-provider-004",
        )

        val result =
            DefaultUnderstandingEvaluationRequestProvider()
                .provide(
                    ConversationIntakeAuthorityResult.create(
                        traceId = traceId,
                        status =
                            ConversationIntakeAuthorityStatus.DEFERRED,
                    ),
                )

        assertEquals(traceId, result.traceId)
        assertEquals(
            UnderstandingEvaluationRequestStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `provide preserves failed conversation intake error`() {
        val traceId = TraceId.from(
            "trace-understanding-provider-005",
        )
        val error = createError(traceId)

        val result =
            DefaultUnderstandingEvaluationRequestProvider()
                .provide(
                    ConversationIntakeAuthorityResult.create(
                        traceId = traceId,
                        status =
                            ConversationIntakeAuthorityStatus.FAILED,
                        error = error,
                    ),
                )

        assertEquals(
            UnderstandingEvaluationRequestStatus.FAILED,
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
            intake = ConversationIntakeResult.create(
                record = ConversationIntakeRecord.create(
                    input = ConversationInput.create(
                        context = ContextEnvelope.create(
                            traceId = traceId,
                            schemaVersion =
                                SchemaVersion.from(1),
                            source = ContextSource.TEXT,
                            trustLevel =
                                ContextTrustLevel.VERIFIED,
                            securityLevel =
                                ContextSecurityLevel.RESTRICTED,
                            observedAt =
                                DevilTimestamp
                                    .fromEpochMilliseconds(
                                        1_754_000_067_000L,
                                    ),
                        ),
                        content =
                            "Please open the camera.",
                    ),
                    state = state,
                    rationale =
                        "Bounded conversation-intake state was established.",
                ),
            ),
        )
    }

    private fun createError(
        traceId: TraceId,
    ): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode = ErrorCode.from(
                "CONVERSATION_INTAKE_FAILED",
            ),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_067_500L,
                ),
            summary = "Conversation intake failed.",
        )
    }
}
