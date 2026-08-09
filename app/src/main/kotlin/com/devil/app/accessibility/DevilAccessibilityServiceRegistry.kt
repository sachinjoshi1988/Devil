package com.devil.app.accessibility

/**
 * Process-local Stage 38 connection registry for DevilAccessibilityService.
 *
 * Registration means only that Android currently has a Devil accessibility
 * service instance connected.
 *
 * Registered accessibility service != capability availability.
 * Registered accessibility service != authorization.
 * Registered accessibility service != execution approval.
 */
object DevilAccessibilityServiceRegistry {

    private var connectedService:
        DevilAccessibilityService? = null

    @Synchronized
    fun register(
        service: DevilAccessibilityService,
    ) {
        connectedService = service
    }

    @Synchronized
    fun unregister(
        service: DevilAccessibilityService,
    ) {
        if (connectedService === service) {
            connectedService = null
        }
    }

    @Synchronized
    fun current():
        DevilAccessibilityService? {
        return connectedService
    }
}
