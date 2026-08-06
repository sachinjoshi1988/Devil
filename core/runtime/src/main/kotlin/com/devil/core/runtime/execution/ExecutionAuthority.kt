package com.devil.core.runtime.execution

import com.devil.core.model.context.ContextEnvelope
import com.devil.core.runtime.authorization.AuthorizationResult
import com.devil.core.runtime.capability.CapabilitySelectionResult
import com.devil.core.runtime.decision.DecisionAuthorityResult
import com.devil.core.runtime.executive.ExecutiveReadinessResult
import com.devil.core.runtime.identity.IdentityResult
import com.devil.core.runtime.plan.PlanAuthorityResult
import com.devil.core.runtime.task.TaskAuthorityResult
import com.devil.core.runtime.trust.TrustResult
import com.devil.core.runtime.understanding.UnderstandingAuthorityResult

/**
 * Coordinates bounded constitutional execution evaluation after affirmative
 * Executive readiness.
 *
 * This authority does not activate capabilities, invoke platform APIs, perform
 * actions, invent execution attempts, observe execution, verify outcomes, or
 * report final success.
 */
interface ExecutionAuthority {

    fun evaluate(
        context: ContextEnvelope,
        identity: IdentityResult,
        trust: TrustResult,
        authorization: AuthorizationResult,
        understanding: UnderstandingAuthorityResult,
        decision: DecisionAuthorityResult,
        task: TaskAuthorityResult,
        plan: PlanAuthorityResult,
        capability: CapabilitySelectionResult,
        readiness: ExecutiveReadinessResult,
    ): ExecutionResult
}
