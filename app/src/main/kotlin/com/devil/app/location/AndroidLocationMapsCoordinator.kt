package com.devil.app.location

/**
 * Stage 191 bounded Location & Maps coordinator.
 *
 * It accepts only explicitly supplied location coordinates.
 *
 * It does not:
 *
 * - inspect device location;
 * - call LocationManager or another location provider;
 * - request location permission;
 * - geocode or reverse-geocode;
 * - launch a maps application;
 * - perform navigation;
 * - grant Devil authorization;
 * - establish execution, Verification, or Outcome;
 * - implement Stage 192 Browser & Web Interaction.
 *
 * SUPPLIED_LOCATION != DEVICE_LOCATION.
 * LOCATION_AVAILABLE != LOCATION_AUTHORIZED.
 * MAPS_INTELLIGENCE != NAVIGATION_EXECUTION.
 */
class AndroidLocationMapsCoordinator {
    fun integrate(
        location: AndroidLocationRecord?,
    ): AndroidLocationMapsResult {
        if (location == null) {
            return AndroidLocationMapsResult.create(
                status = AndroidLocationMapsStatus.DEFERRED,
            )
        }

        return AndroidLocationMapsResult.create(
            status = AndroidLocationMapsStatus.AVAILABLE,
            location = location,
        )
    }
}
