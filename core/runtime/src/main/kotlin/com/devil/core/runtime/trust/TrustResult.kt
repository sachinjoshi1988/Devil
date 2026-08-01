package com.devil.core.runtime.trust

import com.devil.core.model.common.TraceId
import com.devil.core.model.context.ContextTrustLevel
import com.devil.core.model.error.UniversalErrorRecord

/**
 * Represents the structured result of trust evaluation.
 *
 * An evaluated result contains a trust level. A deferred result contains
 * neither trust level nor error. A failed result contains a matching error.
 */
@ConsistentCopyVisibility
data class TrustResult private constructor(
    val traceId: TraceId,
    val status: TrustStatus,
    val trustLevel: ContextTrustLevel?,
    val error: UniversalErrorRecord?,
) {
    companion object {
        fun create(
            traceId: TraceId,
            status: TrustStatus,
            trustLevel: ContextTrustLevel? = null,
            error: UniversalErrorRecord? = null,
        ): TrustResult {
            when (status) {
                TrustStatus.EVALUATED -> {
                    require(trustLevel != null && error == null) {
                        "Evaluated trust results require a trust level and must not contain an error."
                    }
                }

                TrustStatus.DEFERRED -> {
                    require(trustLevel == null && error == null) {
                        "Deferred trust results must not contain a trust level or error."
                    }
                }

                TrustStatus.FAILED -> {
                    require(trustLevel == null && error != null) {
                        "Failed trust results require an error and must not contain a trust level."
                    }
                }
            }

            require(error == null || error.traceId == traceId) {
                "Trust result and error must use the same trace identity."
            }

            return TrustResult(
                traceId = traceId,
                status = status,
                trustLevel = trustLevel,
                error = error,
            )
        }
    }
}
