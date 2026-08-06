package com.devil.core.runtime.executive

import com.devil.core.runtime.capability.CapabilitySelectionResult
import com.devil.core.runtime.plan.PlanAuthorityResult

/**
 * Supplies one structured constitutional Executive-readiness request when a
 * bounded plan and selected registered capability are available.
 *
 * This provider does not establish readiness, authorize or permit execution,
 * check capability availability or health, evaluate operating-system
 * permission, execute actions, observe results, verify outcomes, or report
 * final outcomes.
 */
interface ExecutiveReadinessRequestProvider {

    fun provide(
        plan: PlanAuthorityResult,
        capability: CapabilitySelectionResult,
    ): ExecutiveReadinessRequestResult
}
