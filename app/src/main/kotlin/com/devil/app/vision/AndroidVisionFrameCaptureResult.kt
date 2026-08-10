package com.devil.app.vision

/**
 * Stage 41 result of one bounded request for a visual frame.
 *
 * Only CAPTURED may contain frame data.
 *
 * This result does not interpret pixels, identify people, authenticate anyone,
 * create memory, grant authorization, or establish an Outcome.
 */
@ConsistentCopyVisibility
data class AndroidVisionFrameCaptureResult private constructor(
    val status: AndroidVisionFrameCaptureStatus,
    val frame: AndroidVisionFrame?,
) {
    companion object {

        fun captured(
            frame: AndroidVisionFrame,
        ): AndroidVisionFrameCaptureResult {
            return AndroidVisionFrameCaptureResult(
                status =
                    AndroidVisionFrameCaptureStatus.CAPTURED,
                frame = frame,
            )
        }

        fun cameraUnavailable(): AndroidVisionFrameCaptureResult {
            return AndroidVisionFrameCaptureResult(
                status =
                    AndroidVisionFrameCaptureStatus.CAMERA_UNAVAILABLE,
                frame = null,
            )
        }

        fun permissionUnavailable(): AndroidVisionFrameCaptureResult {
            return AndroidVisionFrameCaptureResult(
                status =
                    AndroidVisionFrameCaptureStatus.PERMISSION_UNAVAILABLE,
                frame = null,
            )
        }

        fun failed(): AndroidVisionFrameCaptureResult {
            return AndroidVisionFrameCaptureResult(
                status =
                    AndroidVisionFrameCaptureStatus.FAILED,
                frame = null,
            )
        }
    }

    init {
        when (status) {
            AndroidVisionFrameCaptureStatus.CAPTURED ->
                requireNotNull(frame) {
                    "Captured Android vision result must contain a frame."
                }

            AndroidVisionFrameCaptureStatus.CAMERA_UNAVAILABLE,
            AndroidVisionFrameCaptureStatus.PERMISSION_UNAVAILABLE,
            AndroidVisionFrameCaptureStatus.FAILED,
            ->
                require(frame == null) {
                    "Non-captured Android vision result must not contain a frame."
                }
        }
    }
}
