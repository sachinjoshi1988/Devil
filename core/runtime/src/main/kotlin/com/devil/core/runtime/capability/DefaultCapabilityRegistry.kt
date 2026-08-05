package com.devil.core.runtime.capability

import com.devil.core.model.capability.CapabilitySelectionRequest
import com.devil.core.model.common.TraceId

/**
 * Default Stage 10 capability registry.
 *
 * No constitutional capability registration source exists yet. Therefore this
 * registry preserves trace continuity and reports that no registered
 * capabilities are available rather than fabricating registrations.
 *
 * This implementation does not select capabilities, establish availability or
 * health, grant authorization, check operating-system permission, execute
 * actions, observe results, verify outcomes, or report final outcomes.
 */
class DefaultCapabilityRegistry : CapabilityRegistry {

    override fun obtain(
        traceId: TraceId,
        request: CapabilitySelectionRequest,
    ): CapabilityRegistryResult {
        require(
            request.plan.task.decision.understanding.context.traceId ==
                traceId,
        ) {
            "Capability registry trace and capability-selection request must use the same trace identity."
        }

        return CapabilityRegistryResult.create(
            traceId = traceId,
            status = CapabilityRegistryStatus.UNAVAILABLE,
        )
    }
}
