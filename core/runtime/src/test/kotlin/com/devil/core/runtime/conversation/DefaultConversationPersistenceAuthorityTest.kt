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

class DefaultConversationPersistenceAuthorityTest {

    @Test
    fun `evaluatePersistence defers when default evaluator has no approved persistence mechanism`() {
        val conversationRecord =
            createProducedRecord(
                TraceId.from(
                    "trace-conversation-persistence-authority-001",
                ),
            )

        val result =
            DefaultConversationPersistenceAuthority()
                .evaluatePersistence(
                    conversationRecord = conversationRecord,
                )

        assertEquals(
            conversationRecord.traceId,
            result.traceId,
        )
        assertEquals(
            ConversationPersistenceStatus.DEFERRED,
            result.status,
        )
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `evaluatePersistence defers when conversation record is unavailable`() {
        val traceId =
            TraceId.from(
                "trace-conversation-persistence-authority-002",
            )

        val result =
            DefaultConversationPersistenceAuthority()
                .evaluatePersistence(
                    conversationRecord =
                        ConversationRecordResult.create(
                            traceId = traceId,
                            status =
                                ConversationRecordStatus.DEFERRED,
                        ),
                )

        assertEquals(traceId, result.traceId)
        assertEquals(
            ConversationPersistenceStatus.DEFERRED,
            result.status,
        )
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `evaluatePersistence preserves failed conversation record error`() {
        val traceId =
            TraceId.from(
                "trace-conversation-persistence-authority-003",
            )
        val error =
            createError(
                traceId = traceId,
                code = "CONVERSATION_RECORD_FAILED",
            )

        val result =
            DefaultConversationPersistenceAuthority()
                .evaluatePersistence(
                    conversationRecord =
                        ConversationRecordResult.create(
                            traceId = traceId,
                            status =
                                ConversationRecordStatus.FAILED,
                            error = error,
                        ),
                )

        assertEquals(
            ConversationPersistenceStatus.FAILED,
            result.status,
        )
        assertNull(result.request)
        assertEquals(error, result.error)
    }

    @Test
    fun `evaluatePersistence preserves genuine persistable evaluation`() {
        val traceId =
            TraceId.from(
                "trace-conversation-persistence-authority-004",
            )

        val authority =
            DefaultConversationPersistenceAuthority(
                evaluator =
                    object : ConversationPersistenceEvaluator {
                        override fun evaluate(
                            traceId: TraceId,
                            request:
                                com.devil.core.model.conversation.ConversationPersistenceRequest,
                        ): ConversationPersistenceEvaluationResult {
                            return ConversationPersistenceEvaluationResult.create(
                                traceId = traceId,
                                status =
                                    ConversationPersistenceEvaluationStatus.PERSISTABLE,
                                request = request,
                            )
                        }
                    },
            )

        val result =
            authority.evaluatePersistence(
                conversationRecord =
                    createProducedRecord(traceId),
            )

        assertEquals(
            ConversationPersistenceStatus.PERSISTABLE,
            result.status,
        )
        assertEquals(traceId, result.traceId)
        check(result.request != null)
        assertNull(result.error)
    }

    @Test
    fun `evaluatePersistence rejects request result from different trace`() {
        val traceId =
            TraceId.from(
                "trace-conversation-persistence-authority-005",
            )

        val authority =
            DefaultConversationPersistenceAuthority(
                requestProvider =
                    object :
                        ConversationPersistenceRequestProvider {
                        override fun provide(
                            conversationRecord:
                                ConversationRecordResult,
                        ): ConversationPersistenceRequestResult {
                            return ConversationPersistenceRequestResult.create(
                                traceId =
                                    TraceId.from(
                                        "trace-conversation-persistence-request-other",
                                    ),
                                status =
                                    ConversationPersistenceRequestStatus.UNAVAILABLE,
                            )
                        }
                    },
            )

        assertFailsWith<IllegalArgumentException> {
            authority.evaluatePersistence(
                conversationRecord =
                    createProducedRecord(traceId),
            )
        }
    }

    @Test
    fun `evaluatePersistence rejects evaluation result from different trace`() {
        val traceId =
            TraceId.from(
                "trace-conversation-persistence-authority-006",
            )

        val authority =
            DefaultConversationPersistenceAuthority(
                evaluator =
                    object : ConversationPersistenceEvaluator {
                        override fun evaluate(
                            traceId: TraceId,
                            request:
                                com.devil.core.model.conversation.ConversationPersistenceRequest,
                        ): ConversationPersistenceEvaluationResult {
                            return ConversationPersistenceEvaluationResult.create(
                                traceId =
                                    TraceId.from(
                                        "trace-conversation-persistence-evaluation-other",
                                    ),
                                status =
                                    ConversationPersistenceEvaluationStatus.UNAVAILABLE,
                            )
                        }
                    },
            )

        assertFailsWith<IllegalArgumentException> {
            authority.evaluatePersistence(
                conversationRecord =
                    createProducedRecord(traceId),
            )
        }
    }

    private fun createProducedRecord(
        traceId: TraceId,
    ): ConversationRecordResult {
        return ConversationRecordResult.create(
            traceId = traceId,
            status = ConversationRecordStatus.PRODUCED,
            record =
                ConversationRecord.create(
                    conversationId =
                        ConversationId.from(
                            "conversation-persistence-authority",
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
                                                        ContextSource.TEST,
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
                                                "Bounded conversation persistence authority test input.",
                                        ),
                                    state =
                                        ConversationIntakeState.ACCEPTED,
                                    rationale =
                                        "Preserve one bounded conversation intake.",
                                ),
                        ),
                ),
        )
    }

    private fun createError(
        traceId: TraceId,
        code: String,
    ): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode = ErrorCode.from(code),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_209_500L,
                ),
            summary =
                "Bounded conversation persistence dependency failed.",
        )
    }
}
