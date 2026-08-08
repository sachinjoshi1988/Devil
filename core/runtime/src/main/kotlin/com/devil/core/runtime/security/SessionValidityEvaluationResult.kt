package com.devil.core.runtime.security

import com.devil.core.model.common.TraceId
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.security.SessionValidityRequest

/**
 * Represents the bounded result of constitutional session-validity evaluation.
 *
 * A VALID or INVALID result preserves the evaluated SessionValidityRequest.
 *
 * Preserving the request does not mutate session state, extend a session,
 * authenticate a subject, grant authorization, advance SecurityStage, enter
 * Owner Mode, approve high-security confirmation, grant Android permission, or
 * permit capability execution.
 *
 * An UNAVAILABLE result contains neither request nor error.
 *
 * A FAILED result contains one matching error.
 */
@ConsistentCopyVisibility
data class SessionValidityEvaluationResult private constructor(
    val traceId: TraceId,
    val status: SessionValidityEvaluationStatus,
    val request: SessionValidityRequest?,
    val error: UniversalErrorRecord?,
) {
    companion object {
        fun create(
            traceId: TraceId,
            status: SessionValidityEvaluationStatus,
            request: SessionValidityRequest? = null,
            error: UniversalErrorRecord? = null,
        ): SessionValidityEvaluationResult {
            when (status) {
                SessionValidityEvaluationStatus.VALID,
                SessionValidityEvaluationStatus.INVALID,
                -> {
                    require(request != null && error == null) {
                        "Determined session validity results require a request and must not contain an error."
                    }
                }

                SessionValidityEvaluationStatus.UNAVAILABLE -> {
                    require(request == null && error == null) {
                        "Unavailable session validity evaluation results must not contain a request or error."
                    }
                }

                SessionValidityEvaluationStatus.FAILED -> {
                    require(request == null && error != null) {
                        "Failed session validity evaluation results require an error and must not contain a request."
                    }
                }
            }

            require(
                request == null ||
                    request.context.traceId == traceId,
            ) {
                "Session validity evaluation result and request must use the same trace identity."
            }

            require(error == null || error.traceId == traceId) {
                "Session validity evaluation result and error must use the same trace identity."
            }

            return SessionValidityEvaluationResult(
                traceId = traceId,
                status = status,
                request = request,
                error = error,
            )
        }
    }
}
