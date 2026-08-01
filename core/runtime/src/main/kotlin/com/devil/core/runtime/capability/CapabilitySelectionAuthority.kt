package com.devil.core.runtime.capability

import com.devil.core.model.context.ContextEnvelope
import com.devil.core.runtime.authorization.AuthorizationResult
import com.devil.core.runtime.decision.DecisionAuthorityResult
import com.devil.core.runtime.identity.IdentityResult
import com.devil.core.runtime.plan.PlanAuthorityResult
import com.devil.core.runtime.task.TaskAuthorityResult
import com.devil.core.runtime.trust.TrustResult
import com.devil.core.runtime.understanding.UnderstandingAuthorityResult

/**
 * Selects one registered capability contract after the preceding runtime
 * authorities have completed their bounded responsibilities.
 *
 * This authority does not grant authorization, establish availability, health,
 * readiness, operating-system permission, or permission to execute. It does not
 * execute capabilities, invent observations, verify outcomes, or report
 * success.
 */
interface CapabilitySelectionAuthority {

    fun select(
        context: ContextEnvelope,
        identity: IdentityResult,
        trust: TrustResult,
        authorization: AuthorizationResult,
        understanding: UnderstandingAuthorityResult,
        decision: DecisionAuthorityResult,
        task: TaskAuthorityResult,
        plan: PlanAuthorityResult,
    ): CapabilitySelectionResult
}
