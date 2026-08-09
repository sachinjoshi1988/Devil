package com.devil.app.capability

import com.devil.app.accessibility.AndroidAccessibilityCapability
import com.devil.app.accessibility.DevilAccessibilityServiceRegistry
import com.devil.core.model.capability.CapabilityAvailabilityState
import com.devil.core.model.capability.CapabilityContract

/**
 * Default Android capability-availability source.
 *
 * Stage 38 can truthfully establish availability for the registered Android
 * accessibility capability only while DevilAccessibilityService is genuinely
 * connected to the process.
 *
 * Every capability without such approved Android availability evidence remains
 * UNAVAILABLE.
 *
 * Service connected != authentication.
 * Service connected != Devil authorization.
 * Service connected != Execution APPROVED.
 * Service connected != action attempted.
 */
class DefaultAndroidCapabilityAvailabilitySource :
    AndroidCapabilityAvailabilitySource {

    override fun availability(
        capability: CapabilityContract,
    ): CapabilityAvailabilityState {
        if (!AndroidAccessibilityCapability.matches(capability)) {
            return CapabilityAvailabilityState.UNAVAILABLE
        }

        return if (
            DevilAccessibilityServiceRegistry.current() != null
        ) {
            CapabilityAvailabilityState.AVAILABLE
        } else {
            CapabilityAvailabilityState.UNAVAILABLE
        }
    }
}
