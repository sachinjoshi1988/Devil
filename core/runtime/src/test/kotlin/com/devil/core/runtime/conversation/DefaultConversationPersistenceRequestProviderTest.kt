package com.devil.core.runtime.conversation

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.common.TraceId
import com.devil.core.model.context.ContextEnvelope
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel
import com.devil.core.model.conversation.ConversationId
import com.devil.core.model.conversation.ConversationInput
import com.devil.core.model.conversation.ConversationIntakeRecord
import com.devil.core.model.conversation.ConversationIntakeResult
import com.devil.core.model.conversation.ConversationIntakeState
import com.devil.core.model.conversation.ConversationRecord
import com.devil.core.model.error.ErrorCode
import com.devil.core.model.error.UniversalErrorRecord
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DefaultConversationPersistenceRequestProviderTest {

    @Test
    fun `provide returns available request for produced conversation record`() {
        val recordResult =
            createProducedRecord(
                traceValue =
                    "trace-conversation-persistence-provider-001",
                state = ConversationIntakeState.ACCEPTED,
            )
        val provider: ConversationPersistenceRequestProvider =
            DefaultConversationPersistenceRequestProvider()

        val result =
            provider.provide(
                conversationRecord = recordResult,
            )

        assertEquals(recordResult.traceId, result.traceId)
        assertEquals(
            ConversationPersistenceRequestStatus.AVAILABLE,
            result.status,
        )
        assertEquals(
            requireNotNull(recordResult.record),
            requireNotNull(result.request).record,
        )
        assertNull(result.error)
    }

    @Test
    fun `provide preserves deferred intake inside produced conversation record`() {
        val recordResult =
            createProducedRecord(
                traceValue =
                    "trace-conversation-persistence-provider-002",
                state = ConversationIntakeState.DEFERRED,
            )

        val result =
            DefaultConversationPersistenceRequestProvider()
                .provide(
                    conversationRecord = recordResult,
                )

        assertEquals(
            ConversationPersistenceRequestStatus.AVAILABLE,
            result.status,
        )
        assertEquals(
            ConversationIntakeState.DEFERRED,
            requireNotNull(result.request)
                .record
                .intake
                .record
                .state,
        )
        assertNull(result.error)
    }

    @Test
    fun `provide preserves rejected intake inside produced conversation record`() {
        val recordResult =
            createProducedRecord(
                traceValue =
                    "trace-conversation-persistence-provider-003",
                state = ConversationIntakeState.REJECTED,
            )

        val result =
            DefaultConversationPersistenceRequestProvider()
                .provide(
                    conversationRecord = recordResult,
                )

        assertEquals(
            ConversationPersistenceRequestStatus.AVAILABLE,
            result.status,
        )
        assertEquals(
            ConversationIntakeState.REJECTED,
            requireNotNull(result.request)
                .record
                .intake
                .record
                .state,
        )
        assertNull(result.error)
    }

    @Test
    fun `provide returns unavailable for deferred conversation record result`() {
        val traceId =
            TraceId.from(
                "trace-conversation-persistence-provider-004",
            )

        val result =
            DefaultConversationPersistenceRequestProvider()
                .provide(
                    conversationRecord =
                        ConversationRecordResult.create(
                            traceId = traceId,
                            status =
                                ConversationRecordStatus.DEFERRED,
                        ),
                )

        assertEquals(traceId, result.traceId)
        assertEquals(
            ConversationPersistenceRequestStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `provide preserves failed conversation record error`() {
        val traceId =
            TraceId.from(
                "trace-conversation-persistence-provider-005",
            )
        val error = createError(traceId)

        val result =
            DefaultConversationPersistenceRequestProvider()
                .provide(
                    conversationRecord =
                        ConversationRecordResult.create(
                            traceId = traceId,
                            status =
                                ConversationRecordStatus.FAILED,
                            error = error,
                        ),
                )

        assertEquals(traceId, result.traceId)
        assertEquals(
            ConversationPersistenceRequestStatus.FAILED,
            result.status,
        )
        assertNull(result.request)
        assertEquals(error, result.error)
    }

    private fun createProducedRecord(
        traceValue: String,
        state: ConversationIntakeState,
    ): ConversationRecordResult {
        val traceId = TraceId.from(traceValue)

        return ConversationRecordResult.create(
            traceId = traceId,
            status = ConversationRecordStatus.PRODUCED,
            record =
                ConversationRecord.create(
                    conversationId =
                        ConversationId.from(
                            "conversation-persistence-provider",
                        ),
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
                                                                1_754_000_209_000L,
                                                            ),
                                                ),
                                            content =
                                                "Bounded conversation persistence provider test input.",
                                        ),
                                    state = state,
                                    rationale =
                                        "Preserve the established conversation intake state.",
                                ),
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
                    "CONVERSATION_RECORD_FAILED",
                ),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_209_500L,
                ),
            summary =
                "Conversation record formation failed.",
        )
    }
}
