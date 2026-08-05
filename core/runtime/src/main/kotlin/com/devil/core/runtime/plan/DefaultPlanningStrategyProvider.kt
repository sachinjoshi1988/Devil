package com.devil.core.runtime.plan

import com.devil.core.model.common.TraceId
import com.devil.core.model.plan.PlanCreationRequest

/**
 * Default Stage 9 planning strategy provider.
 *
 * No constitutional planning policy exists yet. Therefore this provider
 * preserves trace continuity and reports that no planning strategy is
 * available rather than copying upstream summaries or fabricating strategy.
 *
 * This implementation does not generate plan identity, create plans, bind or
 * authorize capabilities, execute actions, observe results, verify outcomes,
 * or report final outcomes.
 */
class DefaultPlanningStrategyProvider : PlanningStrategyProvider {

    override fun provide(
        traceId: TraceId,
        request: PlanCreationRequest,
    ): PlanningStrategyProvisionResult {
        require(
            request.task.decision.understanding.context.traceId == traceId,
        ) {
            "Planning strategy trace and plan-creation request must use the same trace identity."
        }

        return PlanningStrategyProvisionResult.create(
            traceId = traceId,
            status = PlanningStrategyProvisionStatus.UNAVAILABLE,
        )
    }
}
