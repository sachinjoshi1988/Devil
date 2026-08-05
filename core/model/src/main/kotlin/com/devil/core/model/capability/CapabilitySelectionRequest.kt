package com.devil.core.model.capability

import com.devil.core.model.plan.PlanRecord

/**
 * Represents one structured request for bounded constitutional capability
 * selection.
 *
 * The request preserves one existing PlanRecord without reinterpreting its
 * planning strategy or lifecycle state and without selecting a capability.
 *
 * This request does not establish capability availability, health, operating-
 * system permission, authorization, readiness, execution, observation,
 * verification, or final outcome.
 */
@ConsistentCopyVisibility
data class CapabilitySelectionRequest private constructor(
    val plan: PlanRecord,
) {
    companion object {
        fun create(
            plan: PlanRecord,
        ): CapabilitySelectionRequest {
            return CapabilitySelectionRequest(
                plan = plan,
            )
        }
    }
}
