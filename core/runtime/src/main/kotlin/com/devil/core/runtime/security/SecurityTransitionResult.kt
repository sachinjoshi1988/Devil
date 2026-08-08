package com.devil.core.runtime.security

import com.devil.core.model.common.TraceId
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.security.SecurityTransitionRequest

/**
 * Represents the stable operational result of constitutional security-transition
 * evaluation.
 *
 * An approved result preserves one SecurityTransitionRequest for which genuine
 * constitutional transition eligibility was established.
 *
 * Preserving that request does not advance security state, authenticate a subject,
 * prove owner identity, establish trust, grant authorization, create or validate a
 * session, enter Owner Mode, approve high-security confirmation, grant Android
 * permission, or permit execution.
 *
 * A deferred result contains neither request nor error.
 *
 * A failed result contains one matching error.
 */
@ConsistentCopyVisibility
data class SecurityTransitionResult private constructor(
    val traceId: TraceId,
    val status: SecurityTransitionStatus,
    val request: SecurityTransitionRequest?,
    val error: UniversalErrorRecord?,
) {
    companion object {
        fun create(
            traceId: TraceId,
            status: SecurityTransitionStatus,
            request: SecurityTransitionRequest? = null,
            error: UniversalErrorRecord? = null,
        ): SecurityTransitionResult {
            when (status) {
                SecurityTransitionStatus.APPROVED -> {
                    require(request != null && error == null) {
                        "Approved security transition results require a request and must not contain an error."
                    }
                }

                SecurityTransitionStatus.DEFERRED -> {
                    require(request == null && error == null) {
                        "Deferred security transition results must not contain a request or error."
                    }
                }

                SecurityTransitionStatus.FAILED -> {
                    require(request == null && error != null) {
                        "Failed security transition results require an error and must not contain a request."
                    }
                }
            }

            require(
                request == null ||
                    request.context.traceId == traceId,
            ) {
                "Security transition result and request must use the same trace identity."
            }

            require(error == null || error.traceId == traceId) {
                "Security transition result and error must use the same trace identity."
            }

            return SecurityTransitionResult(
                traceId = traceId,
                status = status,
                request = request,
                error = error,
            )
        }
    }
}
