package com.devil.app.vision

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertSame

class AndroidVisionFrameCaptureResultTest {

    @Test
    fun `captured result preserves genuine frame`() {
        val frame =
            createFrame()

        val result =
            AndroidVisionFrameCaptureResult.captured(
                frame = frame,
            )

        assertEquals(
            AndroidVisionFrameCaptureStatus.CAPTURED,
            result.status,
        )

        assertSame(
            frame,
            result.frame,
        )
    }

    @Test
    fun `permission unavailable contains no frame`() {
        val result =
            AndroidVisionFrameCaptureResult
                .permissionUnavailable()

        assertEquals(
            AndroidVisionFrameCaptureStatus.PERMISSION_UNAVAILABLE,
            result.status,
        )

        assertNull(result.frame)
    }

    private fun createFrame(): AndroidVisionFrame {
        return AndroidVisionFrame.create(
            cameraId = "0",
            format = AndroidVisionFrameFormat.JPEG,
            capturedAtEpochMilliseconds = 10L,
            width = 320,
            height = 240,
            encodedBytes =
                byteArrayOf(
                    1,
                ),
        )
    }
}
