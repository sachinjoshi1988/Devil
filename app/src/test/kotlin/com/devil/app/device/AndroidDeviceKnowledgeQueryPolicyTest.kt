package com.devil.app.device

import kotlin.test.Test
import kotlin.test.assertEquals

class AndroidDeviceKnowledgeQueryPolicyTest {

    private val snapshot =
        AndroidDeviceKnowledgeSnapshot.create(
            sdkInt = 35,
            androidRelease = "15",
            manufacturer = "Example",
            model = "Device X",
            device = "device_x",
            product = "device_x_product",
        )

    @Test
    fun `device summary uses only approved snapshot facts`() {
        val result =
            AndroidDeviceKnowledgeQueryPolicy()
                .evaluate(
                    query =
                        AndroidDeviceKnowledgeQuery(
                            type =
                                AndroidDeviceKnowledgeQueryType.DEVICE_SUMMARY,
                        ),
                    snapshot = snapshot,
                )

        assertEquals(
            AndroidDeviceKnowledgeQueryType.DEVICE_SUMMARY,
            result.queryType,
        )

        assertEquals(
            snapshot,
            result.snapshot,
        )

        assertEquals(
            "Example Device X, Android 15 (SDK 35).",
            result.presentation,
        )
    }

    @Test
    fun `android version query preserves bounded version facts`() {
        val result =
            AndroidDeviceKnowledgeQueryPolicy()
                .evaluate(
                    query =
                        AndroidDeviceKnowledgeQuery(
                            type =
                                AndroidDeviceKnowledgeQueryType.ANDROID_VERSION,
                        ),
                    snapshot = snapshot,
                )

        assertEquals(
            "Android 15 (SDK 35).",
            result.presentation,
        )
    }

    @Test
    fun `device model query preserves bounded manufacturer and model`() {
        val result =
            AndroidDeviceKnowledgeQueryPolicy()
                .evaluate(
                    query =
                        AndroidDeviceKnowledgeQuery(
                            type =
                                AndroidDeviceKnowledgeQueryType.DEVICE_MODEL,
                        ),
                    snapshot = snapshot,
                )

        assertEquals(
            "Example Device X.",
            result.presentation,
        )
    }
}
