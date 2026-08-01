package com.devil.core.runtime.plan

import com.devil.core.model.context.ContextEnvelope
import com.devil.core.runtime.authorization.AuthorizationResult
import com.devil.core.runtime.decision.DecisionAuthorityResult
import com.devil.core.runtime.identity.IdentityResult
import com.devil.core.runtime.task.TaskAuthorityResult
import com.devil.core.runtime.trust.TrustResult
import com.devil.core.runtime.understanding.UnderstandingAuthorityResult

/**
 * Default Stage 2 implementation of constitutional plan creation.
 *
 * The current runtime has no planning engine capable of creating a justified
 * PlanRecord. This implementation therefore preserves trace continuity and
 * defers planning without inventing a plan.
 *
 * It performs no identity resolution, trust evaluation, authorization,
 * understanding, decision selection, task creation, capability binding,
 * execution, observation, verification, or outcome reporting.
 */
class DefaultPlanAuthority : PlanAuthority {

    override fun createPlan(
        context: ContextEnvelope,
        identity: IdentityResult,
        trust: TrustResult,
        authorization: AuthorizationResult,
        understanding: UnderstandingAuthorityResult,
        decision: DecisionAuthorityResult,
        task: TaskAuthorityResult,
    ): PlanAuthorityResult {
        require(identity.traceId == context.traceId) {
            "Context and identity result must use the same trace identity."
        }

        require(trust.traceId == context.traceId) {
            "Context and trust result must use the same trace identity."
        }

        require(authorization.traceId == context.traceId) {
            "Context and authorization result must use the same trace identity."
        }

        require(understanding.traceId == context.traceId) {
            "Context and understanding result must use the same trace identity."
        }

        require(decision.traceId == context.traceId) {
            "Context and decision result must use the same trace identity."
        }

        require(task.traceId == context.traceId) {
            "Context and task result must use the same trace identity."
        }

        return PlanAuthorityResult.create(
            traceId = context.traceId,
            status = PlanAuthorityStatus.DEFERRED,
        )
    }
}
