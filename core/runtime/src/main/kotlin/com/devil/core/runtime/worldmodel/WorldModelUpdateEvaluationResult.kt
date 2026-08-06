package com.devil.core.runtime.worldmodel

import com.devil.core.model.common.TraceId
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.worldmodel.WorldModelUpdateRequest

/**
 * Represents the bounded result of constitutional World Model update
 * evaluation.
 *
 * An applicable result preserves the evaluated WorldModelUpdateRequest. An
 * unavailable result contains neither request nor error. A failed result
 * contains one matching error.
 *
 * Preserving the request does not itself mutate world state, claim that world
 * state changed, change task or plan state, create memory or learning,
 * communicate externally, or produce a runtime result.
 */
@ConsistentCopyVisibility
data class WorldModelUpdateEvaluationResult private constructor(
    val traceId: TraceId,
    val status: WorldModelUpdateEvaluationStatus,
    val request: WorldModelUpdateRequest?,
    val error: UniversalErrorRecord?,
) {
    companion object {
        fun create(
            traceId: TraceId,
            status: WorldModelUpdateEvaluationStatus,
            request: WorldModelUpdateRequest? = null,
            error: UniversalErrorRecord? = null,
        ): WorldModelUpdateEvaluationResult {
            when (status) {
                WorldModelUpdateEvaluationStatus.APPLICABLE -> {
                    require(request != null && error == null) {
                        "Applicable World Model update evaluation results require a request and must not contain an error."
                    }
                }

                WorldModelUpdateEvaluationStatus.UNAVAILABLE -> {
                    require(request == null && error == null) {
                        "Unavailable World Model update evaluation results must not contain a request or error."
                    }
                }

                WorldModelUpdateEvaluationStatus.FAILED -> {
                    require(request == null && error != null) {
                        "Failed World Model update evaluation results require an error and must not contain a request."
                    }
                }
            }

            require(
                request == null ||
                    request.outcome
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
                "World Model update evaluation result and request must use the same trace identity."
            }

            require(error == null || error.traceId == traceId) {
                "World Model update evaluation result and error must use the same trace identity."
            }

            return WorldModelUpdateEvaluationResult(
                traceId = traceId,
                status = status,
                request = request,
                error = error,
            )
        }
    }
}
