package com.devil.core.runtime.outcome

import com.devil.core.model.common.TraceId
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.outcome.OutcomeRequest

/**
 * Represents the bounded result of constitutional outcome evaluation.
 *
 * An established result preserves the evaluated OutcomeRequest. An unavailable
 * result contains neither request nor error. A failed result contains one
 * matching error.
 *
 * Preserving the request does not itself update world state, change task or plan
 * state, create memory or learning, communicate an outcome, or produce the final
 * runtime result.
 */
@ConsistentCopyVisibility
data class OutcomeEvaluationResult private constructor(
    val traceId: TraceId,
    val status: OutcomeEvaluationStatus,
    val request: OutcomeRequest?,
    val error: UniversalErrorRecord?,
) {
    companion object {
        fun create(
            traceId: TraceId,
            status: OutcomeEvaluationStatus,
            request: OutcomeRequest? = null,
            error: UniversalErrorRecord? = null,
        ): OutcomeEvaluationResult {
            when (status) {
                OutcomeEvaluationStatus.ESTABLISHED -> {
                    require(request != null && error == null) {
                        "Established outcome evaluation results require a request and must not contain an error."
                    }
                }

                OutcomeEvaluationStatus.UNAVAILABLE -> {
                    require(request == null && error == null) {
                        "Unavailable outcome evaluation results must not contain a request or error."
                    }
                }

                OutcomeEvaluationStatus.FAILED -> {
                    require(request == null && error != null) {
                        "Failed outcome evaluation results require an error and must not contain a request."
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
                "Outcome evaluation result and request must use the same trace identity."
            }

            require(error == null || error.traceId == traceId) {
                "Outcome evaluation result and error must use the same trace identity."
            }

            return OutcomeEvaluationResult(
                traceId = traceId,
                status = status,
                request = request,
                error = error,
            )
        }
    }
}
