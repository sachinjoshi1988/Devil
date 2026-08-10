package com.devil.app.device

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

class AndroidDeviceKnowledgeCoordinatorTest {

    @Test
    fun `coordinator returns supplied device snapshot unchanged`() {
        val expected =
            AndroidDeviceKnowledgeSnapshot.create(
                sdkInt = 35,
                androidRelease = "14",
                manufacturer = "Example",
                model = "Example Model",
                device = "example_device",
                product = "example_product",
            )

        val source =
            AndroidDeviceKnowledgeSource {
                expected
            }

        val coordinator =
            AndroidDeviceKnowledgeCoordinator(
                source = source,
            )

        val actual =
            coordinator.snapshot()

        assertSame(expected, actual)
        assertEquals(expected, actual)
    }
}
