package com.devil.core.runtime.plan

import com.devil.core.model.context.ContextEnvelope
import com.devil.core.runtime.authorization.AuthorizationResult
import com.devil.core.runtime.decision.DecisionAuthorityResult
import com.devil.core.runtime.identity.IdentityResult
import com.devil.core.runtime.task.TaskAuthorityResult
import com.devil.core.runtime.trust.TrustResult
import com.devil.core.runtime.understanding.UnderstandingAuthorityResult

/**
 * Creates one constitutional plan for an existing task after the preceding
 * runtime authorities have completed their bounded responsibilities.
 *
 * This authority does not resolve identity, evaluate trust, grant authority,
 * produce understanding, select decisions, create tasks, bind capabilities,
 * execute, observe, or verify outcomes.
 */
interface PlanAuthority {

    fun createPlan(
        context: ContextEnvelope,
        identity: IdentityResult,
        trust: TrustResult,
        authorization: AuthorizationResult,
        understanding: UnderstandingAuthorityResult,
        decision: DecisionAuthorityResult,
        task: TaskAuthorityResult,
    ): PlanAuthorityResult
}
