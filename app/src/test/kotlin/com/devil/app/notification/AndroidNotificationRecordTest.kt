package com.devil.app.notification

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class AndroidNotificationRecordTest {

    @Test
    fun `record preserves bounded posted notification data`() {
        val record =
            AndroidNotificationRecord.create(
                eventType =
                    AndroidNotificationEventType.POSTED,
                packageName =
                    " com.example.messaging ",
                notificationKey =
                    " notification-key-001 ",
                postedAtEpochMilliseconds =
                    1_754_000_000_000L,
                category =
                    " msg ",
                title =
                    " Alice ",
                text =
                    " Hello ",
                subText =
                    " Messages ",
            )

        assertEquals(
            AndroidNotificationEventType.POSTED,
            record.eventType,
        )

        assertEquals(
            "com.example.messaging",
            record.packageName,
        )

        assertEquals(
            "notification-key-001",
            record.notificationKey,
        )

        assertEquals(
            1_754_000_000_000L,
            record.postedAtEpochMilliseconds,
        )

        assertEquals(
            "msg",
            record.category,
        )

        assertEquals(
            "Alice",
            record.title,
        )

        assertEquals(
            "Hello",
            record.text,
        )

        assertEquals(
            "Messages",
            record.subText,
        )
    }

    @Test
    fun `blank optional fields normalize to null`() {
        val record =
            AndroidNotificationRecord.create(
                eventType =
                    AndroidNotificationEventType.POSTED,
                packageName =
                    "com.example.messaging",
                notificationKey =
                    "notification-key-002",
                postedAtEpochMilliseconds =
                    1L,
                category = " ",
                title = " ",
                text = null,
                subText = "   ",
            )

        assertNull(record.category)
        assertNull(record.title)
        assertNull(record.text)
        assertNull(record.subText)
    }

    @Test
    fun `record rejects blank package identity`() {
        assertFailsWith<IllegalArgumentException> {
            AndroidNotificationRecord.create(
                eventType =
                    AndroidNotificationEventType.POSTED,
                packageName = " ",
                notificationKey =
                    "notification-key-003",
                postedAtEpochMilliseconds =
                    1L,
            )
        }
    }

    @Test
    fun `record rejects blank Android notification key`() {
        assertFailsWith<IllegalArgumentException> {
            AndroidNotificationRecord.create(
                eventType =
                    AndroidNotificationEventType.POSTED,
                packageName =
                    "com.example.messaging",
                notificationKey = " ",
                postedAtEpochMilliseconds =
                    1L,
            )
        }
    }
}
