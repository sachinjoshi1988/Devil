package com.devil.core.runtime.verification

import com.devil.core.model.common.TraceId
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.verification.VerificationRequest

/**
 * Represents the bounded result of constitutional verification evaluation.
 *
 * A verified result preserves the evaluated VerificationRequest. An unavailable
 * result contains neither request nor error. A failed result contains one
 * matching error.
 *
 * Preserving the request does not itself update world state, report final
 * success, change task or plan state, or produce a final outcome.
 */
@ConsistentCopyVisibility
data class VerificationEvaluationResult private constructor(
    val traceId: TraceId,
    val status: VerificationEvaluationStatus,
    val request: VerificationRequest?,
    val error: UniversalErrorRecord?,
) {
    companion object {
        fun create(
            traceId: TraceId,
            status: VerificationEvaluationStatus,
            request: VerificationRequest? = null,
            error: UniversalErrorRecord? = null,
        ): VerificationEvaluationResult {
            when (status) {
                VerificationEvaluationStatus.VERIFIED -> {
                    require(request != null && error == null) {
                        "Verified evaluation results require a request and must not contain an error."
                    }
                }

                VerificationEvaluationStatus.UNAVAILABLE -> {
                    require(request == null && error == null) {
                        "Unavailable verification evaluation results must not contain a request or error."
                    }
                }

                VerificationEvaluationStatus.FAILED -> {
                    require(request == null && error != null) {
                        "Failed verification evaluation results require an error and must not contain a request."
                    }
                }
            }

            require(
                request == null ||
                    request.observation
                        .execution
                        .plan
                        .task
                        .decision
                        .understanding
                        .context
                        .traceId == traceId,
            ) {
                "Verification evaluation result and request must use the same trace identity."
            }

            require(error == null || error.traceId == traceId) {
                "Verification evaluation result and error must use the same trace identity."
            }

            return VerificationEvaluationResult(
                traceId = traceId,
                status = status,
                request = request,
                error = error,
            )
        }
    }
}
