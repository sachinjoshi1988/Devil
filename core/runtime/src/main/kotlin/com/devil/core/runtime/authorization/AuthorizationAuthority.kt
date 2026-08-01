package com.devil.core.runtime.authorization

import com.devil.core.model.context.ContextEnvelope
import com.devil.core.runtime.identity.IdentityResult
import com.devil.core.runtime.trust.TrustResult

/**
 * Evaluates whether supplied context may continue beyond the authorization
 * boundary after identity resolution and trust evaluation.
 *
 * This authority does not resolve identity, evaluate trust, perform
 * understanding, select decisions, plan, authorize individual capabilities,
 * execute actions, or verify outcomes.
 */
interface AuthorizationAuthority {

    fun authorize(
        context: ContextEnvelope,
        identity: IdentityResult,
        trust: TrustResult,
    ): AuthorizationResult
}
