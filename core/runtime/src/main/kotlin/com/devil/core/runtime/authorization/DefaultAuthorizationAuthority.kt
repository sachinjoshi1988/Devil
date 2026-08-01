package com.devil.core.runtime.authorization

import com.devil.core.model.context.ContextEnvelope
import com.devil.core.runtime.identity.IdentityResult
import com.devil.core.runtime.trust.TrustResult

/**
 * Default Stage 2 implementation of authorization evaluation.
 *
 * The current runtime has no authorization policy capable of granting or
 * denying continuation. This implementation therefore preserves trace
 * continuity and defers authorization without inventing authority.
 *
 * It performs no identity resolution, trust evaluation, understanding,
 * decision-making, planning, capability authorization, execution, or
 * verification.
 */
class DefaultAuthorizationAuthority : AuthorizationAuthority {

    override fun authorize(
        context: ContextEnvelope,
        identity: IdentityResult,
        trust: TrustResult,
    ): AuthorizationResult {
        require(identity.traceId == context.traceId) {
            "Context and identity result must use the same trace identity."
        }

        require(trust.traceId == context.traceId) {
            "Context and trust result must use the same trace identity."
        }

        return AuthorizationResult.create(
            traceId = context.traceId,
            status = AuthorizationStatus.DEFERRED,
        )
    }
}
