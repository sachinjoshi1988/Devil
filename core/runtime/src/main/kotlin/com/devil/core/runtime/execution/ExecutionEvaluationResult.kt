package com.devil.core.runtime.execution

import com.devil.core.model.common.TraceId
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.execution.ExecutionRequest

/**
 * Represents the bounded result of constitutional execution evaluation.
 *
 * An approved result preserves one evaluated ExecutionRequest. An unavailable
 * result contains neither request nor error. A failed result contains one
 * matching error.
 *
 * This result does not activate capabilities, perform platform actions, observe
 * execution, verify outcomes, or report final success.
 */
@ConsistentCopyVisibility
data class ExecutionEvaluationResult private constructor(
    val traceId: TraceId,
    val status: ExecutionEvaluationStatus,
    val request: ExecutionRequest?,
    val error: UniversalErrorRecord?,
) {
    companion object {
        fun create(
            traceId: TraceId,
            status: ExecutionEvaluationStatus,
            request: ExecutionRequest? = null,
            error: UniversalErrorRecord? = null,
        ): ExecutionEvaluationResult {
            when (status) {
                ExecutionEvaluationStatus.APPROVED -> {
                    require(request != null && error == null) {
                        "Approved execution evaluation results require a request and must not contain an error."
                    }
                }

                ExecutionEvaluationStatus.UNAVAILABLE -> {
                    require(request == null && error == null) {
                        "Unavailable execution evaluation results must not contain a request or error."
                    }
                }

                ExecutionEvaluationStatus.FAILED -> {
                    require(request == null && error != null) {
                        "Failed execution evaluation results require an error and must not contain a request."
                    }
                }
            }

            require(
                request == null ||
                    request.plan.task.decision.understanding.context.traceId ==
                    traceId,
            ) {
                "Execution evaluation result and request must use the same trace identity."
            }

            require(error == null || error.traceId == traceId) {
                "Execution evaluation result and error must use the same trace identity."
            }

            return ExecutionEvaluationResult(
                traceId = traceId,
                status = status,
                request = request,
                error = error,
            )
        }
    }
}
