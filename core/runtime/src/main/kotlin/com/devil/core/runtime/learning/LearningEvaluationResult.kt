package com.devil.core.runtime.learning

import com.devil.core.model.common.TraceId
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.learning.LearningRequest

/**
 * Represents the bounded result of constitutional learning evaluation.
 *
 * A learnable result preserves the evaluated LearningRequest. An unavailable
 * result contains neither request nor error. A failed result contains one
 * matching error.
 *
 * Preserving a request does not itself create learning, create or commit memory,
 * mutate world state, change task or plan state, communicate externally, or
 * produce a runtime result.
 */
@ConsistentCopyVisibility
data class LearningEvaluationResult private constructor(
    val traceId: TraceId,
    val status: LearningEvaluationStatus,
    val request: LearningRequest?,
    val error: UniversalErrorRecord?,
) {
    companion object {
        fun create(
            traceId: TraceId,
            status: LearningEvaluationStatus,
            request: LearningRequest? = null,
            error: UniversalErrorRecord? = null,
        ): LearningEvaluationResult {
            when (status) {
                LearningEvaluationStatus.LEARNABLE -> {
                    require(request != null && error == null) {
                        "Learnable evaluation results require a request and must not contain an error."
                    }
                }

                LearningEvaluationStatus.UNAVAILABLE -> {
                    require(request == null && error == null) {
                        "Unavailable learning evaluation results must not contain a request or error."
                    }
                }

                LearningEvaluationStatus.FAILED -> {
                    require(request == null && error != null) {
                        "Failed learning evaluation results require an error and must not contain a request."
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
                "Learning evaluation result and request must use the same trace identity."
            }

            require(error == null || error.traceId == traceId) {
                "Learning evaluation result and error must use the same trace identity."
            }

            return LearningEvaluationResult(
                traceId = traceId,
                status = status,
                request = request,
                error = error,
            )
        }
    }
}
