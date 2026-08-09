package com.devil.app.notification

import android.app.Notification
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import com.devil.app.DevilApplication

/**
 * Stage 39 Android NotificationListenerService embodiment.
 *
 * This Android service is intentionally thin.
 *
 * Production path:
 *
 * Android NotificationListenerService callback
 * -> bounded primitive extraction
 * -> AndroidNotificationRecordMapper
 * -> process-scoped AndroidNotificationPerceptionCoordinator
 * -> bounded safety/classification/analysis.
 *
 * The process-scoped coordinator is owned by DevilApplication.
 *
 * This service does not create another Devil intelligence, Brain, planner,
 * conversation engine, authorization authority, execution authority,
 * observation authority, verification authority, outcome authority, or
 * memory authority.
 *
 * Notification-listener access
 * != Devil authorization.
 *
 * Notification posted
 * != authenticated sender.
 *
 * Notification content
 * != trusted statement.
 *
 * Notification content
 * != Devil command.
 *
 * Notification received
 * != ConversationInput.
 *
 * Notification analyzed
 * != permission to interrupt.
 *
 * Notification analyzed
 * != permission to speak.
 *
 * Notification analyzed
 * != permission to persist.
 *
 * Notification analyzed
 * != execution approval.
 */
class DevilNotificationListenerService :
    NotificationListenerService() {

    private val mapper =
        AndroidNotificationRecordMapper()

    private val perceptionCoordinator:
        AndroidNotificationPerceptionCoordinator
        get() =
            (application as DevilApplication)
                .notificationPerceptionCoordinator

    override fun onListenerConnected() {
        super.onListenerConnected()

        DevilNotificationListenerServiceRegistry.register(
            service = this,
        )
    }

    override fun onNotificationPosted(
        sbn: StatusBarNotification?,
    ) {
        val notification =
            sbn
                ?: return

        publish(
            eventType =
                AndroidNotificationEventType.POSTED,
            notification = notification,
        )
    }

    override fun onNotificationRemoved(
        sbn: StatusBarNotification?,
    ) {
        val notification =
            sbn
                ?: return

        publish(
            eventType =
                AndroidNotificationEventType.REMOVED,
            notification = notification,
        )
    }

    override fun onListenerDisconnected() {
        DevilNotificationListenerServiceRegistry.unregister(
            service = this,
        )

        super.onListenerDisconnected()
    }

    override fun onDestroy() {
        DevilNotificationListenerServiceRegistry.unregister(
            service = this,
        )

        super.onDestroy()
    }

    private fun publish(
        eventType: AndroidNotificationEventType,
        notification: StatusBarNotification,
    ) {
        val platformNotification =
            notification.notification

        val extras =
            platformNotification.extras

        val record =
            mapper.map(
                eventType = eventType,
                packageName =
                    notification.packageName,
                notificationKey =
                    notification.key,
                postedAtEpochMilliseconds =
                    notification.postTime,
                category =
                    platformNotification.category,
                title =
                    extras?.getCharSequence(
                        Notification.EXTRA_TITLE,
                    ),
                text =
                    extras?.getCharSequence(
                        Notification.EXTRA_TEXT,
                    ),
                subText =
                    extras?.getCharSequence(
                        Notification.EXTRA_SUB_TEXT,
                    ),
            )

        perceptionCoordinator.accept(
            record = record,
        )
    }
}
