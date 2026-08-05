package com.devil.core.runtime.plan

import com.devil.core.model.common.TraceId
import com.devil.core.model.plan.PlanCreationRequest

/**
 * Supplies one bounded constitutional planning strategy for a structured
 * plan-creation request when an authorized planning policy is available.
 *
 * This provider must not fabricate strategy. It does not generate plan
 * identity, create plans, bind or authorize capabilities, execute actions,
 * observe results, verify outcomes, or report final outcomes.
 */
interface PlanningStrategyProvider {

    fun provide(
        traceId: TraceId,
        request: PlanCreationRequest,
    ): PlanningStrategyProvisionResult
}
