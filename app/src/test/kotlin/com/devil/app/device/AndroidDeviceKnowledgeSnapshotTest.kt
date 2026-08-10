package com.devil.app.device

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class AndroidDeviceKnowledgeSnapshotTest {

    @Test
    fun `snapshot preserves normalized bounded device facts`() {
        val snapshot =
            AndroidDeviceKnowledgeSnapshot.create(
                sdkInt = 35,
                androidRelease = " 14 ",
                manufacturer = " Example ",
                model = " Example Model ",
                device = " example_device ",
                product = " example_product ",
            )

        assertEquals(35, snapshot.sdkInt)
        assertEquals("14", snapshot.androidRelease)
        assertEquals("Example", snapshot.manufacturer)
        assertEquals("Example Model", snapshot.model)
        assertEquals("example_device", snapshot.device)
        assertEquals("example_product", snapshot.product)
    }

    @Test
    fun `snapshot rejects invalid sdk level`() {
        assertFailsWith<IllegalArgumentException> {
            AndroidDeviceKnowledgeSnapshot.create(
                sdkInt = 0,
                androidRelease = "14",
                manufacturer = "Example",
                model = "Example Model",
                device = "example_device",
                product = "example_product",
            )
        }
    }

    @Test
    fun `snapshot rejects blank required platform facts`() {
        assertFailsWith<IllegalArgumentException> {
            AndroidDeviceKnowledgeSnapshot.create(
                sdkInt = 35,
                androidRelease = "14",
                manufacturer = " ",
                model = "Example Model",
                device = "example_device",
                product = "example_product",
            )
        }
    }
}
