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

class ConversationPersistenceRequestResultTest {

    @Test
    fun `create preserves available result with matching request`() {
        val traceId =
            TraceId.from(
                "trace-conversation-persistence-request-result-001",
            )
        val request = createRequest(traceId)

        val result =
            ConversationPersistenceRequestResult.create(
                traceId = traceId,
                status =
                    ConversationPersistenceRequestStatus.AVAILABLE,
                request = request,
            )

        assertEquals(traceId, result.traceId)
        assertEquals(
            ConversationPersistenceRequestStatus.AVAILABLE,
            result.status,
        )
        assertEquals(request, result.request)
        assertNull(result.error)
    }

    @Test
    fun `create preserves unavailable result without request or error`() {
        val traceId =
            TraceId.from(
                "trace-conversation-persistence-request-result-002",
            )

        val result =
            ConversationPersistenceRequestResult.create(
                traceId = traceId,
                status =
                    ConversationPersistenceRequestStatus.UNAVAILABLE,
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
    fun `create preserves failed result with matching error`() {
        val traceId =
            TraceId.from(
                "trace-conversation-persistence-request-result-003",
            )
        val error = createError(traceId)

        val result =
            ConversationPersistenceRequestResult.create(
                traceId = traceId,
                status =
                    ConversationPersistenceRequestStatus.FAILED,
                error = error,
            )

        assertEquals(
            ConversationPersistenceRequestStatus.FAILED,
            result.status,
        )
        assertNull(result.request)
        assertEquals(error, result.error)
    }

    @Test
    fun `create rejects available result without request`() {
        assertFailsWith<IllegalArgumentException> {
            ConversationPersistenceRequestResult.create(
                traceId =
                    TraceId.from(
                        "trace-conversation-persistence-request-result-004",
                    ),
                status =
                    ConversationPersistenceRequestStatus.AVAILABLE,
            )
        }
    }

    @Test
    fun `create rejects available result with error`() {
        val traceId =
            TraceId.from(
                "trace-conversation-persistence-request-result-005",
            )

        assertFailsWith<IllegalArgumentException> {
            ConversationPersistenceRequestResult.create(
                traceId = traceId,
                status =
                    ConversationPersistenceRequestStatus.AVAILABLE,
                request = createRequest(traceId),
                error = createError(traceId),
            )
        }
    }

    @Test
    fun `create rejects available request from a different trace`() {
        assertFailsWith<IllegalArgumentException> {
            ConversationPersistenceRequestResult.create(
                traceId =
                    TraceId.from(
                        "trace-conversation-persistence-request-result-006",
                    ),
                status =
                    ConversationPersistenceRequestStatus.AVAILABLE,
                request =
                    createRequest(
                        TraceId.from(
                            "trace-conversation-persistence-request-result-other",
                        ),
                    ),
            )
        }
    }

    @Test
    fun `create rejects unavailable result with request`() {
        val traceId =
            TraceId.from(
                "trace-conversation-persistence-request-result-007",
            )

        assertFailsWith<IllegalArgumentException> {
            ConversationPersistenceRequestResult.create(
                traceId = traceId,
                status =
                    ConversationPersistenceRequestStatus.UNAVAILABLE,
                request = createRequest(traceId),
            )
        }
    }

    @Test
    fun `create rejects unavailable result with error`() {
        val traceId =
            TraceId.from(
                "trace-conversation-persistence-request-result-008",
            )

        assertFailsWith<IllegalArgumentException> {
            ConversationPersistenceRequestResult.create(
                traceId = traceId,
                status =
                    ConversationPersistenceRequestStatus.UNAVAILABLE,
                error = createError(traceId),
            )
        }
    }

    @Test
    fun `create rejects failed result without error`() {
        assertFailsWith<IllegalArgumentException> {
            ConversationPersistenceRequestResult.create(
                traceId =
                    TraceId.from(
                        "trace-conversation-persistence-request-result-009",
                    ),
                status =
                    ConversationPersistenceRequestStatus.FAILED,
            )
        }
    }

    @Test
    fun `create rejects failed result with request`() {
        val traceId =
            TraceId.from(
                "trace-conversation-persistence-request-result-010",
            )

        assertFailsWith<IllegalArgumentException> {
            ConversationPersistenceRequestResult.create(
                traceId = traceId,
                status =
                    ConversationPersistenceRequestStatus.FAILED,
                request = createRequest(traceId),
                error = createError(traceId),
            )
        }
    }

    @Test
    fun `create rejects failed error from a different trace`() {
        assertFailsWith<IllegalArgumentException> {
            ConversationPersistenceRequestResult.create(
                traceId =
                    TraceId.from(
                        "trace-conversation-persistence-request-result-011",
                    ),
                status =
                    ConversationPersistenceRequestStatus.FAILED,
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
            record = createRecord(traceId),
        )
    }

    private fun createRecord(
        traceId: TraceId,
    ): ConversationRecord {
        return ConversationRecord.create(
            conversationId =
                ConversationId.from(
                    "conversation-persistence-request-result",
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
                                        "Bounded conversation persistence request result test input.",
                                ),
                            state =
                                ConversationIntakeState.ACCEPTED,
                            rationale =
                                "Preserve the established conversation intake.",
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
                    "CONVERSATION_PERSISTENCE_REQUEST_FAILED",
                ),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_208_500L,
                ),
            summary =
                "Conversation persistence request preparation failed.",
        )
    }
}
