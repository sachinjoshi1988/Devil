package com.devil.app.vision

/**
 * Stage 190 bounded Camera Capability integration coordinator.
 *
 * It consumes exact Stage 41 camera-perception provenance and one explicitly
 * supplied Stage 41 camera descriptor.
 *
 * It does not grant CAMERA permission, open a camera, capture a frame,
 * interpret imagery, establish identity/authentication, grant authorization,
 * establish execution, Verification or Outcome, or implement Stage 191.
 *
 * CAMERA_AVAILABLE != CAMERA_AUTHORIZED.
 * FRAME_REQUEST_READY != FRAME_CAPTURED.
 */
class AndroidCameraCapabilityIntegrationCoordinator {
    fun prepare(
        perception: AndroidVisionPerceptionResult,
        camera: AndroidCameraDescriptor?,
    ): AndroidCameraCapabilityIntegrationResult {
        if (
            perception.status != AndroidVisionPerceptionStatus.AVAILABLE ||
            camera == null ||
            perception.inventory.cameras.none { it == camera }
        ) {
            return AndroidCameraCapabilityIntegrationResult.create(
                status = AndroidCameraCapabilityIntegrationStatus.DEFERRED,
                perception = perception,
            )
        }

        return AndroidCameraCapabilityIntegrationResult.create(
            status = AndroidCameraCapabilityIntegrationStatus.READY,
            perception = perception,
            camera = camera,
            frameRequest =
                AndroidVisionFrameRequest.create(
                    cameraId = camera.cameraId,
                ),
        )
    }
}
