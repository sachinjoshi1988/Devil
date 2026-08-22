package com.devil.app.vision

import com.devil.app.accessibility.AndroidScreenElementRecord
import com.devil.app.accessibility.AndroidScreenUnderstandingResult
import com.devil.app.accessibility.AndroidScreenUnderstandingStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class Stage209ScreenVisionTest {

    @Test
    fun `available screen and understood image produce available screen vision`() {
        val screenUnderstanding =
            availableScreenUnderstanding()

        val imageUnderstanding =
            understoodImage()

        val result =
            AndroidScreenVisionCoordinator()
                .integrate(
                    screenUnderstanding = screenUnderstanding,
                    imageUnderstanding = imageUnderstanding,
                )

        assertEquals(
            AndroidScreenVisionStatus.AVAILABLE,
            result.status,
        )
        assertSame(
            screenUnderstanding,
            result.screenUnderstanding,
        )
        assertSame(
            imageUnderstanding,
            result.imageUnderstanding,
        )
    }

    @Test
    fun `screen unavailable remains deferred`() {
        val screenUnderstanding =
            AndroidScreenUnderstandingResult.create(
                status =
                    AndroidScreenUnderstandingStatus.SCREEN_UNAVAILABLE,
            )

        val result =
            AndroidScreenVisionCoordinator()
                .integrate(
                    screenUnderstanding = screenUnderstanding,
                    imageUnderstanding = understoodImage(),
                )

        assertEquals(
            AndroidScreenVisionStatus.DEFERRED,
            result.status,
        )
        assertSame(
            screenUnderstanding,
            result.screenUnderstanding,
        )
    }

    @Test
    fun `accessibility service unavailable remains deferred`() {
        val screenUnderstanding =
            AndroidScreenUnderstandingResult.create(
                status =
                    AndroidScreenUnderstandingStatus.SERVICE_UNAVAILABLE,
            )

        val result =
            AndroidScreenVisionCoordinator()
                .integrate(
                    screenUnderstanding = screenUnderstanding,
                    imageUnderstanding = understoodImage(),
                )

        assertEquals(
            AndroidScreenVisionStatus.DEFERRED,
            result.status,
        )
    }

    @Test
    fun `deferred image understanding remains deferred`() {
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

        val result =
            AndroidScreenVisionCoordinator()
                .integrate(
                    screenUnderstanding =
                        availableScreenUnderstanding(),
                    imageUnderstanding = imageUnderstanding,
                )

        assertEquals(
            AndroidScreenVisionStatus.DEFERRED,
            result.status,
        )
    }

    @Test
    fun `available result requires available Stage 179 screen understanding`() {
        val screenUnderstanding =
            AndroidScreenUnderstandingResult.create(
                status =
                    AndroidScreenUnderstandingStatus.SCREEN_UNAVAILABLE,
            )

        assertFailsWith<IllegalArgumentException> {
            AndroidScreenVisionResult.create(
                status = AndroidScreenVisionStatus.AVAILABLE,
                screenUnderstanding = screenUnderstanding,
                imageUnderstanding = understoodImage(),
            )
        }
    }

    @Test
    fun `available result requires understood Stage 206 image`() {
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

        assertFailsWith<IllegalArgumentException> {
            AndroidScreenVisionResult.create(
                status = AndroidScreenVisionStatus.AVAILABLE,
                screenUnderstanding =
                    availableScreenUnderstanding(),
                imageUnderstanding = imageUnderstanding,
            )
        }
    }

    private fun availableScreenUnderstanding():
        AndroidScreenUnderstandingResult {
        return AndroidScreenUnderstandingResult.create(
            status =
                AndroidScreenUnderstandingStatus.AVAILABLE,
            elements =
                listOf(
                    AndroidScreenElementRecord.create(
                        position = 0,
                        text = "Devil",
                        contentDescription = null,
                    ),
                    AndroidScreenElementRecord.create(
                        position = 1,
                        text = null,
                        contentDescription = "Send",
                    ),
                ),
        )
    }

    private fun understoodImage(): AndroidImageUnderstandingResult {
        val frame =
            AndroidVisionFrame.create(
                cameraId = "camera:stage209",
                format = AndroidVisionFrameFormat.JPEG,
                capturedAtEpochMilliseconds = 209L,
                width = 1,
                height = 1,
                encodedBytes = byteArrayOf(2, 0, 9),
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
                description = "Bounded supplied screen-image description.",
            )
    }
}
