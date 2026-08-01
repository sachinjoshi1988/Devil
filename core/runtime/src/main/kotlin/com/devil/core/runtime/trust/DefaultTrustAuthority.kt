package com.devil.core.runtime.trust

import com.devil.core.model.context.ContextEnvelope
import com.devil.core.runtime.identity.IdentityResult
import com.devil.core.runtime.identity.IdentityStatus

/**
 * Default Stage 2 implementation of trust evaluation.
 *
 * Trust is evaluated only after identity has been resolved. If identity remains
 * unresolved or has failed, trust evaluation is deferred without inventing a
 * trust conclusion.
 *
 * It performs no identity resolution, authentication, authorization,
 * reasoning, planning, or execution.
 */
class DefaultTrustAuthority : TrustAuthority {

    override fun evaluate(
        context: ContextEnvelope,
        identity: IdentityResult,
    ): TrustResult {
        require(identity.traceId == context.traceId) {
            "Context and identity result must use the same trace identity."
        }

        return when (identity.status) {
            IdentityStatus.RESOLVED -> TrustResult.create(
                traceId = context.traceId,
                status = TrustStatus.EVALUATED,
                trustLevel = context.trustLevel,
            )

            IdentityStatus.UNRESOLVED,
            IdentityStatus.FAILED,
            -> TrustResult.create(
                traceId = context.traceId,
                status = TrustStatus.DEFERRED,
            )
        }
    }
}
