package com.devil.app.capability

import com.devil.core.model.capability.CapabilityContract

/**
 * Default Stage 28 Android capability availability-and-health coordinator.
 *
 * This coordinator preserves one existing CapabilityContract and obtains only
 * bounded availability and health values from their dedicated sources.
 *
 * It does not derive availability from registration.
 *
 * It does not treat health READY as Executive readiness.
 *
 * It does not inspect or grant Android permission, grant Devil authorization,
 * activate capabilities, execute actions, observe results, verify outcomes, or
 * report success.
 */
class DefaultAndroidCapabilityStateProvider(
    private val availabilitySource: AndroidCapabilityAvailabilitySource =
        DefaultAndroidCapabilityAvailabilitySource(),
    private val healthSource: AndroidCapabilityHealthSource =
        DefaultAndroidCapabilityHealthSource(),
) : AndroidCapabilityStateProvider {

    override fun stateOf(
        capability: CapabilityContract,
    ): AndroidCapabilityState {
        return AndroidCapabilityState.create(
            capability = capability,
            availability =
                availabilitySource.availability(
                    capability = capability,
                ),
            health =
                healthSource.health(
                    capability = capability,
                ),
        )
    }
}
