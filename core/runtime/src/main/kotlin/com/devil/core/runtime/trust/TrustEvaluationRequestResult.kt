package com.devil.core.runtime.trust

import com.devil.core.model.common.TraceId
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.trust.TrustEvaluationRequest

/**
 * Represents the result of constructing a trust-evaluation request.
 *
 * An available result contains a request. An unavailable result contains
 * neither a request nor an error. A failed result contains a matching error.
 */
@ConsistentCopyVisibility
data class TrustEvaluationRequestResult private constructor(
    val traceId: TraceId,
    val status: TrustEvaluationRequestStatus,
    val request: TrustEvaluationRequest?,
    val error: UniversalErrorRecord?,
) {
    companion object {
        fun create(
            traceId: TraceId,
            status: TrustEvaluationRequestStatus,
            request: TrustEvaluationRequest? = null,
            error: UniversalErrorRecord? = null,
        ): TrustEvaluationRequestResult {
            when (status) {
                TrustEvaluationRequestStatus.AVAILABLE -> {
                    require(request != null && error == null) {
                        "Available trust-evaluation request results require a request and must not contain an error."
                    }
                    require(request.context.traceId == traceId) {
                        "Trust-evaluation request and result must use the same trace identity."
                    }
                }

                TrustEvaluationRequestStatus.UNAVAILABLE -> {
                    require(request == null && error == null) {
                        "Unavailable trust-evaluation request results must not contain a request or error."
                    }
                }

                TrustEvaluationRequestStatus.FAILED -> {
                    require(request == null && error != null) {
                        "Failed trust-evaluation request results require an error and must not contain a request."
                    }
                    require(error.traceId == traceId) {
                        "Trust-evaluation request result and error must use the same trace identity."
                    }
                }
            }

            return TrustEvaluationRequestResult(
                traceId = traceId,
                status = status,
                request = request,
                error = error,
            )
        }
    }
}
