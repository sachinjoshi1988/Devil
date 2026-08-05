package com.devil.core.runtime.plan

import com.devil.core.model.plan.PlanCreationRequest
import com.devil.core.model.plan.PlanId
import com.devil.core.model.plan.PlanRecord

/**
 * Creates one bounded constitutional PlanRecord from a structured plan-creation
 * request, one genuine plan identity, and one supplied planning strategy.
 *
 * This resolver does not generate plan identity, create or reinterpret planning
 * strategy, bind or authorize capabilities, execute actions, observe results,
 * verify outcomes, or report final outcomes.
 */
interface PlanCreationResolver {

    fun create(
        request: PlanCreationRequest,
        planId: PlanId,
        strategy: String,
    ): PlanRecord
}
