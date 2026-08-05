package com.devil.core.runtime.decision

import com.devil.core.model.common.TraceId
import com.devil.core.model.decision.DecisionEvaluationRequest
import com.devil.core.model.error.UniversalErrorRecord

/**
 * Represents the result of constructing a constitutional decision-evaluation
 * request.
 *
 * An available result contains a request. An unavailable result contains
 * neither request nor error. A failed result contains a matching error.
 *
 * This result does not evaluate or select a decision, create memory, create
 * tasks, plan work, authorize capabilities, execute actions, observe results,
 * or verify outcomes.
 */
@ConsistentCopyVisibility
data class DecisionEvaluationRequestResult private constructor(
    val traceId: TraceId,
    val status: DecisionEvaluationRequestStatus,
    val request: DecisionEvaluationRequest?,
    val error: UniversalErrorRecord?,
) {
    companion object {
        fun create(
            traceId: TraceId,
            status: DecisionEvaluationRequestStatus,
            request: DecisionEvaluationRequest? = null,
            error: UniversalErrorRecord? = null,
        ): DecisionEvaluationRequestResult {
            when (status) {
                DecisionEvaluationRequestStatus.AVAILABLE -> {
                    require(request != null && error == null) {
                        "Available decision-evaluation request results require a request and must not contain an error."
                    }

                    require(
                        request.understanding.context.traceId ==
                            traceId,
                    ) {
                        "Decision-evaluation request and result must use the same trace identity."
                    }
                }

                DecisionEvaluationRequestStatus.UNAVAILABLE -> {
                    require(request == null && error == null) {
                        "Unavailable decision-evaluation request results must not contain a request or error."
                    }
                }

                DecisionEvaluationRequestStatus.FAILED -> {
                    require(request == null && error != null) {
                        "Failed decision-evaluation request results require an error and must not contain a request."
                    }

                    require(error.traceId == traceId) {
                        "Decision-evaluation request result and error must use the same trace identity."
                    }
                }
            }

            return DecisionEvaluationRequestResult(
                traceId = traceId,
                status = status,
                request = request,
                error = error,
            )
        }
    }
}
