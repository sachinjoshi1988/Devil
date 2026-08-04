package com.devil.core.runtime.authorization

import com.devil.core.model.context.ContextEnvelope
import com.devil.core.runtime.identity.IdentityResult
import com.devil.core.runtime.trust.TrustResult

/**
 * Supplies a structured authorization-evaluation request when genuine subject
 * identity and trust-assessment inputs are available.
 *
 * This provider must not fabricate subject trust, authorization, capability
 * permission, operating-system permission, Owner Mode, or execution authority.
 */
interface AuthorizationEvaluationRequestProvider {

    fun provide(
        context: ContextEnvelope,
        identity: IdentityResult,
        trust: TrustResult,
    ): AuthorizationEvaluationRequestResult
}
