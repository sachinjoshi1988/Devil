package com.devil.app.notification

/**
 * Receives one bounded Stage 39 Android notification perception record.
 *
 * This listener represents notification perception only.
 *
 * Implementations must not treat receipt as:
 *
 * - authentication;
 * - subject trust;
 * - conversational intent;
 * - authorization;
 * - memory commitment;
 * - execution approval;
 * - verified outcome;
 * - or task completion.
 */
fun interface AndroidNotificationPerceptionListener {

    fun onNotification(
        record: AndroidNotificationRecord,
    )
}
