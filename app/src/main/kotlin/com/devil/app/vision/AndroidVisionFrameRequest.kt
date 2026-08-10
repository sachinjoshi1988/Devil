package com.devil.app.vision

/**
 * Explicit Stage 41 request for one bounded camera frame.
 *
 * cameraId must already come from approved Android camera inventory.
 *
 * Creating this request does not:
 *
 * - establish CAMERA permission;
 * - open a camera;
 * - grant Devil authorization;
 * - establish Executive readiness;
 * - approve constitutional Execution;
 * - interpret visual content;
 * - identify a person;
 * - or authorize persistence.
 */
@ConsistentCopyVisibility
data class AndroidVisionFrameRequest private constructor(
    val cameraId: String,
) {
    companion object {

        fun create(
            cameraId: String,
        ): AndroidVisionFrameRequest {
            val normalizedCameraId =
                cameraId.trim()

            require(normalizedCameraId.isNotEmpty()) {
                "Android vision frame request camera identity must not be blank."
            }

            return AndroidVisionFrameRequest(
                cameraId = normalizedCameraId,
            )
        }
    }
}
