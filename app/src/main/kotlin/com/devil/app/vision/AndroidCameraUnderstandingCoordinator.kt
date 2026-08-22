package com.devil.app.vision

/**
 * Stage 207 bounded Camera Understanding coordinator.
 *
 * It binds one already-understood Stage 206 image to one explicitly supplied
 * Android camera descriptor only when the captured frame preserves that exact
 * camera identity.
 *
 * It does not:
 *
 * - capture imagery;
 * - create new image semantics;
 * - infer camera location or physical orientation from cameraId;
 * - perform face recognition;
 * - identify or authenticate a person;
 * - perform OCR or document analysis;
 * - invoke models or providers;
 * - create Memory;
 * - establish constitutional Observation, Verification, or Outcome;
 * - implement Stage 208 Document Vision.
 *
 * CAMERA_UNDERSTOOD != NEW_IMAGE_ANALYSIS.
 * CAMERA_ID != PERSON_IDENTITY.
 * CAMERA_ID != CAMERA_LOCATION.
 * CAMERA_CONTEXT != FACE_RECOGNITION.
 * CAMERA_CONTEXT != AUTHENTICATION.
 * CAMERA_CONTEXT != VERIFIED_REALITY.
 * CAMERA_UNDERSTANDING != MEMORY.
 */
class AndroidCameraUnderstandingCoordinator {

    fun integrate(
        imageUnderstanding: AndroidImageUnderstandingResult,
        camera: AndroidCameraDescriptor?,
    ): AndroidCameraUnderstandingResult {
        val frame =
            imageUnderstanding
                .visionIntegration
                .frame

        if (
            imageUnderstanding.status !=
                AndroidImageUnderstandingStatus.UNDERSTOOD ||
            camera == null ||
            frame == null ||
            frame.cameraId != camera.cameraId
        ) {
            return AndroidCameraUnderstandingResult.create(
                status = AndroidCameraUnderstandingStatus.DEFERRED,
                imageUnderstanding = imageUnderstanding,
            )
        }

        return AndroidCameraUnderstandingResult.create(
            status = AndroidCameraUnderstandingStatus.UNDERSTOOD,
            imageUnderstanding = imageUnderstanding,
            camera = camera,
        )
    }
}
