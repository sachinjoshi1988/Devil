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
import com.devil.core.model.understanding.UnderstandingEvaluationRequest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class UnderstandingEvaluationRequestResultTest {

    @Test
    fun `create preserves available result with matching request`() {
        val traceId = TraceId.from(
            "trace-understanding-request-result-001",
        )
        val request = createRequest(traceId)

        val result = UnderstandingEvaluationRequestResult.create(
            traceId = traceId,
            status = UnderstandingEvaluationRequestStatus.AVAILABLE,
            request = request,
        )

        assertEquals(traceId, result.traceId)
        assertEquals(
            UnderstandingEvaluationRequestStatus.AVAILABLE,
            result.status,
        )
        assertEquals(request, result.request)
        assertNull(result.error)
    }

    @Test
    fun `create preserves unavailable result without request or error`() {
        val traceId = TraceId.from(
            "trace-understanding-request-result-002",
        )

        val result = UnderstandingEvaluationRequestResult.create(
            traceId = traceId,
            status = UnderstandingEvaluationRequestStatus.UNAVAILABLE,
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
    fun `create preserves failed result with matching error`() {
        val traceId = TraceId.from(
            "trace-understanding-request-result-003",
        )
        val error = createError(traceId)

        val result = UnderstandingEvaluationRequestResult.create(
            traceId = traceId,
            status = UnderstandingEvaluationRequestStatus.FAILED,
            error = error,
        )

        assertEquals(
            UnderstandingEvaluationRequestStatus.FAILED,
            result.status,
        )
        assertNull(result.request)
        assertEquals(error, result.error)
    }

    @Test
    fun `create rejects available result without request`() {
        assertFailsWith<IllegalArgumentException> {
            UnderstandingEvaluationRequestResult.create(
                traceId = TraceId.from(
                    "trace-understanding-request-result-004",
                ),
                status =
                    UnderstandingEvaluationRequestStatus.AVAILABLE,
            )
        }
    }

    @Test
    fun `create rejects available request from a different trace`() {
        assertFailsWith<IllegalArgumentException> {
            UnderstandingEvaluationRequestResult.create(
                traceId = TraceId.from(
                    "trace-understanding-request-result-005",
                ),
                status =
                    UnderstandingEvaluationRequestStatus.AVAILABLE,
                request = createRequest(
                    TraceId.from(
                        "trace-understanding-request-other",
                    ),
                ),
            )
        }
    }

    @Test
    fun `create rejects unavailable result with request`() {
        val traceId = TraceId.from(
            "trace-understanding-request-result-006",
        )

        assertFailsWith<IllegalArgumentException> {
            UnderstandingEvaluationRequestResult.create(
                traceId = traceId,
                status =
                    UnderstandingEvaluationRequestStatus.UNAVAILABLE,
                request = createRequest(traceId),
            )
        }
    }

    @Test
    fun `create rejects failed result without error`() {
        assertFailsWith<IllegalArgumentException> {
            UnderstandingEvaluationRequestResult.create(
                traceId = TraceId.from(
                    "trace-understanding-request-result-007",
                ),
                status =
                    UnderstandingEvaluationRequestStatus.FAILED,
            )
        }
    }

    @Test
    fun `create rejects failed error from a different trace`() {
        assertFailsWith<IllegalArgumentException> {
            UnderstandingEvaluationRequestResult.create(
                traceId = TraceId.from(
                    "trace-understanding-request-result-008",
                ),
                status =
                    UnderstandingEvaluationRequestStatus.FAILED,
                error = createError(
                    TraceId.from(
                        "trace-understanding-request-error-other",
                    ),
                ),
            )
        }
    }

    private fun createRequest(
        traceId: TraceId,
    ): UnderstandingEvaluationRequest {
        return UnderstandingEvaluationRequest.create(
            conversationIntake =
                ConversationIntakeResult.create(
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
                                            1_754_000_066_000L,
                                        ),
                            ),
                            content =
                                "Please open the camera.",
                        ),
                        state =
                            ConversationIntakeState.ACCEPTED,
                        rationale =
                            "Conversation intake was accepted.",
                    ),
                ),
        )
    }

    private fun createError(
        traceId: TraceId,
    ): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode = ErrorCode.from(
                "UNDERSTANDING_EVALUATION_REQUEST_FAILED",
            ),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_066_500L,
                ),
            summary =
                "Understanding evaluation request construction failed.",
        )
    }
}
