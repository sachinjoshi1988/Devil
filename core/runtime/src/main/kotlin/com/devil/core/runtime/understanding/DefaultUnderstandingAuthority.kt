package com.devil.core.runtime.understanding

import com.devil.core.model.context.ContextEnvelope
import com.devil.core.runtime.authorization.AuthorizationResult
import com.devil.core.runtime.identity.IdentityResult
import com.devil.core.runtime.trust.TrustResult

/**
 * Default Stage 2 implementation of structured understanding.
 *
 * The current ContextEnvelope carries constitutional metadata but no semantic
 * interaction content from which an UnderstandingRecord can honestly be
 * produced. This implementation therefore preserves trace continuity and
 * defers understanding without inventing meaning.
 *
 * It performs no identity resolution, trust evaluation, authorization,
 * decision-making, task creation, planning, execution, or verification.
 */
class DefaultUnderstandingAuthority : UnderstandingAuthority {

    override fun understand(
        context: ContextEnvelope,
        identity: IdentityResult,
        trust: TrustResult,
        authorization: AuthorizationResult,
    ): UnderstandingAuthorityResult {
        require(identity.traceId == context.traceId) {
            "Context and identity result must use the same trace identity."
        }

        require(trust.traceId == context.traceId) {
            "Context and trust result must use the same trace identity."
        }

        require(authorization.traceId == context.traceId) {
            "Context and authorization result must use the same trace identity."
        }

        return UnderstandingAuthorityResult.create(
            traceId = context.traceId,
            status = UnderstandingAuthorityStatus.DEFERRED,
        )
    }
}
