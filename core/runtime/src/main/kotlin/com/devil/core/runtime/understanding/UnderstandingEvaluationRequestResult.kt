package com.devil.core.runtime.understanding

import com.devil.core.model.common.TraceId
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.understanding.UnderstandingEvaluationRequest

/**
 * Represents the result of constructing an understanding-evaluation request.
 *
 * An available result contains a request. An unavailable result contains
 * neither request nor error. A failed result contains a matching error.
 *
 * This result does not interpret language, infer intent, produce understanding,
 * create memory, select decisions, plan work, execute capabilities, or verify
 * outcomes.
 */
@ConsistentCopyVisibility
data class UnderstandingEvaluationRequestResult private constructor(
    val traceId: TraceId,
    val status: UnderstandingEvaluationRequestStatus,
    val request: UnderstandingEvaluationRequest?,
    val error: UniversalErrorRecord?,
) {
    companion object {
        fun create(
            traceId: TraceId,
            status: UnderstandingEvaluationRequestStatus,
            request: UnderstandingEvaluationRequest? = null,
            error: UniversalErrorRecord? = null,
        ): UnderstandingEvaluationRequestResult {
            when (status) {
                UnderstandingEvaluationRequestStatus.AVAILABLE -> {
                    require(request != null && error == null) {
                        "Available understanding-evaluation request results require a request and must not contain an error."
                    }

                    require(
                        request.conversationIntake
                            .record
                            .input
                            .context
                            .traceId == traceId,
                    ) {
                        "Understanding-evaluation request and result must use the same trace identity."
                    }
                }

                UnderstandingEvaluationRequestStatus.UNAVAILABLE -> {
                    require(request == null && error == null) {
                        "Unavailable understanding-evaluation request results must not contain a request or error."
                    }
                }

                UnderstandingEvaluationRequestStatus.FAILED -> {
                    require(request == null && error != null) {
                        "Failed understanding-evaluation request results require an error and must not contain a request."
                    }

                    require(error.traceId == traceId) {
                        "Understanding-evaluation request result and error must use the same trace identity."
                    }
                }
            }

            return UnderstandingEvaluationRequestResult(
                traceId = traceId,
                status = status,
                request = request,
                error = error,
            )
        }
    }
}
