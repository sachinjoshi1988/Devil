package com.devil.app.capability

import com.devil.core.model.capability.CapabilityAvailabilityState
import com.devil.core.model.capability.CapabilityContract

/**
 * Default Stage 28 Android capability-availability source.
 *
 * No production Android capability implementation currently supplies genuine
 * availability evidence.
 *
 * Therefore this source returns UNAVAILABLE rather than treating capability
 * registration, Android API presence, hardware presence, component presence, or
 * permission declaration as proof of availability.
 *
 * This implementation invokes no Android platform API and performs no action.
 */
class DefaultAndroidCapabilityAvailabilitySource :
    AndroidCapabilityAvailabilitySource {

    override fun availability(
        capability: CapabilityContract,
    ): CapabilityAvailabilityState {
        return CapabilityAvailabilityState.UNAVAILABLE
    }
}
