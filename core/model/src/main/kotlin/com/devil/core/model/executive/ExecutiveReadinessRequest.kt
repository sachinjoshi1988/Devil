package com.devil.core.model.executive

import com.devil.core.model.capability.CapabilityContract
import com.devil.core.model.plan.PlanRecord

/**
 * Represents one structured request for bounded constitutional Executive
 * readiness evaluation.
 *
 * The request preserves one existing PlanRecord and one selected registered
 * CapabilityContract without reinterpreting planning or capability selection.
 *
 * This request does not grant authorization, establish capability availability
 * or health, check operating-system permission, permit execution, execute
 * actions, observe results, verify outcomes, or report final outcomes.
 */
@ConsistentCopyVisibility
data class ExecutiveReadinessRequest private constructor(
    val plan: PlanRecord,
    val capability: CapabilityContract,
) {
    companion object {
        fun create(
            plan: PlanRecord,
            capability: CapabilityContract,
        ): ExecutiveReadinessRequest {
            return ExecutiveReadinessRequest(
                plan = plan,
                capability = capability,
            )
        }
    }
}
