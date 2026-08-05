package com.devil.core.runtime.plan

import com.devil.core.model.common.TraceId
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.plan.PlanCreationRequest

/**
 * Represents the structured operational result of plan-creation request
 * preparation.
 *
 * An available result contains one PlanCreationRequest. An unavailable result
 * contains neither request nor error. A failed result contains the matching
 * error.
 */
@ConsistentCopyVisibility
data class PlanCreationRequestResult private constructor(
    val traceId: TraceId,
    val status: PlanCreationRequestStatus,
    val request: PlanCreationRequest?,
    val error: UniversalErrorRecord?,
) {
    companion object {
        fun create(
            traceId: TraceId,
            status: PlanCreationRequestStatus,
            request: PlanCreationRequest? = null,
            error: UniversalErrorRecord? = null,
        ): PlanCreationRequestResult {
            when (status) {
                PlanCreationRequestStatus.AVAILABLE -> {
                    require(request != null && error == null) {
                        "Available plan creation request results require a request and must not contain an error."
                    }
                }

                PlanCreationRequestStatus.UNAVAILABLE -> {
                    require(request == null && error == null) {
                        "Unavailable plan creation request results must not contain a request or error."
                    }
                }

                PlanCreationRequestStatus.FAILED -> {
                    require(request == null && error != null) {
                        "Failed plan creation request results require an error and must not contain a request."
                    }
                }
            }

            require(
                request == null ||
                    request.task.decision.understanding.context.traceId == traceId,
            ) {
                "Plan creation request result and request must use the same trace identity."
            }

            require(error == null || error.traceId == traceId) {
                "Plan creation request result and error must use the same trace identity."
            }

            return PlanCreationRequestResult(
                traceId = traceId,
                status = status,
                request = request,
                error = error,
            )
        }
    }
}
