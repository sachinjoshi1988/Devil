package com.devil.app.vision

import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class AndroidVisionFrameTest {

    @Test
    fun `frame preserves validated bounded capture data`() {
        val sourceBytes =
            byteArrayOf(
                1,
                2,
                3,
                4,
            )

        val frame =
            AndroidVisionFrame.create(
                cameraId = " 0 ",
                format = AndroidVisionFrameFormat.JPEG,
                capturedAtEpochMilliseconds = 1234L,
                width = 640,
                height = 480,
                encodedBytes = sourceBytes,
            )

        sourceBytes[0] = 99

        assertEquals(
            "0",
            frame.cameraId,
        )

        assertEquals(
            AndroidVisionFrameFormat.JPEG,
            frame.format,
        )

        assertEquals(
            4,
            frame.byteCount,
        )

        assertContentEquals(
            byteArrayOf(
                1,
                2,
                3,
                4,
            ),
            frame.encodedBytesCopy(),
        )
    }

    @Test
    fun `frame rejects empty visual payload`() {
        assertFailsWith<IllegalArgumentException> {
            AndroidVisionFrame.create(
                cameraId = "0",
                format = AndroidVisionFrameFormat.JPEG,
                capturedAtEpochMilliseconds = 1L,
                width = 640,
                height = 480,
                encodedBytes = byteArrayOf(),
            )
        }
    }
}
