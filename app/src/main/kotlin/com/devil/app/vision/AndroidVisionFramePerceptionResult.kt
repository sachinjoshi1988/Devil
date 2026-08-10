package com.devil.app.vision

/**
 * Stage 41 bounded frame-perception result.
 *
 * PERCEIVED means only that one genuine captured frame reached the bounded
 * visual perception boundary.
 *
 * No semantic visual understanding is established here.
 */
@ConsistentCopyVisibility
data class AndroidVisionFramePerceptionResult private constructor(
    val status: AndroidVisionFrameCaptureStatus,
    val frame: AndroidVisionFrame?,
) {
    companion object {

        fun fromCapture(
            captureResult: AndroidVisionFrameCaptureResult,
        ): AndroidVisionFramePerceptionResult {
            return AndroidVisionFramePerceptionResult(
                status = captureResult.status,
                frame = captureResult.frame,
            )
        }
    }
}
