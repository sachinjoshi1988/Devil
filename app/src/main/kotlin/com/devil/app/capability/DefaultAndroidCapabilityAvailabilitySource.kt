package com.devil.app.capability

import com.devil.app.accessibility.AndroidAccessibilityCapability
import com.devil.app.accessibility.DevilAccessibilityServiceRegistry
import com.devil.app.device.AndroidDeviceKnowledgeCapability
import com.devil.core.model.capability.CapabilityAvailabilityState
import com.devil.core.model.capability.CapabilityContract

/**
 * Default Android capability-availability source.
 *
 * Stage 38 accessibility availability requires a genuinely connected
 * DevilAccessibilityService.
 *
 * Stage 40 Device Knowledge is AVAILABLE because its approved bounded source
 * reads non-sensitive Android Build facts directly from the running platform
 * and requires no separate service connection.
 *
 * Availability describes embodiment availability only.
 *
 * Available != authenticated.
 * Available != Devil authorized.
 * Available != Executive ready.
 * Available != execution approved.
 * Available != verified outcome.
 */
class DefaultAndroidCapabilityAvailabilitySource :
    AndroidCapabilityAvailabilitySource {

    override fun availability(
        capability: CapabilityContract,
    ): CapabilityAvailabilityState {
        return when {
            AndroidAccessibilityCapability.matches(
                capability,
            ) ->
                if (
                    DevilAccessibilityServiceRegistry.current() != null
                ) {
                    CapabilityAvailabilityState.AVAILABLE
                } else {
                    CapabilityAvailabilityState.UNAVAILABLE
                }

            AndroidDeviceKnowledgeCapability.matches(
                capability,
            ) ->
                CapabilityAvailabilityState.AVAILABLE

            else ->
                CapabilityAvailabilityState.UNAVAILABLE
        }
    }
}
