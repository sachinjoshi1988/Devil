package com.devil.core.runtime.decision

import com.devil.core.model.context.ContextEnvelope
import com.devil.core.runtime.authorization.AuthorizationResult
import com.devil.core.runtime.identity.IdentityResult
import com.devil.core.runtime.trust.TrustResult
import com.devil.core.runtime.understanding.UnderstandingAuthorityResult

/**
 * Selects one constitutional decision after the preceding runtime authorities
 * have completed their bounded responsibilities.
 *
 * This authority does not resolve identity, evaluate trust, grant authority,
 * produce understanding, create tasks, plan, execute, or verify outcomes.
 */
interface DecisionAuthority {

    fun decide(
        context: ContextEnvelope,
        identity: IdentityResult,
        trust: TrustResult,
        authorization: AuthorizationResult,
        understanding: UnderstandingAuthorityResult,
    ): DecisionAuthorityResult
}
