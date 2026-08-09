package com.devil.app.notification

import kotlin.test.Test
import kotlin.test.assertEquals

class AndroidNotificationRecordMapperTest {

    @Test
    fun `mapper preserves explicit Android notification values without interpreting them`() {
        val mapper =
            AndroidNotificationRecordMapper()

        val record =
            mapper.map(
                eventType =
                    AndroidNotificationEventType.POSTED,
                packageName =
                    "com.example.app",
                notificationKey =
                    "key-001",
                postedAtEpochMilliseconds =
                    1234L,
                category =
                    "msg",
                title =
                    StringBuilder("Payment received"),
                text =
                    StringBuilder("Rs 500"),
                subText =
                    null,
            )

        assertEquals(
            "com.example.app",
            record.packageName,
        )

        assertEquals(
            "msg",
            record.category,
        )

        assertEquals(
            "Payment received",
            record.title,
        )

        assertEquals(
            "Rs 500",
            record.text,
        )
    }

    @Test
    fun `mapper preserves removed event without creating meaning`() {
        val mapper =
            AndroidNotificationRecordMapper()

        val record =
            mapper.map(
                eventType =
                    AndroidNotificationEventType.REMOVED,
                packageName =
                    "com.example.app",
                notificationKey =
                    "key-002",
                postedAtEpochMilliseconds =
                    5678L,
                category = null,
                title = null,
                text = null,
                subText = null,
            )

        assertEquals(
            AndroidNotificationEventType.REMOVED,
            record.eventType,
        )
    }
}
