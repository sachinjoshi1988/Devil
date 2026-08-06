package com.devil.core.model.execution

import com.devil.core.model.capability.CapabilityContract
import com.devil.core.model.plan.PlanRecord

/**
 * Represents one structured request to approach bounded constitutional
 * execution.
 *
 * The request preserves one existing PlanRecord and one selected registered
 * CapabilityContract after affirmative Executive readiness. It does not
 * reinterpret planning, capability selection, or readiness.
 *
 * This request does not establish capability availability or health, grant
 * authorization, check operating-system permission, activate a capability,
 * execute an action, observe an execution attempt, verify an outcome, or report
 * final success.
 */
@ConsistentCopyVisibility
data class ExecutionRequest private constructor(
    val plan: PlanRecord,
    val capability: CapabilityContract,
) {
    companion object {
        fun create(
            plan: PlanRecord,
            capability: CapabilityContract,
        ): ExecutionRequest {
            return ExecutionRequest(
                plan = plan,
                capability = capability,
            )
        }
    }
}
