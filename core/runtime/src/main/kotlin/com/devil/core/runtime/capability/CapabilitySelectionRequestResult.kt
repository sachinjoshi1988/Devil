package com.devil.core.runtime.capability

import com.devil.core.model.capability.CapabilitySelectionRequest
import com.devil.core.model.common.TraceId
import com.devil.core.model.error.UniversalErrorRecord

/**
 * Represents the structured operational result of capability-selection request
 * preparation.
 *
 * An available result contains one CapabilitySelectionRequest. An unavailable
 * result contains neither request nor error. A failed result contains the
 * matching error.
 */
@ConsistentCopyVisibility
data class CapabilitySelectionRequestResult private constructor(
    val traceId: TraceId,
    val status: CapabilitySelectionRequestStatus,
    val request: CapabilitySelectionRequest?,
    val error: UniversalErrorRecord?,
) {
    companion object {
        fun create(
            traceId: TraceId,
            status: CapabilitySelectionRequestStatus,
            request: CapabilitySelectionRequest? = null,
            error: UniversalErrorRecord? = null,
        ): CapabilitySelectionRequestResult {
            when (status) {
                CapabilitySelectionRequestStatus.AVAILABLE -> {
                    require(request != null && error == null) {
                        "Available capability selection request results require a request and must not contain an error."
                    }
                }

                CapabilitySelectionRequestStatus.UNAVAILABLE -> {
                    require(request == null && error == null) {
                        "Unavailable capability selection request results must not contain a request or error."
                    }
                }

                CapabilitySelectionRequestStatus.FAILED -> {
                    require(request == null && error != null) {
                        "Failed capability selection request results require an error and must not contain a request."
                    }
                }
            }

            require(
                request == null ||
                    request.plan.task.decision.understanding.context.traceId ==
                    traceId,
            ) {
                "Capability selection request result and request must use the same trace identity."
            }

            require(error == null || error.traceId == traceId) {
                "Capability selection request result and error must use the same trace identity."
            }

            return CapabilitySelectionRequestResult(
                traceId = traceId,
                status = status,
                request = request,
                error = error,
            )
        }
    }
}
