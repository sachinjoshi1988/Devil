package com.devil.app.vision

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class AndroidCameraDescriptorTest {

    @Test
    fun `camera descriptor preserves normalized bounded platform metadata`() {
        val descriptor =
            AndroidCameraDescriptor.create(
                cameraId = " 0 ",
                facing = AndroidCameraFacing.BACK,
            )

        assertEquals(
            "0",
            descriptor.cameraId,
        )
        assertEquals(
            AndroidCameraFacing.BACK,
            descriptor.facing,
        )
    }

    @Test
    fun `camera descriptor rejects blank camera identity`() {
        assertFailsWith<IllegalArgumentException> {
            AndroidCameraDescriptor.create(
                cameraId = "   ",
                facing = AndroidCameraFacing.UNKNOWN,
            )
        }
    }
}
