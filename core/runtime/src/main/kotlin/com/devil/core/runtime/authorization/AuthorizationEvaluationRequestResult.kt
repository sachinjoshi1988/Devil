package com.devil.core.runtime.authorization

import com.devil.core.model.authorization.AuthorizationEvaluationRequest
import com.devil.core.model.common.TraceId
import com.devil.core.model.error.UniversalErrorRecord

/**
 * Represents the result of constructing an authorization-evaluation request.
 *
 * An available result contains a request. An unavailable result contains
 * neither request nor error. A failed result contains a matching error.
 */
@ConsistentCopyVisibility
data class AuthorizationEvaluationRequestResult private constructor(
    val traceId: TraceId,
    val status: AuthorizationEvaluationRequestStatus,
    val request: AuthorizationEvaluationRequest?,
    val error: UniversalErrorRecord?,
) {
    companion object {
        fun create(
            traceId: TraceId,
            status: AuthorizationEvaluationRequestStatus,
            request: AuthorizationEvaluationRequest? = null,
            error: UniversalErrorRecord? = null,
        ): AuthorizationEvaluationRequestResult {
            when (status) {
                AuthorizationEvaluationRequestStatus.AVAILABLE -> {
                    require(request != null && error == null) {
                        "Available authorization-evaluation request results require a request and must not contain an error."
                    }

                    require(request.context.traceId == traceId) {
                        "Authorization-evaluation request and result must use the same trace identity."
                    }
                }

                AuthorizationEvaluationRequestStatus.UNAVAILABLE -> {
                    require(request == null && error == null) {
                        "Unavailable authorization-evaluation request results must not contain a request or error."
                    }
                }

                AuthorizationEvaluationRequestStatus.FAILED -> {
                    require(request == null && error != null) {
                        "Failed authorization-evaluation request results require an error and must not contain a request."
                    }

                    require(error.traceId == traceId) {
                        "Authorization-evaluation request result and error must use the same trace identity."
                    }
                }
            }

            return AuthorizationEvaluationRequestResult(
                traceId = traceId,
                status = status,
                request = request,
                error = error,
            )
        }
    }
}
