package com.devil.app.vision

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage207CameraUnderstandingTest {

    @Test
    fun `matching camera identity produces understood camera context`() {
        val imageUnderstanding =
            understoodImage(
                cameraId = "back-camera",
            )

        val camera =
            AndroidCameraDescriptor.create(
                cameraId = "back-camera",
                facing = AndroidCameraFacing.BACK,
            )

        val result =
            AndroidCameraUnderstandingCoordinator()
                .integrate(
                    imageUnderstanding = imageUnderstanding,
                    camera = camera,
                )

        assertEquals(
            AndroidCameraUnderstandingStatus.UNDERSTOOD,
            result.status,
        )
        assertSame(
            imageUnderstanding,
            result.imageUnderstanding,
        )
        assertSame(
            camera,
            result.camera,
        )
    }

    @Test
    fun `different camera identity remains deferred`() {
        val imageUnderstanding =
            understoodImage(
                cameraId = "back-camera",
            )

        val camera =
            AndroidCameraDescriptor.create(
                cameraId = "front-camera",
                facing = AndroidCameraFacing.FRONT,
            )

        val result =
            AndroidCameraUnderstandingCoordinator()
                .integrate(
                    imageUnderstanding = imageUnderstanding,
                    camera = camera,
                )

        assertEquals(
            AndroidCameraUnderstandingStatus.DEFERRED,
            result.status,
        )
        assertSame(
            imageUnderstanding,
            result.imageUnderstanding,
        )
        assertNull(result.camera)
    }

    @Test
    fun `missing camera descriptor remains deferred`() {
        val imageUnderstanding =
            understoodImage(
                cameraId = "camera:stage207",
            )

        val result =
            AndroidCameraUnderstandingCoordinator()
                .integrate(
                    imageUnderstanding = imageUnderstanding,
                    camera = null,
                )

        assertEquals(
            AndroidCameraUnderstandingStatus.DEFERRED,
            result.status,
        )
        assertNull(result.camera)
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
                    description = "Supplied description",
                )

        val camera =
            AndroidCameraDescriptor.create(
                cameraId = "camera:stage207",
                facing = AndroidCameraFacing.UNKNOWN,
            )

        val result =
            AndroidCameraUnderstandingCoordinator()
                .integrate(
                    imageUnderstanding = imageUnderstanding,
                    camera = camera,
                )

        assertEquals(
            AndroidCameraUnderstandingStatus.DEFERRED,
            result.status,
        )
        assertNull(result.camera)
    }

    @Test
    fun `understood result rejects mismatched camera identity`() {
        val imageUnderstanding =
            understoodImage(
                cameraId = "camera-a",
            )

        val camera =
            AndroidCameraDescriptor.create(
                cameraId = "camera-b",
                facing = AndroidCameraFacing.UNKNOWN,
            )

        assertFailsWith<IllegalArgumentException> {
            AndroidCameraUnderstandingResult.create(
                status = AndroidCameraUnderstandingStatus.UNDERSTOOD,
                imageUnderstanding = imageUnderstanding,
                camera = camera,
            )
        }
    }

    @Test
    fun `deferred result rejects camera descriptor`() {
        val imageUnderstanding =
            understoodImage(
                cameraId = "camera:stage207",
            )

        val camera =
            AndroidCameraDescriptor.create(
                cameraId = "camera:stage207",
                facing = AndroidCameraFacing.UNKNOWN,
            )

        assertFailsWith<IllegalArgumentException> {
            AndroidCameraUnderstandingResult.create(
                status = AndroidCameraUnderstandingStatus.DEFERRED,
                imageUnderstanding = imageUnderstanding,
                camera = camera,
            )
        }
    }

    private fun understoodImage(
        cameraId: String,
    ): AndroidImageUnderstandingResult {
        val frame =
            AndroidVisionFrame.create(
                cameraId = cameraId,
                format = AndroidVisionFrameFormat.JPEG,
                capturedAtEpochMilliseconds = 207L,
                width = 1,
                height = 1,
                encodedBytes = byteArrayOf(2, 0, 7),
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
