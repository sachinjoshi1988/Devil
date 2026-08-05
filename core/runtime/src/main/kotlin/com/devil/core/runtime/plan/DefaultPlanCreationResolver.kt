package com.devil.core.runtime.plan

import com.devil.core.model.plan.PlanCreationRequest
import com.devil.core.model.plan.PlanId
import com.devil.core.model.plan.PlanRecord
import com.devil.core.model.plan.PlanState

/**
 * Default Stage 9 constitutional plan-creation resolver.
 *
 * This resolver preserves the originating TaskRecord, applies the supplied
 * genuine PlanId and planning strategy, and creates one PlanRecord in the
 * CREATED lifecycle state.
 *
 * It does not generate plan identity, create or reinterpret planning strategy,
 * bind or authorize capabilities, execute actions, observe results, verify
 * outcomes, or report final outcomes.
 */
class DefaultPlanCreationResolver : PlanCreationResolver {

    override fun create(
        request: PlanCreationRequest,
        planId: PlanId,
        strategy: String,
    ): PlanRecord {
        return PlanRecord.create(
            planId = planId,
            task = request.task,
            state = PlanState.CREATED,
            summary = strategy,
        )
    }
}
