package com.devil.core.runtime.understanding

import com.devil.core.model.context.ContextEnvelope
import com.devil.core.runtime.authorization.AuthorizationResult
import com.devil.core.runtime.identity.IdentityResult
import com.devil.core.runtime.trust.TrustResult

/**
 * Produces structured understanding after constitutionally ordered identity,
 * trust, and authorization processing.
 *
 * This authority does not resolve identity, evaluate trust, grant authority,
 * select decisions, create tasks, plan, execute, or verify outcomes.
 */
interface UnderstandingAuthority {

    fun understand(
        context: ContextEnvelope,
        identity: IdentityResult,
        trust: TrustResult,
        authorization: AuthorizationResult,
    ): UnderstandingAuthorityResult
}
