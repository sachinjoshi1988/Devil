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
import com.devil.core.model.conversation.ConversationPersistenceRequest
import com.devil.core.model.conversation.ConversationRecord
import com.devil.core.model.error.ErrorCode
import com.devil.core.model.error.UniversalErrorRecord
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class ConversationPersistenceEvaluationResultTest {

    @Test
    fun `create preserves persistable result with matching request`() {
        val traceId =
            TraceId.from(
                "trace-conversation-persistence-evaluation-result-001",
            )
        val request = createRequest(traceId)

        val result =
            ConversationPersistenceEvaluationResult.create(
                traceId = traceId,
                status =
                    ConversationPersistenceEvaluationStatus.PERSISTABLE,
                request = request,
            )

        assertEquals(traceId, result.traceId)
        assertEquals(
            ConversationPersistenceEvaluationStatus.PERSISTABLE,
            result.status,
        )
        assertEquals(request, result.request)
        assertNull(result.error)
    }

    @Test
    fun `create preserves unavailable result without request or error`() {
        val traceId =
            TraceId.from(
                "trace-conversation-persistence-evaluation-result-002",
            )

        val result =
            ConversationPersistenceEvaluationResult.create(
                traceId = traceId,
                status =
                    ConversationPersistenceEvaluationStatus.UNAVAILABLE,
            )

        assertEquals(
            ConversationPersistenceEvaluationStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `create preserves failed result with matching error`() {
        val traceId =
            TraceId.from(
                "trace-conversation-persistence-evaluation-result-003",
            )
        val error = createError(traceId)

        val result =
            ConversationPersistenceEvaluationResult.create(
                traceId = traceId,
                status =
                    ConversationPersistenceEvaluationStatus.FAILED,
                error = error,
            )

        assertEquals(
            ConversationPersistenceEvaluationStatus.FAILED,
            result.status,
        )
        assertNull(result.request)
        assertEquals(error, result.error)
    }

    @Test
    fun `create rejects persistable result without request`() {
        assertFailsWith<IllegalArgumentException> {
            ConversationPersistenceEvaluationResult.create(
                traceId =
                    TraceId.from(
                        "trace-conversation-persistence-evaluation-result-004",
                    ),
                status =
                    ConversationPersistenceEvaluationStatus.PERSISTABLE,
            )
        }
    }

    @Test
    fun `create rejects persistable request from different trace`() {
        assertFailsWith<IllegalArgumentException> {
            ConversationPersistenceEvaluationResult.create(
                traceId =
                    TraceId.from(
                        "trace-conversation-persistence-evaluation-result-005",
                    ),
                status =
                    ConversationPersistenceEvaluationStatus.PERSISTABLE,
                request =
                    createRequest(
                        TraceId.from(
                            "trace-conversation-persistence-evaluation-request-other",
                        ),
                    ),
            )
        }
    }

    @Test
    fun `create rejects failed result without error`() {
        assertFailsWith<IllegalArgumentException> {
            ConversationPersistenceEvaluationResult.create(
                traceId =
                    TraceId.from(
                        "trace-conversation-persistence-evaluation-result-006",
                    ),
                status =
                    ConversationPersistenceEvaluationStatus.FAILED,
            )
        }
    }

    @Test
    fun `create rejects failed error from different trace`() {
        assertFailsWith<IllegalArgumentException> {
            ConversationPersistenceEvaluationResult.create(
                traceId =
                    TraceId.from(
                        "trace-conversation-persistence-evaluation-result-007",
                    ),
                status =
                    ConversationPersistenceEvaluationStatus.FAILED,
                error =
                    createError(
                        TraceId.from(
                            "trace-conversation-persistence-evaluation-error-other",
                        ),
                    ),
            )
        }
    }

    private fun createRequest(
        traceId: TraceId,
    ): ConversationPersistenceRequest {
        return ConversationPersistenceRequest.create(
            record =
                ConversationRecord.create(
                    conversationId =
                        ConversationId.from(
                            "conversation-persistence-evaluation-result",
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
                                                                1_754_000_208_000L,
                                                            ),
                                                ),
                                            content =
                                                "Bounded conversation persistence evaluation test input.",
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
    ): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode =
                ErrorCode.from(
                    "CONVERSATION_PERSISTENCE_EVALUATION_FAILED",
                ),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_208_500L,
                ),
            summary =
                "Conversation persistence evaluation failed.",
        )
    }
}
