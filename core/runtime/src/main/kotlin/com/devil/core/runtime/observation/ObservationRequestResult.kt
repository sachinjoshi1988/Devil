package com.devil.core.runtime.observation

import com.devil.core.model.common.TraceId
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.observation.ObservationRequest

/**
 * Represents the structured operational result of observation-request
 * preparation.
 *
 * An available result contains one ObservationRequest. An unavailable result
 * contains neither request nor error. A failed result contains one matching
 * error.
 *
 * This result does not create observations, verify outcomes, or report final
 * success.
 */
@ConsistentCopyVisibility
data class ObservationRequestResult private constructor(
    val traceId: TraceId,
    val status: ObservationRequestStatus,
    val request: ObservationRequest?,
    val error: UniversalErrorRecord?,
) {
    companion object {
        fun create(
            traceId: TraceId,
            status: ObservationRequestStatus,
            request: ObservationRequest? = null,
            error: UniversalErrorRecord? = null,
        ): ObservationRequestResult {
            when (status) {
                ObservationRequestStatus.AVAILABLE -> {
                    require(request != null && error == null) {
                        "Available observation request results require a request and must not contain an error."
                    }
                }

                ObservationRequestStatus.UNAVAILABLE -> {
                    require(request == null && error == null) {
                        "Unavailable observation request results must not contain a request or error."
                    }
                }

                ObservationRequestStatus.FAILED -> {
                    require(request == null && error != null) {
                        "Failed observation request results require an error and must not contain a request."
                    }
                }
            }

            require(
                request == null ||
                    request.execution.plan.task.decision.understanding.context.traceId ==
                    traceId,
            ) {
                "Observation request result and request must use the same trace identity."
            }

            require(error == null || error.traceId == traceId) {
                "Observation request result and error must use the same trace identity."
            }

            return ObservationRequestResult(
                traceId = traceId,
                status = status,
                request = request,
                error = error,
            )
        }
    }
}
