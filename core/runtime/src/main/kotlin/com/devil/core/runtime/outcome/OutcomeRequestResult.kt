package com.devil.core.runtime.outcome

import com.devil.core.model.common.TraceId
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.outcome.OutcomeRequest

/**
 * Represents the structured operational result of outcome-request preparation.
 *
 * An available result contains one OutcomeRequest. An unavailable result
 * contains neither request nor error. A failed result contains one matching
 * error.
 *
 * This result does not determine final task success or failure, update world
 * state, change task or plan state, create memory or learning, communicate an
 * outcome, or produce the final runtime result.
 */
@ConsistentCopyVisibility
data class OutcomeRequestResult private constructor(
    val traceId: TraceId,
    val status: OutcomeRequestStatus,
    val request: OutcomeRequest?,
    val error: UniversalErrorRecord?,
) {
    companion object {
        fun create(
            traceId: TraceId,
            status: OutcomeRequestStatus,
            request: OutcomeRequest? = null,
            error: UniversalErrorRecord? = null,
        ): OutcomeRequestResult {
            when (status) {
                OutcomeRequestStatus.AVAILABLE -> {
                    require(request != null && error == null) {
                        "Available outcome request results require a request and must not contain an error."
                    }
                }

                OutcomeRequestStatus.UNAVAILABLE -> {
                    require(request == null && error == null) {
                        "Unavailable outcome request results must not contain a request or error."
                    }
                }

                OutcomeRequestStatus.FAILED -> {
                    require(request == null && error != null) {
                        "Failed outcome request results require an error and must not contain a request."
                    }
                }
            }

            require(
                request == null ||
                    request.verification
                        .observation
                        .execution
                        .plan
                        .task
                        .decision
                        .understanding
                        .context
                        .traceId == traceId,
            ) {
                "Outcome request result and request must use the same trace identity."
            }

            require(error == null || error.traceId == traceId) {
                "Outcome request result and error must use the same trace identity."
            }

            return OutcomeRequestResult(
                traceId = traceId,
                status = status,
                request = request,
                error = error,
            )
        }
    }
}
