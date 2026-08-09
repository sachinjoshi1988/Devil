package com.devil.app.notification

/**
 * Process-local Stage 39 connection registry for DevilNotificationListenerService.
 *
 * Registration means only that Android currently has Devil's notification
 * listener service connected.
 *
 * Registered notification listener
 * != notification access authorized by Devil.
 *
 * Registered notification listener
 * != sender authentication.
 *
 * Registered notification listener
 * != capability availability.
 *
 * Registered notification listener
 * != execution approval.
 */
object DevilNotificationListenerServiceRegistry {

    private var connectedService:
        DevilNotificationListenerService? = null

    @Synchronized
    fun register(
        service: DevilNotificationListenerService,
    ) {
        connectedService = service
    }

    @Synchronized
    fun unregister(
        service: DevilNotificationListenerService,
    ) {
        if (connectedService === service) {
            connectedService = null
        }
    }

    @Synchronized
    fun current():
        DevilNotificationListenerService? {
        return connectedService
    }
}
