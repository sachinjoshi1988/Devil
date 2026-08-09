package com.devil.app.capability

import com.devil.core.model.capability.CapabilitySelectionRequest
import com.devil.core.model.common.TraceId
import com.devil.core.runtime.capability.CapabilityRegistryResult
import com.devil.core.runtime.capability.CapabilityRegistryStatus

/**
 * Default Stage 27 Android Capability Registry.
 *
 * The registry obtains only explicit Android capability registrations from the
 * supplied AndroidCapabilityRegistrationSource.
 *
 * An empty registration source maps truthfully to UNAVAILABLE.
 *
 * One or more genuine registrations map to AVAILABLE and are preserved without
 * reinterpretation.
 *
 * CapabilityRegistryResult remains responsible for enforcing unique capability
 * identity.
 *
 * This implementation does not establish availability, health, readiness,
 * operating-system permission, Devil authorization, execution permission,
 * execution success, observation, verification, or outcome.
 *
 * It invokes no Android platform API and performs no capability action.
 */
class DefaultAndroidCapabilityRegistry(
    private val registrationSource: AndroidCapabilityRegistrationSource =
        DefaultAndroidCapabilityRegistrationSource(),
) : AndroidCapabilityRegistry {

    override fun obtain(
        traceId: TraceId,
        request: CapabilitySelectionRequest,
    ): CapabilityRegistryResult {
        require(
            request.plan.task.decision.understanding.context.traceId ==
                traceId,
        ) {
            "Android capability registry trace and capability-selection request must use the same trace identity."
        }

        val registrations =
            registrationSource.registrations().toList()

        return if (registrations.isEmpty()) {
            CapabilityRegistryResult.create(
                traceId = traceId,
                status = CapabilityRegistryStatus.UNAVAILABLE,
            )
        } else {
            CapabilityRegistryResult.create(
                traceId = traceId,
                status = CapabilityRegistryStatus.AVAILABLE,
                capabilities = registrations,
            )
        }
    }
}
