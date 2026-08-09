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
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class ConversationRecordResultTest {

    @Test
    fun `create preserves produced result with matching conversation record`() {
        val traceId =
            TraceId.from(
                "trace-conversation-record-result-001",
            )
        val record = createRecord(traceId)

        val result =
            ConversationRecordResult.create(
                traceId = traceId,
                status = ConversationRecordStatus.PRODUCED,
                record = record,
            )

        assertEquals(traceId, result.traceId)
        assertEquals(
            ConversationRecordStatus.PRODUCED,
            result.status,
        )
        assertEquals(record, result.record)
        assertNull(result.error)
    }

    @Test
    fun `create preserves deferred result without record or error`() {
        val traceId =
            TraceId.from(
                "trace-conversation-record-result-002",
            )

        val result =
            ConversationRecordResult.create(
                traceId = traceId,
                status = ConversationRecordStatus.DEFERRED,
            )

        assertEquals(traceId, result.traceId)
        assertEquals(
            ConversationRecordStatus.DEFERRED,
            result.status,
        )
        assertNull(result.record)
        assertNull(result.error)
    }

    @Test
    fun `create preserves failed result with matching error`() {
        val traceId =
            TraceId.from(
                "trace-conversation-record-result-003",
            )
        val error = createError(traceId)

        val result =
            ConversationRecordResult.create(
                traceId = traceId,
                status = ConversationRecordStatus.FAILED,
                error = error,
            )

        assertEquals(
            ConversationRecordStatus.FAILED,
            result.status,
        )
        assertNull(result.record)
        assertEquals(error, result.error)
    }

    @Test
    fun `create rejects produced result without record`() {
        assertFailsWith<IllegalArgumentException> {
            ConversationRecordResult.create(
                traceId =
                    TraceId.from(
                        "trace-conversation-record-result-004",
                    ),
                status = ConversationRecordStatus.PRODUCED,
            )
        }
    }

    @Test
    fun `create rejects produced result with error`() {
        val traceId =
            TraceId.from(
                "trace-conversation-record-result-005",
            )

        assertFailsWith<IllegalArgumentException> {
            ConversationRecordResult.create(
                traceId = traceId,
                status = ConversationRecordStatus.PRODUCED,
                record = createRecord(traceId),
                error = createError(traceId),
            )
        }
    }

    @Test
    fun `create rejects produced record from a different trace`() {
        assertFailsWith<IllegalArgumentException> {
            ConversationRecordResult.create(
                traceId =
                    TraceId.from(
                        "trace-conversation-record-result-006",
                    ),
                status = ConversationRecordStatus.PRODUCED,
                record =
                    createRecord(
                        TraceId.from(
                            "trace-conversation-record-result-other",
                        ),
                    ),
            )
        }
    }

    @Test
    fun `create rejects deferred result with record`() {
        val traceId =
            TraceId.from(
                "trace-conversation-record-result-007",
            )

        assertFailsWith<IllegalArgumentException> {
            ConversationRecordResult.create(
                traceId = traceId,
                status = ConversationRecordStatus.DEFERRED,
                record = createRecord(traceId),
            )
        }
    }

    @Test
    fun `create rejects deferred result with error`() {
        val traceId =
            TraceId.from(
                "trace-conversation-record-result-008",
            )

        assertFailsWith<IllegalArgumentException> {
            ConversationRecordResult.create(
                traceId = traceId,
                status = ConversationRecordStatus.DEFERRED,
                error = createError(traceId),
            )
        }
    }

    @Test
    fun `create rejects failed result without error`() {
        assertFailsWith<IllegalArgumentException> {
            ConversationRecordResult.create(
                traceId =
                    TraceId.from(
                        "trace-conversation-record-result-009",
                    ),
                status = ConversationRecordStatus.FAILED,
            )
        }
    }

    @Test
    fun `create rejects failed result with record`() {
        val traceId =
            TraceId.from(
                "trace-conversation-record-result-010",
            )

        assertFailsWith<IllegalArgumentException> {
            ConversationRecordResult.create(
                traceId = traceId,
                status = ConversationRecordStatus.FAILED,
                record = createRecord(traceId),
                error = createError(traceId),
            )
        }
    }

    @Test
    fun `create rejects failed error from a different trace`() {
        assertFailsWith<IllegalArgumentException> {
            ConversationRecordResult.create(
                traceId =
                    TraceId.from(
                        "trace-conversation-record-result-011",
                    ),
                status = ConversationRecordStatus.FAILED,
                error =
                    createError(
                        TraceId.from(
                            "trace-conversation-record-error-other",
                        ),
                    ),
            )
        }
    }

    @Test
    fun `produced record does not imply conversation persistence`() {
        val traceId =
            TraceId.from(
                "trace-conversation-record-result-012",
            )

        val result =
            ConversationRecordResult.create(
                traceId = traceId,
                status = ConversationRecordStatus.PRODUCED,
                record = createRecord(traceId),
            )

        assertEquals(
            ConversationRecordStatus.PRODUCED,
            result.status,
        )
        assertEquals(
            ConversationIntakeState.ACCEPTED,
            result.record?.intake?.record?.state,
        )
    }

    private fun createRecord(
        traceId: TraceId,
    ): ConversationRecord {
        return ConversationRecord.create(
            conversationId =
                ConversationId.from(
                    "conversation-record-result",
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
                                            source = ContextSource.TEXT,
                                            trustLevel =
                                                ContextTrustLevel.VERIFIED,
                                            securityLevel =
                                                ContextSecurityLevel.RESTRICTED,
                                            observedAt =
                                                DevilTimestamp
                                                    .fromEpochMilliseconds(
                                                        1_754_000_203_000L,
                                                    ),
                                        ),
                                    content =
                                        "Bounded conversation record result test input.",
                                ),
                            state =
                                ConversationIntakeState.ACCEPTED,
                            rationale =
                                "Preserve one bounded conversation intake result.",
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
                    1_754_000_203_500L,
                ),
            summary =
                "Conversation record formation failed.",
        )
    }
}
