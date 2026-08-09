package com.devil.app.notification

import android.app.Notification
import kotlin.test.Test
import kotlin.test.assertEquals

class AndroidNotificationClassificationPolicyTest {

    private val policy =
        AndroidNotificationClassificationPolicy()

    @Test
    fun `message category maps only to descriptive message classification`() {
        val result =
            policy.classify(
                Notification.CATEGORY_MESSAGE,
            )

        assertEquals(
            AndroidNotificationClassification.MESSAGE,
            result.classification,
        )

        assertEquals(
            Notification.CATEGORY_MESSAGE,
            result.rawCategory,
        )
    }

    @Test
    fun `call category maps only to descriptive call classification`() {
        val result =
            policy.classify(
                Notification.CATEGORY_CALL,
            )

        assertEquals(
            AndroidNotificationClassification.CALL,
            result.classification,
        )
    }

    @Test
    fun `security extended category remains descriptive only`() {
        val result =
            policy.classify(
                " SECURITY ",
            )

        assertEquals(
            AndroidNotificationClassification.SECURITY,
            result.classification,
        )

        assertEquals(
            "security",
            result.rawCategory,
        )
    }

    @Test
    fun `unknown explicit category is not guessed`() {
        val result =
            policy.classify(
                "custom-vendor-category",
            )

        assertEquals(
            AndroidNotificationClassification.OTHER,
            result.classification,
        )
    }

    @Test
    fun `missing category remains unknown`() {
        val result =
            policy.classify(
                null,
            )

        assertEquals(
            AndroidNotificationClassification.UNKNOWN,
            result.classification,
        )
    }
}
