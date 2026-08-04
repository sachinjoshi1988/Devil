package com.devil.core.runtime.identity

import com.devil.core.model.common.TraceId
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.identity.IdentityResolutionRequest

/**
 * Represents the result of attempting to supply an identity-resolution request.
 *
 * An available result contains a request. An unavailable result contains
 * neither request nor error. A failed result contains a matching error.
 */
@ConsistentCopyVisibility
data class IdentityResolutionRequestResult private constructor(
    val traceId: TraceId,
    val status: IdentityResolutionRequestStatus,
    val request: IdentityResolutionRequest?,
    val error: UniversalErrorRecord?,
) {
    companion object {
        fun create(
            traceId: TraceId,
            status: IdentityResolutionRequestStatus,
            request: IdentityResolutionRequest? = null,
            error: UniversalErrorRecord? = null,
        ): IdentityResolutionRequestResult {
            when (status) {
                IdentityResolutionRequestStatus.AVAILABLE -> {
                    require(request != null && error == null) {
                        "Available identity resolution request results require a request and must not contain an error."
                    }

                    require(request.context.traceId == traceId) {
                        "Identity resolution request result and request must use the same trace identity."
                    }
                }

                IdentityResolutionRequestStatus.UNAVAILABLE -> {
                    require(request == null && error == null) {
                        "Unavailable identity resolution request results must not contain a request or error."
                    }
                }

                IdentityResolutionRequestStatus.FAILED -> {
                    require(request == null && error != null) {
                        "Failed identity resolution request results require an error and must not contain a request."
                    }
                }
            }

            require(error == null || error.traceId == traceId) {
                "Identity resolution request result and error must use the same trace identity."
            }

            return IdentityResolutionRequestResult(
                traceId = traceId,
                status = status,
                request = request,
                error = error,
            )
        }
    }
}
