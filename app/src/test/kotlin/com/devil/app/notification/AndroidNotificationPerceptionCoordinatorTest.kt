package com.devil.app.notification

import kotlin.test.Test
import kotlin.test.assertEquals

class AndroidNotificationPerceptionCoordinatorTest {

    @Test
    fun `coordinator forwards exactly one bounded notification record`() {
        val observed =
            mutableListOf<AndroidNotificationRecord>()

        val coordinator =
            AndroidNotificationPerceptionCoordinator(
                listener =
                    AndroidNotificationPerceptionListener {
                        record ->
                        observed += record
                    },
            )

        val record =
            AndroidNotificationRecord.create(
                eventType =
                    AndroidNotificationEventType.POSTED,
                packageName =
                    "com.example.app",
                notificationKey =
                    "notification-key-stage-39",
                postedAtEpochMilliseconds =
                    100L,
                title =
                    "Example",
                text =
                    "Notification",
            )

        coordinator.accept(
            record = record,
        )

        assertEquals(
            listOf(record),
            observed,
        )
    }
}
