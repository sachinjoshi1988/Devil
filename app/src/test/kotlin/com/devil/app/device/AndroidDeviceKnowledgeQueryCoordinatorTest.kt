package com.devil.app.device

import kotlin.test.Test
import kotlin.test.assertEquals

class AndroidDeviceKnowledgeQueryCoordinatorTest {

    @Test
    fun `coordinator obtains one genuine source snapshot and evaluates explicit query`() {
        val snapshot =
            AndroidDeviceKnowledgeSnapshot.create(
                sdkInt = 34,
                androidRelease = "14",
                manufacturer = "Example",
                model = "Phone",
                device = "example_phone",
                product = "example_product",
            )

        var sourceCalls = 0

        val coordinator =
            AndroidDeviceKnowledgeQueryCoordinator(
                source =
                    AndroidDeviceKnowledgeSource {
                        sourceCalls += 1
                        snapshot
                    },
            )

        val result =
            coordinator.query(
                AndroidDeviceKnowledgeQuery(
                    type =
                        AndroidDeviceKnowledgeQueryType.DEVICE_SUMMARY,
                ),
            )

        assertEquals(
            1,
            sourceCalls,
        )

        assertEquals(
            snapshot,
            result.snapshot,
        )

        assertEquals(
            "Example Phone, Android 14 (SDK 34).",
            result.presentation,
        )
    }
}
