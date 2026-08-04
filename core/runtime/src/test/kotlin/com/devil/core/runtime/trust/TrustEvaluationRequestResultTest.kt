package com.devil.core.runtime.trust

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.common.TraceId
import com.devil.core.model.context.ContextEnvelope
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel
import com.devil.core.model.error.ErrorCode
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.identity.IdentityId
import com.devil.core.model.trust.TrustEvaluationRequest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class TrustEvaluationRequestResultTest {

    @Test
    fun `create preserves available result with matching request`() {
        val context = createContext(
            "trace-trust-request-result-001",
        )
        val request = createRequest(context)

        val result = TrustEvaluationRequestResult.create(
            traceId = context.traceId,
            status = TrustEvaluationRequestStatus.AVAILABLE,
            request = request,
        )

        assertEquals(context.traceId, result.traceId)
        assertEquals(
            TrustEvaluationRequestStatus.AVAILABLE,
            result.status,
        )
        assertEquals(request, result.request)
        assertNull(result.error)
    }

    @Test
    fun `create preserves unavailable result without request or error`() {
        val traceId = TraceId.from(
            "trace-trust-request-result-002",
        )

        val result = TrustEvaluationRequestResult.create(
            traceId = traceId,
            status = TrustEvaluationRequestStatus.UNAVAILABLE,
        )

        assertEquals(traceId, result.traceId)
        assertEquals(
            TrustEvaluationRequestStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `create preserves failed result with matching error`() {
        val traceId = TraceId.from(
            "trace-trust-request-result-003",
        )
        val error = createError(traceId)

        val result = TrustEvaluationRequestResult.create(
            traceId = traceId,
            status = TrustEvaluationRequestStatus.FAILED,
            error = error,
        )

        assertEquals(traceId, result.traceId)
        assertEquals(
            TrustEvaluationRequestStatus.FAILED,
            result.status,
        )
        assertNull(result.request)
        assertEquals(error, result.error)
    }

    @Test
    fun `create rejects available result without request`() {
        assertFailsWith<IllegalArgumentException> {
            TrustEvaluationRequestResult.create(
                traceId = TraceId.from(
                    "trace-trust-request-result-004",
                ),
                status = TrustEvaluationRequestStatus.AVAILABLE,
            )
        }
    }

    @Test
    fun `create rejects available request from a different trace`() {
        assertFailsWith<IllegalArgumentException> {
            TrustEvaluationRequestResult.create(
                traceId = TraceId.from(
                    "trace-trust-request-result-005",
                ),
                status = TrustEvaluationRequestStatus.AVAILABLE,
                request = createRequest(
                    createContext(
                        "trace-trust-request-result-other",
                    ),
                ),
            )
        }
    }

    @Test
    fun `create rejects unavailable result with request`() {
        val context = createContext(
            "trace-trust-request-result-006",
        )

        assertFailsWith<IllegalArgumentException> {
            TrustEvaluationRequestResult.create(
                traceId = context.traceId,
                status = TrustEvaluationRequestStatus.UNAVAILABLE,
                request = createRequest(context),
            )
        }
    }

    @Test
    fun `create rejects failed result without error`() {
        assertFailsWith<IllegalArgumentException> {
            TrustEvaluationRequestResult.create(
                traceId = TraceId.from(
                    "trace-trust-request-result-007",
                ),
                status = TrustEvaluationRequestStatus.FAILED,
            )
        }
    }

    @Test
    fun `create rejects failed error from a different trace`() {
        assertFailsWith<IllegalArgumentException> {
            TrustEvaluationRequestResult.create(
                traceId = TraceId.from(
                    "trace-trust-request-result-008",
                ),
                status = TrustEvaluationRequestStatus.FAILED,
                error = createError(
                    TraceId.from(
                        "trace-trust-request-error-other",
                    ),
                ),
            )
        }
    }

    private fun createRequest(
        context: ContextEnvelope,
    ): TrustEvaluationRequest {
        return TrustEvaluationRequest.create(
            context = context,
            subjectIdentityId = IdentityId.from(
                "subject-trust-request-result",
            ),
        )
    }

    private fun createContext(
        traceValue: String,
    ): ContextEnvelope {
        return ContextEnvelope.create(
            traceId = TraceId.from(traceValue),
            schemaVersion = SchemaVersion.from(1),
            source = ContextSource.TEST,
            trustLevel = ContextTrustLevel.VERIFIED,
            securityLevel = ContextSecurityLevel.RESTRICTED,
            observedAt = DevilTimestamp.fromEpochMilliseconds(
                1_754_000_049_000L,
            ),
        )
    }

    private fun createError(
        traceId: TraceId,
    ): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode = ErrorCode.from(
                "TRUST_EVALUATION_REQUEST_FAILED",
            ),
            traceId = traceId,
            occurredAt = DevilTimestamp.fromEpochMilliseconds(
                1_754_000_049_500L,
            ),
            summary = "Trust evaluation request construction failed.",
        )
    }
}
