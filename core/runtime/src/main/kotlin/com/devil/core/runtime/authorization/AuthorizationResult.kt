package com.devil.core.runtime.authorization

import com.devil.core.model.common.TraceId
import com.devil.core.model.error.UniversalErrorRecord

/**
 * Represents the structured result of authorization evaluation.
 *
 * Authorized, denied, and deferred results contain no error. A failed result
 * requires a matching UniversalErrorRecord from the same trace.
 */
@ConsistentCopyVisibility
data class AuthorizationResult private constructor(
    val traceId: TraceId,
    val status: AuthorizationStatus,
    val error: UniversalErrorRecord?,
) {
    companion object {
        fun create(
            traceId: TraceId,
            status: AuthorizationStatus,
            error: UniversalErrorRecord? = null,
        ): AuthorizationResult {
            require(
                (status == AuthorizationStatus.FAILED) == (error != null),
            ) {
                "Failed authorization results must contain an error and non-failed results must not."
            }

            require(error == null || error.traceId == traceId) {
                "Authorization result and error must use the same trace identity."
            }

            return AuthorizationResult(
                traceId = traceId,
                status = status,
                error = error,
            )
        }
    }
}
