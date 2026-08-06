package com.devil.core.runtime.learning

import com.devil.core.model.common.TraceId
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.learning.LearningRequest

/**
 * Represents the structured operational result of constitutional learning-request
 * preparation.
 *
 * An available result contains one LearningRequest. An unavailable result
 * contains neither request nor error. A failed result contains one matching
 * error.
 *
 * This result does not create learning, create or commit memory, mutate world
 * state, change task or plan state, communicate externally, or produce a
 * runtime result.
 */
@ConsistentCopyVisibility
data class LearningRequestResult private constructor(
    val traceId: TraceId,
    val status: LearningRequestStatus,
    val request: LearningRequest?,
    val error: UniversalErrorRecord?,
) {
    companion object {
        fun create(
            traceId: TraceId,
            status: LearningRequestStatus,
            request: LearningRequest? = null,
            error: UniversalErrorRecord? = null,
        ): LearningRequestResult {
            when (status) {
                LearningRequestStatus.AVAILABLE -> {
                    require(request != null && error == null) {
                        "Available learning request results require a request and must not contain an error."
                    }
                }

                LearningRequestStatus.UNAVAILABLE -> {
                    require(request == null && error == null) {
                        "Unavailable learning request results must not contain a request or error."
                    }
                }

                LearningRequestStatus.FAILED -> {
                    require(request == null && error != null) {
                        "Failed learning request results require an error and must not contain a request."
                    }
                }
            }

            require(
                request == null ||
                    request.worldModelUpdate
                        .outcome
                        .verification
                        .observation
                        .execution
                        .plan
                        .task
                        .decision
                        .understanding
                        .context
                        .traceId == traceId,
            ) {
                "Learning request result and request must use the same trace identity."
            }

            require(error == null || error.traceId == traceId) {
                "Learning request result and error must use the same trace identity."
            }

            return LearningRequestResult(
                traceId = traceId,
                status = status,
                request = request,
                error = error,
            )
        }
    }
}
