package com.devil.core.runtime.security

import com.devil.core.model.common.TraceId
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.security.SessionValidityRequest

/**
 * Represents the stable operational result of constitutional session-validity
 * evaluation.
 *
 * VALID and INVALID results preserve the evaluated SessionValidityRequest.
 *
 * Preserving that request does not mutate SessionRecord, extend a session,
 * authenticate a subject, grant authorization, advance SecurityStage, enter
 * Owner Mode, approve high-security confirmation, grant Android permission, or
 * permit capability execution.
 *
 * A DEFERRED result contains neither request nor error.
 *
 * A FAILED result contains one matching error.
 */
@ConsistentCopyVisibility
data class SessionValidityResult private constructor(
    val traceId: TraceId,
    val status: SessionValidityStatus,
    val request: SessionValidityRequest?,
    val error: UniversalErrorRecord?,
) {
    companion object {
        fun create(
            traceId: TraceId,
            status: SessionValidityStatus,
            request: SessionValidityRequest? = null,
            error: UniversalErrorRecord? = null,
        ): SessionValidityResult {
            when (status) {
                SessionValidityStatus.VALID,
                SessionValidityStatus.INVALID,
                -> {
                    require(request != null && error == null) {
                        "Determined session validity results require a request and must not contain an error."
                    }
                }

                SessionValidityStatus.DEFERRED -> {
                    require(request == null && error == null) {
                        "Deferred session validity results must not contain a request or error."
                    }
                }

                SessionValidityStatus.FAILED -> {
                    require(request == null && error != null) {
                        "Failed session validity results require an error and must not contain a request."
                    }
                }
            }

            require(
                request == null ||
                    request.context.traceId == traceId,
            ) {
                "Session validity result and request must use the same trace identity."
            }

            require(error == null || error.traceId == traceId) {
                "Session validity result and error must use the same trace identity."
            }

            return SessionValidityResult(
                traceId = traceId,
                status = status,
                request = request,
                error = error,
            )
        }
    }
}
