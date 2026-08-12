package com.devil.core.runtime.plan

import com.devil.core.model.common.TraceId
import com.devil.core.model.plan.PlanCreationRequest
import com.devil.core.model.plan.PlanId

/**
 * Default bounded constitutional Plan identity provider.
 *
 * Stage 59 establishes one deterministic Plan identity from the already-created
 * Task identity:
 *
 * plan:<task-id>
 *
 * PlanId therefore remains distinct from TaskId while preserving direct
 * constitutional traceability to the originating task.
 *
 * Providing a PlanId does not create planning strategy, create a plan, bind or
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
            status = PlanIdentityProvisionStatus.AVAILABLE,
            planId = PlanId.from(
                "plan:${request.task.taskId.value}",
            ),
        )
    }
}
