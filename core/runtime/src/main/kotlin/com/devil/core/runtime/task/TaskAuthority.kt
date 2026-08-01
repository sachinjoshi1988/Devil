package com.devil.core.runtime.task

import com.devil.core.model.context.ContextEnvelope
import com.devil.core.runtime.authorization.AuthorizationResult
import com.devil.core.runtime.decision.DecisionAuthorityResult
import com.devil.core.runtime.identity.IdentityResult
import com.devil.core.runtime.trust.TrustResult
import com.devil.core.runtime.understanding.UnderstandingAuthorityResult

/**
 * Creates one constitutional task from a selected decision after the preceding
 * runtime authorities have completed their bounded responsibilities.
 *
 * This authority does not resolve identity, evaluate trust, grant authority,
 * produce understanding, select decisions, plan, bind capabilities, execute,
 * or verify outcomes.
 */
interface TaskAuthority {

    fun createTask(
        context: ContextEnvelope,
        identity: IdentityResult,
        trust: TrustResult,
        authorization: AuthorizationResult,
        understanding: UnderstandingAuthorityResult,
        decision: DecisionAuthorityResult,
    ): TaskAuthorityResult
}
