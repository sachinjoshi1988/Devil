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

class DefaultConversationPersistenceResultMapperTest {

    @Test
    fun `map preserves persistable evaluation`() {
        val traceId =
            TraceId.from(
                "trace-conversation-persistence-mapper-001",
            )
        val request = createRequest(traceId)

        val result =
            DefaultConversationPersistenceResultMapper().map(
                traceId = traceId,
                evaluation =
                    ConversationPersistenceEvaluationResult.create(
                        traceId = traceId,
                        status =
                            ConversationPersistenceEvaluationStatus.PERSISTABLE,
                        request = request,
                    ),
            )

        assertEquals(
            ConversationPersistenceStatus.PERSISTABLE,
            result.status,
        )
        assertEquals(request, result.request)
        assertNull(result.error)
    }

    @Test
    fun `map converts unavailable evaluation to deferred`() {
        val traceId =
            TraceId.from(
                "trace-conversation-persistence-mapper-002",
            )

        val result =
            DefaultConversationPersistenceResultMapper().map(
                traceId = traceId,
                evaluation =
                    ConversationPersistenceEvaluationResult.create(
                        traceId = traceId,
                        status =
                            ConversationPersistenceEvaluationStatus.UNAVAILABLE,
                    ),
            )

        assertEquals(
            ConversationPersistenceStatus.DEFERRED,
            result.status,
        )
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `map preserves failed evaluation error`() {
        val traceId =
            TraceId.from(
                "trace-conversation-persistence-mapper-003",
            )
        val error = createError(traceId)

        val result =
            DefaultConversationPersistenceResultMapper().map(
                traceId = traceId,
                evaluation =
                    ConversationPersistenceEvaluationResult.create(
                        traceId = traceId,
                        status =
                            ConversationPersistenceEvaluationStatus.FAILED,
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
    fun `map rejects evaluation from different trace`() {
        assertFailsWith<IllegalArgumentException> {
            DefaultConversationPersistenceResultMapper().map(
                traceId =
                    TraceId.from(
                        "trace-conversation-persistence-mapper-004",
                    ),
                evaluation =
                    ConversationPersistenceEvaluationResult.create(
                        traceId =
                            TraceId.from(
                                "trace-conversation-persistence-mapper-other",
                            ),
                        status =
                            ConversationPersistenceEvaluationStatus.UNAVAILABLE,
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
                            "conversation-persistence-mapper",
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
                                                                1_754_000_208_000L,
                                                            ),
                                                ),
                                            content =
                                                "Bounded conversation persistence mapper test input.",
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
