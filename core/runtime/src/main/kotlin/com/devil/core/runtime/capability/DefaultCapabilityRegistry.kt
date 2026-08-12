package com.devil.core.runtime.capability

import com.devil.core.model.capability.CapabilityCategory
import com.devil.core.model.capability.CapabilityContract
import com.devil.core.model.capability.CapabilityId
import com.devil.core.model.capability.CapabilitySelectionRequest
import com.devil.core.model.common.TraceId

/**
 * Default bounded constitutional capability registry.
 *
 * Stage 60 establishes the first deliberately small set of registered Devil
 * capability contracts.
 *
 * Registration means only that a capability contract is known to Devil.
 *
 * Registered does not mean available, healthy, authorized, ready, permitted by
 * the operating system, executed, observed, verified, or successful.
 *
 * This registry does not select capabilities, establish availability or health,
 * grant authorization, check operating-system permission, execute actions,
 * observe results, verify outcomes, or report final outcomes.
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
            status = CapabilityRegistryStatus.AVAILABLE,
            capabilities = REGISTERED_CAPABILITIES,
        )
    }

    private companion object {

        val REGISTERED_CAPABILITIES =
            listOf(
                CapabilityContract.create(
                    capabilityId =
                        CapabilityId.from(
                            "capability-camera",
                        ),
                    category = CapabilityCategory.ACTION,
                    name = "Camera",
                    description =
                        "Represents the bounded registered capability for opening or addressing the camera target.",
                ),
            )
    }
}
