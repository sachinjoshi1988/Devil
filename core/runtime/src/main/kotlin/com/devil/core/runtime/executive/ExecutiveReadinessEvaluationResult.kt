package com.devil.core.runtime.executive

import com.devil.core.model.common.TraceId
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.executive.ExecutiveReadinessRequest

/**
 * Represents the bounded result of constitutional Executive readiness
 * evaluation.
 *
 * A ready result preserves the evaluated ExecutiveReadinessRequest. An
 * unavailable result contains neither request nor error. A failed result
 * contains one matching error.
 *
 * This result does not authorize execution, perform execution, observe results,
 * verify outcomes, or report final outcomes.
 */
@ConsistentCopyVisibility
data class ExecutiveReadinessEvaluationResult private constructor(
    val traceId: TraceId,
    val status: ExecutiveReadinessEvaluationStatus,
    val request: ExecutiveReadinessRequest?,
    val error: UniversalErrorRecord?,
) {
    companion object {
        fun create(
            traceId: TraceId,
            status: ExecutiveReadinessEvaluationStatus,
            request: ExecutiveReadinessRequest? = null,
            error: UniversalErrorRecord? = null,
        ): ExecutiveReadinessEvaluationResult {
            when (status) {
                ExecutiveReadinessEvaluationStatus.READY -> {
                    require(request != null && error == null) {
                        "Ready Executive readiness evaluation results require a request and must not contain an error."
                    }
                }

                ExecutiveReadinessEvaluationStatus.UNAVAILABLE -> {
                    require(request == null && error == null) {
                        "Unavailable Executive readiness evaluation results must not contain a request or error."
                    }
                }

                ExecutiveReadinessEvaluationStatus.FAILED -> {
                    require(request == null && error != null) {
                        "Failed Executive readiness evaluation results require an error and must not contain a request."
                    }
                }
            }

            require(
                request == null ||
                    request.plan.task.decision.understanding.context.traceId ==
                    traceId,
            ) {
                "Executive readiness evaluation result and request must use the same trace identity."
            }

            require(error == null || error.traceId == traceId) {
                "Executive readiness evaluation result and error must use the same trace identity."
            }

            return ExecutiveReadinessEvaluationResult(
                traceId = traceId,
                status = status,
                request = request,
                error = error,
            )
        }
    }
}
