package com.devil.core.runtime.plan

import com.devil.core.model.common.TraceId
import com.devil.core.model.plan.PlanCreationRequest

/**
 * Default Stage 9 plan identity provider.
 *
 * No constitutional plan identity policy exists yet. Therefore this provider
 * preserves trace continuity and reports that no plan identity is available
 * rather than fabricating one.
 *
 * This implementation does not create planning strategy, create plans, bind or
 * authorize capabilities, execute actions, observe results, verify outcomes,
 * or report final outcomes.
 */
class DefaultPlanIdentityProvider : PlanIdentityProvider {

    override fun provide(
        traceId: TraceId,
        request: PlanCreationRequest,
    ): PlanIdentityProvisionResult {
        require(
            request.task.decision.understanding.context.traceId == traceId,
        ) {
            "Plan identity trace and plan-creation request must use the same trace identity."
        }

        return PlanIdentityProvisionResult.create(
            traceId = traceId,
            status = PlanIdentityProvisionStatus.UNAVAILABLE,
        )
    }
}
