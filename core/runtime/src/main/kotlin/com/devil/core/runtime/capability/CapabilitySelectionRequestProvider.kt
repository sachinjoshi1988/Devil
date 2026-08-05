package com.devil.core.runtime.capability

import com.devil.core.runtime.plan.PlanAuthorityResult

/**
 * Supplies one structured constitutional capability-selection request when one
 * bounded PlanRecord has been created.
 *
 * This provider does not select capabilities, establish availability or health,
 * grant authorization, check operating-system permission, execute actions,
 * observe results, verify outcomes, or report final outcomes.
 */
interface CapabilitySelectionRequestProvider {

    fun provide(
        plan: PlanAuthorityResult,
    ): CapabilitySelectionRequestResult
}
