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

class ConversationPersistenceResultTest {

    @Test
    fun `create preserves persistable result with matching request`() {
        val traceId =
            TraceId.from(
                "trace-conversation-persistence-result-001",
            )
        val request = createRequest(traceId)

        val result =
            ConversationPersistenceResult.create(
                traceId = traceId,
                status =
                    ConversationPersistenceStatus.PERSISTABLE,
                request = request,
            )

        assertEquals(traceId, result.traceId)
        assertEquals(
            ConversationPersistenceStatus.PERSISTABLE,
            result.status,
        )
        assertEquals(request, result.request)
        assertNull(result.error)
    }

    @Test
    fun `create preserves deferred result without request or error`() {
        val traceId =
            TraceId.from(
                "trace-conversation-persistence-result-002",
            )

        val result =
            ConversationPersistenceResult.create(
                traceId = traceId,
                status =
                    ConversationPersistenceStatus.DEFERRED,
            )

        assertEquals(
            ConversationPersistenceStatus.DEFERRED,
            result.status,
        )
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `create preserves failed result with matching error`() {
        val traceId =
            TraceId.from(
                "trace-conversation-persistence-result-003",
            )
        val error = createError(traceId)

        val result =
            ConversationPersistenceResult.create(
                traceId = traceId,
                status =
                    ConversationPersistenceStatus.FAILED,
                error = error,
            )

        assertEquals(
            ConversationPersistenceStatus.FAILED,
            result.status,
        )
        assertNull(result.request)
        assertEquals(error, result.error)
    }

    @Test
    fun `create rejects persistable result without request`() {
        assertFailsWith<IllegalArgumentException> {
            ConversationPersistenceResult.create(
                traceId =
                    TraceId.from(
                        "trace-conversation-persistence-result-004",
                    ),
                status =
                    ConversationPersistenceStatus.PERSISTABLE,
            )
        }
    }

    @Test
    fun `create rejects persistable request from different trace`() {
        assertFailsWith<IllegalArgumentException> {
            ConversationPersistenceResult.create(
                traceId =
                    TraceId.from(
                        "trace-conversation-persistence-result-005",
                    ),
                status =
                    ConversationPersistenceStatus.PERSISTABLE,
                request =
                    createRequest(
                        TraceId.from(
                            "trace-conversation-persistence-result-other",
                        ),
                    ),
            )
        }
    }

    @Test
    fun `create rejects failed result without error`() {
        assertFailsWith<IllegalArgumentException> {
            ConversationPersistenceResult.create(
                traceId =
                    TraceId.from(
                        "trace-conversation-persistence-result-006",
                    ),
                status =
                    ConversationPersistenceStatus.FAILED,
            )
        }
    }

    @Test
    fun `create rejects failed error from different trace`() {
        assertFailsWith<IllegalArgumentException> {
            ConversationPersistenceResult.create(
                traceId =
                    TraceId.from(
                        "trace-conversation-persistence-result-007",
                    ),
                status =
                    ConversationPersistenceStatus.FAILED,
                error =
                    createError(
                        TraceId.from(
                            "trace-conversation-persistence-error-other",
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
                            "conversation-persistence-result",
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
                                                                1_754_000_210_000L,
                                                            ),
                                                ),
                                            content =
                                                "Bounded conversation persistence result test input.",
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
                    "CONVERSATION_PERSISTENCE_FAILED",
                ),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_210_500L,
                ),
            summary =
                "Conversation persistence evaluation failed.",
        )
    }
}
