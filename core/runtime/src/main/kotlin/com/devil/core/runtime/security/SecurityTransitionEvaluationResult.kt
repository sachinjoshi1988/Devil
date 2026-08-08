package com.devil.core.runtime.security

import com.devil.core.model.common.TraceId
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.security.SecurityTransitionRequest

/**
 * Represents the bounded result of constitutional security-transition evaluation.
 *
 * An approved result preserves the evaluated SecurityTransitionRequest.
 *
 * Preserving that request does not itself advance security state, authenticate a
 * subject, create or validate a session, enter Owner Mode, or approve a
 * high-security action.
 *
 * An unavailable result contains neither request nor error.
 *
 * A failed result contains one matching error.
 */
@ConsistentCopyVisibility
data class SecurityTransitionEvaluationResult private constructor(
    val traceId: TraceId,
    val status: SecurityTransitionEvaluationStatus,
    val request: SecurityTransitionRequest?,
    val error: UniversalErrorRecord?,
) {
    companion object {
        fun create(
            traceId: TraceId,
            status: SecurityTransitionEvaluationStatus,
            request: SecurityTransitionRequest? = null,
            error: UniversalErrorRecord? = null,
        ): SecurityTransitionEvaluationResult {
            when (status) {
                SecurityTransitionEvaluationStatus.APPROVED -> {
                    require(request != null && error == null) {
                        "Approved security transition evaluation results require a request and must not contain an error."
                    }
                }

                SecurityTransitionEvaluationStatus.UNAVAILABLE -> {
                    require(request == null && error == null) {
                        "Unavailable security transition evaluation results must not contain a request or error."
                    }
                }

                SecurityTransitionEvaluationStatus.FAILED -> {
                    require(request == null && error != null) {
                        "Failed security transition evaluation results require an error and must not contain a request."
                    }
                }
            }

            require(
                request == null ||
                    request.context.traceId == traceId,
            ) {
                "Security transition evaluation result and request must use the same trace identity."
            }

            require(error == null || error.traceId == traceId) {
                "Security transition evaluation result and error must use the same trace identity."
            }

            return SecurityTransitionEvaluationResult(
                traceId = traceId,
                status = status,
                request = request,
                error = error,
            )
        }
    }
}
