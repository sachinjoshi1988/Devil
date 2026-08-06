package com.devil.core.runtime.observation

import com.devil.core.model.common.TraceId
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.observation.ObservationRequest

/**
 * Represents the bounded result of constitutional observation evaluation.
 *
 * An observed result preserves the evaluated ObservationRequest. An unavailable
 * result contains neither request nor error. A failed result contains one
 * matching error.
 *
 * Preserving the request does not itself prove that execution occurred or that
 * an outcome was achieved.
 */
@ConsistentCopyVisibility
data class ObservationEvaluationResult private constructor(
    val traceId: TraceId,
    val status: ObservationEvaluationStatus,
    val request: ObservationRequest?,
    val error: UniversalErrorRecord?,
) {
    companion object {
        fun create(
            traceId: TraceId,
            status: ObservationEvaluationStatus,
            request: ObservationRequest? = null,
            error: UniversalErrorRecord? = null,
        ): ObservationEvaluationResult {
            when (status) {
                ObservationEvaluationStatus.OBSERVED -> {
                    require(request != null && error == null) {
                        "Observed evaluation results require a request and must not contain an error."
                    }
                }

                ObservationEvaluationStatus.UNAVAILABLE -> {
                    require(request == null && error == null) {
                        "Unavailable observation evaluation results must not contain a request or error."
                    }
                }

                ObservationEvaluationStatus.FAILED -> {
                    require(request == null && error != null) {
                        "Failed observation evaluation results require an error and must not contain a request."
                    }
                }
            }

            require(
                request == null ||
                    request.execution.plan.task.decision.understanding.context.traceId ==
                    traceId,
            ) {
                "Observation evaluation result and request must use the same trace identity."
            }

            require(error == null || error.traceId == traceId) {
                "Observation evaluation result and error must use the same trace identity."
            }

            return ObservationEvaluationResult(
                traceId = traceId,
                status = status,
                request = request,
                error = error,
            )
        }
    }
}
