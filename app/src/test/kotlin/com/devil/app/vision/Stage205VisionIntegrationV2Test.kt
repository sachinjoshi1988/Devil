package com.devil.app.vision

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage205VisionIntegrationV2Test {

    @Test
    fun `captured Stage 41 perception becomes available and preserves exact frame`() {
        val frame = createFrame()

        val perception =
            AndroidVisionFramePerceptionResult.fromCapture(
                AndroidVisionFrameCaptureResult.captured(
                    frame = frame,
                ),
            )

        val result =
            AndroidVisionIntegrationV2Coordinator()
                .integrate(perception)

        assertEquals(
            AndroidVisionIntegrationV2Status.AVAILABLE,
            result.status,
        )
        assertSame(
            perception,
            result.perceptionResult,
        )
        assertSame(
            frame,
            result.frame,
        )
    }

    @Test
    fun `permission unavailable remains deferred`() {
        val perception =
            AndroidVisionFramePerceptionResult.fromCapture(
                AndroidVisionFrameCaptureResult.permissionUnavailable(),
            )

        val result =
            AndroidVisionIntegrationV2Coordinator()
                .integrate(perception)

        assertEquals(
            AndroidVisionIntegrationV2Status.DEFERRED,
            result.status,
        )
        assertSame(
            perception,
            result.perceptionResult,
        )
        assertNull(result.frame)
    }

    @Test
    fun `camera unavailable remains deferred`() {
        val perception =
            AndroidVisionFramePerceptionResult.fromCapture(
                AndroidVisionFrameCaptureResult.cameraUnavailable(),
            )

        val result =
            AndroidVisionIntegrationV2Coordinator()
                .integrate(perception)

        assertEquals(
            AndroidVisionIntegrationV2Status.DEFERRED,
            result.status,
        )
        assertNull(result.frame)
    }

    @Test
    fun `capture failure remains deferred`() {
        val perception =
            AndroidVisionFramePerceptionResult.fromCapture(
                AndroidVisionFrameCaptureResult.failed(),
            )

        val result =
            AndroidVisionIntegrationV2Coordinator()
                .integrate(perception)

        assertEquals(
            AndroidVisionIntegrationV2Status.DEFERRED,
            result.status,
        )
        assertNull(result.frame)
    }

    @Test
    fun `available result requires exact captured frame provenance`() {
        val capturedFrame = createFrame()
        val differentFrame =
            AndroidVisionFrame.create(
                cameraId = "camera:stage205-other",
                format = AndroidVisionFrameFormat.JPEG,
                capturedAtEpochMilliseconds = 200L,
                width = 1,
                height = 1,
                encodedBytes = byteArrayOf(9),
            )

        val perception =
            AndroidVisionFramePerceptionResult.fromCapture(
                AndroidVisionFrameCaptureResult.captured(
                    frame = capturedFrame,
                ),
            )

        assertFailsWith<IllegalArgumentException> {
            AndroidVisionIntegrationV2Result.create(
                status = AndroidVisionIntegrationV2Status.AVAILABLE,
                perceptionResult = perception,
                frame = differentFrame,
            )
        }
    }

    @Test
    fun `deferred result rejects captured perception`() {
        val frame = createFrame()

        val perception =
            AndroidVisionFramePerceptionResult.fromCapture(
                AndroidVisionFrameCaptureResult.captured(
                    frame = frame,
                ),
            )

        assertFailsWith<IllegalArgumentException> {
            AndroidVisionIntegrationV2Result.create(
                status = AndroidVisionIntegrationV2Status.DEFERRED,
                perceptionResult = perception,
            )
        }
    }

    private fun createFrame(): AndroidVisionFrame {
        return AndroidVisionFrame.create(
            cameraId = "camera:stage205",
            format = AndroidVisionFrameFormat.JPEG,
            capturedAtEpochMilliseconds = 100L,
            width = 1,
            height = 1,
            encodedBytes = byteArrayOf(1, 2, 3),
        )
    }
}
