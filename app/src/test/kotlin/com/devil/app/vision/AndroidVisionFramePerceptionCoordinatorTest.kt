package com.devil.app.vision

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertSame

class AndroidVisionFramePerceptionCoordinatorTest {

    @Test
    fun `coordinator preserves captured frame without interpretation`() {
        val frame =
            AndroidVisionFrame.create(
                cameraId = "back-camera",
                format = AndroidVisionFrameFormat.JPEG,
                capturedAtEpochMilliseconds = 500L,
                width = 1920,
                height = 1080,
                encodedBytes =
                    byteArrayOf(
                        10,
                        20,
                    ),
            )

        val coordinator =
            AndroidVisionFramePerceptionCoordinator(
                frameSource =
                    AndroidVisionFrameSource { request ->
                        assertEquals(
                            "back-camera",
                            request.cameraId,
                        )

                        AndroidVisionFrameCaptureResult.captured(
                            frame = frame,
                        )
                    },
            )

        val result =
            coordinator.perceive(
                request =
                    AndroidVisionFrameRequest.create(
                        cameraId = "back-camera",
                    ),
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
    fun `coordinator truthfully preserves unavailable permission`() {
        val coordinator =
            AndroidVisionFramePerceptionCoordinator(
                frameSource =
                    AndroidVisionFrameSource {
                        AndroidVisionFrameCaptureResult
                            .permissionUnavailable()
                    },
            )

        val result =
            coordinator.perceive(
                request =
                    AndroidVisionFrameRequest.create(
                        cameraId = "0",
                    ),
            )

        assertEquals(
            AndroidVisionFrameCaptureStatus.PERMISSION_UNAVAILABLE,
            result.status,
        )

        assertNull(result.frame)
    }
}
