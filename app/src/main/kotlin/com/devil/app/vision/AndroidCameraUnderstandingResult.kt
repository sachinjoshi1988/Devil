package com.devil.app.vision

/**
 * Stage 207 bounded Camera Understanding result.
 *
 * UNDERSTOOD preserves the exact Stage 206 image-understanding result and the
 * exact explicitly supplied Android camera descriptor whose camera identity
 * matches the captured frame provenance.
 *
 * DEFERRED preserves the exact Stage 206 result and no camera descriptor.
 *
 * CAMERA_UNDERSTOOD != NEW_IMAGE_ANALYSIS.
 * CAMERA_ID != PERSON_IDENTITY.
 * CAMERA_ID != CAMERA_LOCATION.
 * CAMERA_CONTEXT != FACE_RECOGNITION.
 * CAMERA_CONTEXT != AUTHENTICATION.
 * CAMERA_CONTEXT != VERIFIED_REALITY.
 * CAMERA_UNDERSTANDING != MEMORY.
 */
@ConsistentCopyVisibility
data class AndroidCameraUnderstandingResult private constructor(
    val status: AndroidCameraUnderstandingStatus,
    val imageUnderstanding: AndroidImageUnderstandingResult,
    val camera: AndroidCameraDescriptor?,
) {
    companion object {
        fun create(
            status: AndroidCameraUnderstandingStatus,
            imageUnderstanding: AndroidImageUnderstandingResult,
            camera: AndroidCameraDescriptor? = null,
        ): AndroidCameraUnderstandingResult {
            when (status) {
                AndroidCameraUnderstandingStatus.UNDERSTOOD -> {
                    require(
                        imageUnderstanding.status ==
                            AndroidImageUnderstandingStatus.UNDERSTOOD,
                    ) {
                        "Understood Stage 207 camera context requires understood Stage 206 image understanding."
                    }

                    val suppliedCamera =
                        requireNotNull(camera) {
                            "Understood Stage 207 camera context requires one camera descriptor."
                        }

                    val frame =
                        requireNotNull(
                            imageUnderstanding
                                .visionIntegration
                                .frame,
                        ) {
                            "Understood Stage 207 camera context requires captured frame provenance."
                        }

                    require(frame.cameraId == suppliedCamera.cameraId) {
                        "Stage 207 camera descriptor must match the exact captured frame camera identity."
                    }
                }

                AndroidCameraUnderstandingStatus.DEFERRED -> {
                    require(camera == null) {
                        "Deferred Stage 207 camera understanding must not contain a camera descriptor."
                    }
                }
            }

            return AndroidCameraUnderstandingResult(
                status = status,
                imageUnderstanding = imageUnderstanding,
                camera = camera,
            )
        }
    }
}
