package com.devil.app.vision

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage208DocumentVisionTest {

    @Test
    fun `understood image accepts bounded document description and preserves provenance`() {
        val imageUnderstanding =
            understoodImage()

        val result =
            AndroidDocumentVisionCoordinator()
                .understand(
                    imageUnderstanding = imageUnderstanding,
                    documentDescription =
                        "  Supplied bounded document description.  ",
                )

        assertEquals(
            AndroidDocumentVisionStatus.UNDERSTOOD,
            result.status,
        )
        assertSame(
            imageUnderstanding,
            result.imageUnderstanding,
        )
        assertEquals(
            "Supplied bounded document description.",
            result.documentDescription,
        )
    }

    @Test
    fun `blank document description remains deferred`() {
        val imageUnderstanding =
            understoodImage()

        val result =
            AndroidDocumentVisionCoordinator()
                .understand(
                    imageUnderstanding = imageUnderstanding,
                    documentDescription = "   ",
                )

        assertEquals(
            AndroidDocumentVisionStatus.DEFERRED,
            result.status,
        )
        assertSame(
            imageUnderstanding,
            result.imageUnderstanding,
        )
        assertNull(result.documentDescription)
    }

    @Test
    fun `missing document description remains deferred`() {
        val imageUnderstanding =
            understoodImage()

        val result =
            AndroidDocumentVisionCoordinator()
                .understand(
                    imageUnderstanding = imageUnderstanding,
                    documentDescription = null,
                )

        assertEquals(
            AndroidDocumentVisionStatus.DEFERRED,
            result.status,
        )
        assertNull(result.documentDescription)
    }

    @Test
    fun `deferred image understanding remains deferred`() {
        val perception =
            AndroidVisionFramePerceptionResult.fromCapture(
                AndroidVisionFrameCaptureResult.permissionUnavailable(),
            )

        val integration =
            AndroidVisionIntegrationV2Coordinator()
                .integrate(perception)

        val imageUnderstanding =
            AndroidImageUnderstandingCoordinator()
                .understand(
                    visionIntegration = integration,
                    description = "Supplied image description.",
                )

        val result =
            AndroidDocumentVisionCoordinator()
                .understand(
                    imageUnderstanding = imageUnderstanding,
                    documentDescription =
                        "Supplied document description.",
                )

        assertEquals(
            AndroidDocumentVisionStatus.DEFERRED,
            result.status,
        )
        assertSame(
            imageUnderstanding,
            result.imageUnderstanding,
        )
        assertNull(result.documentDescription)
    }

    @Test
    fun `understood result requires understood Stage 206 provenance`() {
        val perception =
            AndroidVisionFramePerceptionResult.fromCapture(
                AndroidVisionFrameCaptureResult.failed(),
            )

        val integration =
            AndroidVisionIntegrationV2Coordinator()
                .integrate(perception)

        val imageUnderstanding =
            AndroidImageUnderstandingCoordinator()
                .understand(
                    visionIntegration = integration,
                    description = "Supplied image description.",
                )

        assertFailsWith<IllegalArgumentException> {
            AndroidDocumentVisionResult.create(
                status = AndroidDocumentVisionStatus.UNDERSTOOD,
                imageUnderstanding = imageUnderstanding,
                documentDescription = "Document description",
            )
        }
    }

    @Test
    fun `understood result rejects blank document description`() {
        assertFailsWith<IllegalArgumentException> {
            AndroidDocumentVisionResult.create(
                status = AndroidDocumentVisionStatus.UNDERSTOOD,
                imageUnderstanding = understoodImage(),
                documentDescription = "   ",
            )
        }
    }

    private fun understoodImage(): AndroidImageUnderstandingResult {
        val frame =
            AndroidVisionFrame.create(
                cameraId = "camera:stage208",
                format = AndroidVisionFrameFormat.JPEG,
                capturedAtEpochMilliseconds = 208L,
                width = 1,
                height = 1,
                encodedBytes = byteArrayOf(2, 0, 8),
            )

        val perception =
            AndroidVisionFramePerceptionResult.fromCapture(
                AndroidVisionFrameCaptureResult.captured(
                    frame = frame,
                ),
            )

        val integration =
            AndroidVisionIntegrationV2Coordinator()
                .integrate(perception)

        return AndroidImageUnderstandingCoordinator()
            .understand(
                visionIntegration = integration,
                description = "Bounded supplied image description.",
            )
    }
}
