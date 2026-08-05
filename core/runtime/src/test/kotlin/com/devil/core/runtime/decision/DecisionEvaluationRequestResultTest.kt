package com.devil.core.runtime.decision

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.common.TraceId
import com.devil.core.model.context.ContextEnvelope
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel
import com.devil.core.model.decision.DecisionEvaluationRequest
import com.devil.core.model.error.ErrorCode
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.understanding.UnderstandingRecord
import com.devil.core.model.understanding.UnderstandingState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class DecisionEvaluationRequestResultTest {

    @Test
    fun `create preserves available result with matching request`() {
        val traceId = TraceId.from(
            "trace-decision-request-result-001",
        )
        val request = createRequest(traceId)

        val result = DecisionEvaluationRequestResult.create(
            traceId = traceId,
            status = DecisionEvaluationRequestStatus.AVAILABLE,
            request = request,
        )

        assertEquals(traceId, result.traceId)
        assertEquals(
            DecisionEvaluationRequestStatus.AVAILABLE,
            result.status,
        )
        assertEquals(request, result.request)
        assertNull(result.error)
    }

    @Test
    fun `create preserves unavailable result without request or error`() {
        val traceId = TraceId.from(
            "trace-decision-request-result-002",
        )

        val result = DecisionEvaluationRequestResult.create(
            traceId = traceId,
            status =
                DecisionEvaluationRequestStatus.UNAVAILABLE,
        )

        assertEquals(traceId, result.traceId)
        assertEquals(
            DecisionEvaluationRequestStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `create preserves failed result with matching error`() {
        val traceId = TraceId.from(
            "trace-decision-request-result-003",
        )
        val error = createError(traceId)

        val result = DecisionEvaluationRequestResult.create(
            traceId = traceId,
            status = DecisionEvaluationRequestStatus.FAILED,
            error = error,
        )

        assertEquals(
            DecisionEvaluationRequestStatus.FAILED,
            result.status,
        )
        assertNull(result.request)
        assertEquals(error, result.error)
    }

    @Test
    fun `create rejects available result without request`() {
        assertFailsWith<IllegalArgumentException> {
            DecisionEvaluationRequestResult.create(
                traceId = TraceId.from(
                    "trace-decision-request-result-004",
                ),
                status =
                    DecisionEvaluationRequestStatus.AVAILABLE,
            )
        }
    }

    @Test
    fun `create rejects available request from a different trace`() {
        assertFailsWith<IllegalArgumentException> {
            DecisionEvaluationRequestResult.create(
                traceId = TraceId.from(
                    "trace-decision-request-result-005",
                ),
                status =
                    DecisionEvaluationRequestStatus.AVAILABLE,
                request = createRequest(
                    TraceId.from(
                        "trace-decision-request-other",
                    ),
                ),
            )
        }
    }

    @Test
    fun `create rejects unavailable result with request`() {
        val traceId = TraceId.from(
            "trace-decision-request-result-006",
        )

        assertFailsWith<IllegalArgumentException> {
            DecisionEvaluationRequestResult.create(
                traceId = traceId,
                status =
                    DecisionEvaluationRequestStatus.UNAVAILABLE,
                request = createRequest(traceId),
            )
        }
    }

    @Test
    fun `create rejects failed result without error`() {
        assertFailsWith<IllegalArgumentException> {
            DecisionEvaluationRequestResult.create(
                traceId = TraceId.from(
                    "trace-decision-request-result-007",
                ),
                status =
                    DecisionEvaluationRequestStatus.FAILED,
            )
        }
    }

    @Test
    fun `create rejects failed error from a different trace`() {
        assertFailsWith<IllegalArgumentException> {
            DecisionEvaluationRequestResult.create(
                traceId = TraceId.from(
                    "trace-decision-request-result-008",
                ),
                status =
                    DecisionEvaluationRequestStatus.FAILED,
                error = createError(
                    TraceId.from(
                        "trace-decision-request-error-other",
                    ),
                ),
            )
        }
    }

    private fun createRequest(
        traceId: TraceId,
    ): DecisionEvaluationRequest {
        return DecisionEvaluationRequest.create(
            understanding = UnderstandingRecord.create(
                context = ContextEnvelope.create(
                    traceId = traceId,
                    schemaVersion = SchemaVersion.from(1),
                    source = ContextSource.TEXT,
                    trustLevel =
                        ContextTrustLevel.VERIFIED,
                    securityLevel =
                        ContextSecurityLevel.RESTRICTED,
                    observedAt =
                        DevilTimestamp
                            .fromEpochMilliseconds(
                                1_754_000_072_000L,
                            ),
                ),
                state = UnderstandingState.COMPLETE,
                summary = "Open the camera application.",
            ),
        )
    }

    private fun createError(
        traceId: TraceId,
    ): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode = ErrorCode.from(
                "DECISION_EVALUATION_REQUEST_FAILED",
            ),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_072_500L,
                ),
            summary =
                "Decision evaluation request construction failed.",
        )
    }
}
