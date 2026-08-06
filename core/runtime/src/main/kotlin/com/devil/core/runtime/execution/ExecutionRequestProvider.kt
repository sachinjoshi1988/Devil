package com.devil.core.runtime.execution

import com.devil.core.runtime.capability.CapabilitySelectionResult
import com.devil.core.runtime.executive.ExecutiveReadinessResult
import com.devil.core.runtime.plan.PlanAuthorityResult

/**
 * Supplies one structured constitutional execution request when a bounded plan,
 * selected registered capability, and affirmative Executive readiness result
 * are available.
 *
 * This provider does not establish capability health, check operating-system
 * permission, activate capabilities, execute actions, observe execution,
 * verify outcomes, or report final success.
 */
interface ExecutionRequestProvider {

    fun provide(
        plan: PlanAuthorityResult,
        capability: CapabilitySelectionResult,
        readiness: ExecutiveReadinessResult,
    ): ExecutionRequestResult
}
