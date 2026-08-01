package com.devil.core.runtime.identity

import com.devil.core.model.common.TraceId
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.identity.IdentityId

/**
 * Represents the structured result of identity resolution.
 *
 * A resolved result contains an identity. An unresolved result contains neither
 * identity nor error. A failed result contains a matching error.
 */
@ConsistentCopyVisibility
data class IdentityResult private constructor(
    val traceId: TraceId,
    val status: IdentityStatus,
    val identityId: IdentityId?,
    val error: UniversalErrorRecord?,
) {
    companion object {
        fun create(
            traceId: TraceId,
            status: IdentityStatus,
            identityId: IdentityId? = null,
            error: UniversalErrorRecord? = null,
        ): IdentityResult {
            when (status) {
                IdentityStatus.RESOLVED -> {
                    require(identityId != null && error == null) {
                        "Resolved identity results require an identity and must not contain an error."
                    }
                }

                IdentityStatus.UNRESOLVED -> {
                    require(identityId == null && error == null) {
                        "Unresolved identity results must not contain an identity or error."
                    }
                }

                IdentityStatus.FAILED -> {
                    require(identityId == null && error != null) {
                        "Failed identity results require an error and must not contain an identity."
                    }
                }
            }

            require(error == null || error.traceId == traceId) {
                "Identity result and error must use the same trace identity."
            }

            return IdentityResult(
                traceId = traceId,
                status = status,
                identityId = identityId,
                error = error,
            )
        }
    }
}
