package com.devil.app.notification

import android.app.Notification
import kotlin.test.Test
import kotlin.test.assertEquals

class AndroidNotificationSafetyPolicyTest {

    private val policy =
        AndroidNotificationSafetyPolicy()

    @Test
    fun `posted notification with bounded content may approach later analysis only`() {
        val record =
            AndroidNotificationRecord.create(
                eventType =
                    AndroidNotificationEventType.POSTED,
                packageName =
                    "com.example.messaging",
                notificationKey =
                    "notification-stage39-safety-001",
                postedAtEpochMilliseconds =
                    100L,
                category =
                    Notification.CATEGORY_MESSAGE,
                title =
                    "Alice",
                text =
                    "Hello",
            )

        val result =
            policy.evaluate(
                record = record,
            )

        assertEquals(
            AndroidNotificationClassification.MESSAGE,
            result.classification.classification,
        )

        assertEquals(
            AndroidNotificationSafetyDisposition
                .ELIGIBLE_FOR_LATER_ANALYSIS,
            result.disposition,
        )
    }

    @Test
    fun `posted notification without presentation content remains perception only`() {
        val record =
            AndroidNotificationRecord.create(
                eventType =
                    AndroidNotificationEventType.POSTED,
                packageName =
                    "com.example.system",
                notificationKey =
                    "notification-stage39-safety-002",
                postedAtEpochMilliseconds =
                    200L,
                category =
                    Notification.CATEGORY_SYSTEM,
            )

        val result =
            policy.evaluate(
                record = record,
            )

        assertEquals(
            AndroidNotificationSafetyDisposition
                .PERCEPTION_ONLY,
            result.disposition,
        )
    }

    @Test
    fun `removed notification remains perception only even when content exists`() {
        val record =
            AndroidNotificationRecord.create(
                eventType =
                    AndroidNotificationEventType.REMOVED,
                packageName =
                    "com.example.messaging",
                notificationKey =
                    "notification-stage39-safety-003",
                postedAtEpochMilliseconds =
                    300L,
                category =
                    Notification.CATEGORY_MESSAGE,
                title =
                    "Alice",
                text =
                    "Hello",
            )

        val result =
            policy.evaluate(
                record = record,
            )

        assertEquals(
            AndroidNotificationSafetyDisposition
                .PERCEPTION_ONLY,
            result.disposition,
        )
    }

    @Test
    fun `eligibility for later analysis does not depend on privileged classification`() {
        val record =
            AndroidNotificationRecord.create(
                eventType =
                    AndroidNotificationEventType.POSTED,
                packageName =
                    "com.example.unknown",
                notificationKey =
                    "notification-stage39-safety-004",
                postedAtEpochMilliseconds =
                    400L,
                category =
                    "unknown-category",
                text =
                    "Some content",
            )

        val result =
            policy.evaluate(
                record = record,
            )

        assertEquals(
            AndroidNotificationClassification.OTHER,
            result.classification.classification,
        )

        assertEquals(
            AndroidNotificationSafetyDisposition
                .ELIGIBLE_FOR_LATER_ANALYSIS,
            result.disposition,
        )
    }
}
