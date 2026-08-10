package com.devil.app.device

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

class Stage40DeviceKnowledgeProductionCompositionTest {

    @Test
    fun `bounded coordinator returns supplied genuine snapshot unchanged`() {
        val snapshot =
            AndroidDeviceKnowledgeSnapshot.create(
                sdkInt = 34,
                androidRelease = "14",
                manufacturer = "Example",
                model = "Device",
                device = "example_device",
                product = "example_product",
            )

        val coordinator =
            AndroidDeviceKnowledgeCoordinator(
                source =
                    AndroidDeviceKnowledgeSource {
                        snapshot
                    },
            )

        assertSame(
            snapshot,
            coordinator.snapshot(),
        )
    }

    @Test
    fun `query boundary remains typed and does not require conversation input`() {
        val snapshot =
            AndroidDeviceKnowledgeSnapshot.create(
                sdkInt = 35,
                androidRelease = "15",
                manufacturer = "Example",
                model = "Device Pro",
                device = "example_device_pro",
                product = "example_product_pro",
            )

        val coordinator =
            AndroidDeviceKnowledgeQueryCoordinator(
                source =
                    AndroidDeviceKnowledgeSource {
                        snapshot
                    },
            )

        val result =
            coordinator.query(
                AndroidDeviceKnowledgeQuery(
                    type =
                        AndroidDeviceKnowledgeQueryType.DEVICE_MODEL,
                ),
            )

        assertEquals(
            AndroidDeviceKnowledgeQueryType.DEVICE_MODEL,
            result.queryType,
        )

        assertEquals(
            "Example Device Pro.",
            result.presentation,
        )

        assertSame(
            snapshot,
            result.snapshot,
        )
    }
}
