package com.devil.core.runtime.executive

import com.devil.core.model.common.TraceId
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.executive.ExecutiveReadinessRequest

/**
 * Represents the structured operational result of Executive-readiness request
 * preparation.
 *
 * An available result contains one ExecutiveReadinessRequest. An unavailable
 * result contains neither request nor error. A failed result contains the
 * matching error.
 */
@ConsistentCopyVisibility
data class ExecutiveReadinessRequestResult private constructor(
    val traceId: TraceId,
    val status: ExecutiveReadinessRequestStatus,
    val request: ExecutiveReadinessRequest?,
    val error: UniversalErrorRecord?,
) {
    companion object {
        fun create(
            traceId: TraceId,
            status: ExecutiveReadinessRequestStatus,
            request: ExecutiveReadinessRequest? = null,
            error: UniversalErrorRecord? = null,
        ): ExecutiveReadinessRequestResult {
            when (status) {
                ExecutiveReadinessRequestStatus.AVAILABLE -> {
                    require(request != null && error == null) {
                        "Available Executive readiness request results require a request and must not contain an error."
                    }
                }

                ExecutiveReadinessRequestStatus.UNAVAILABLE -> {
                    require(request == null && error == null) {
                        "Unavailable Executive readiness request results must not contain a request or error."
                    }
                }

                ExecutiveReadinessRequestStatus.FAILED -> {
                    require(request == null && error != null) {
                        "Failed Executive readiness request results require an error and must not contain a request."
                    }
                }
            }

            require(
                request == null ||
                    request.plan.task.decision.understanding.context.traceId ==
                    traceId,
            ) {
                "Executive readiness request result and request must use the same trace identity."
            }

            require(error == null || error.traceId == traceId) {
                "Executive readiness request result and error must use the same trace identity."
            }

            return ExecutiveReadinessRequestResult(
                traceId = traceId,
                status = status,
                request = request,
                error = error,
            )
        }
    }
}
