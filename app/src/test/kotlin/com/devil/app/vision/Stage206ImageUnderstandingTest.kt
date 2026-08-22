package com.devil.app.vision

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage206ImageUnderstandingTest {

    @Test
    fun `available Stage 205 image accepts bounded description and preserves provenance`() {
        val integration =
            availableVisionIntegration()

        val result =
            AndroidImageUnderstandingCoordinator()
                .understand(
                    visionIntegration = integration,
                    description = "  A bounded supplied visual description.  ",
                )

        assertEquals(
            AndroidImageUnderstandingStatus.UNDERSTOOD,
            result.status,
        )
        assertSame(
            integration,
            result.visionIntegration,
        )
        assertEquals(
            "A bounded supplied visual description.",
            result.description,
        )
    }

    @Test
    fun `blank description remains deferred`() {
        val integration =
            availableVisionIntegration()

        val result =
            AndroidImageUnderstandingCoordinator()
                .understand(
                    visionIntegration = integration,
                    description = "   ",
                )

        assertEquals(
            AndroidImageUnderstandingStatus.DEFERRED,
            result.status,
        )
        assertSame(
            integration,
            result.visionIntegration,
        )
        assertNull(result.description)
    }

    @Test
    fun `missing description remains deferred`() {
        val integration =
            availableVisionIntegration()

        val result =
            AndroidImageUnderstandingCoordinator()
                .understand(
                    visionIntegration = integration,
                    description = null,
                )

        assertEquals(
            AndroidImageUnderstandingStatus.DEFERRED,
            result.status,
        )
        assertNull(result.description)
    }

    @Test
    fun `deferred Stage 205 integration remains deferred`() {
        val perception =
            AndroidVisionFramePerceptionResult.fromCapture(
                AndroidVisionFrameCaptureResult.permissionUnavailable(),
            )

        val integration =
            AndroidVisionIntegrationV2Coordinator()
                .integrate(perception)

        val result =
            AndroidImageUnderstandingCoordinator()
                .understand(
                    visionIntegration = integration,
                    description = "Supplied visual description.",
                )

        assertEquals(
            AndroidImageUnderstandingStatus.DEFERRED,
            result.status,
        )
        assertSame(
            integration,
            result.visionIntegration,
        )
        assertNull(result.description)
    }

    @Test
    fun `understood result requires available Stage 205 integration`() {
        val perception =
            AndroidVisionFramePerceptionResult.fromCapture(
                AndroidVisionFrameCaptureResult.failed(),
            )

        val integration =
            AndroidVisionIntegrationV2Coordinator()
                .integrate(perception)

        assertFailsWith<IllegalArgumentException> {
            AndroidImageUnderstandingResult.create(
                status = AndroidImageUnderstandingStatus.UNDERSTOOD,
                visionIntegration = integration,
                description = "Description",
            )
        }
    }

    @Test
    fun `understood result rejects blank description`() {
        assertFailsWith<IllegalArgumentException> {
            AndroidImageUnderstandingResult.create(
                status = AndroidImageUnderstandingStatus.UNDERSTOOD,
                visionIntegration = availableVisionIntegration(),
                description = "   ",
            )
        }
    }

    private fun availableVisionIntegration(): AndroidVisionIntegrationV2Result {
        val frame =
            AndroidVisionFrame.create(
                cameraId = "camera:stage206",
                format = AndroidVisionFrameFormat.JPEG,
                capturedAtEpochMilliseconds = 206L,
                width = 1,
                height = 1,
                encodedBytes = byteArrayOf(2, 0, 6),
            )

        val perception =
            AndroidVisionFramePerceptionResult.fromCapture(
                AndroidVisionFrameCaptureResult.captured(
                    frame = frame,
                ),
            )

        return AndroidVisionIntegrationV2Coordinator()
            .integrate(perception)
    }
}
