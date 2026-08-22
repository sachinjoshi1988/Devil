package com.devil.app.vision

/**
 * Stage 190 bounded Camera Capability integration result.
 *
 * READY preserves the exact Stage 41 perception result, exact selected camera
 * descriptor, and one bounded Stage 41 frame request.
 *
 * FRAME_REQUEST_READY != FRAME_CAPTURED.
 * FRAME_CAPTURED != VERIFIED_OUTCOME.
 */
@ConsistentCopyVisibility
data class AndroidCameraCapabilityIntegrationResult private constructor(
    val status: AndroidCameraCapabilityIntegrationStatus,
    val perception: AndroidVisionPerceptionResult,
    val camera: AndroidCameraDescriptor?,
    val frameRequest: AndroidVisionFrameRequest?,
) {
    companion object {
        fun create(
            status: AndroidCameraCapabilityIntegrationStatus,
            perception: AndroidVisionPerceptionResult,
            camera: AndroidCameraDescriptor? = null,
            frameRequest: AndroidVisionFrameRequest? = null,
        ): AndroidCameraCapabilityIntegrationResult {
            when (status) {
                AndroidCameraCapabilityIntegrationStatus.READY -> {
                    require(perception.status == AndroidVisionPerceptionStatus.AVAILABLE) {
                        "Ready Android camera capability requires available Stage 41 perception."
                    }

                    require(camera != null) {
                        "Ready Android camera capability requires one camera descriptor."
                    }

                    require(perception.inventory.cameras.any { it == camera }) {
                        "Ready Android camera capability camera must originate from the supplied Stage 41 inventory."
                    }

                    require(frameRequest != null) {
                        "Ready Android camera capability requires one frame request."
                    }

                    require(frameRequest.cameraId == camera.cameraId) {
                        "Prepared frame request must preserve the exact selected Stage 41 camera identity."
                    }
                }

                AndroidCameraCapabilityIntegrationStatus.DEFERRED -> {
                    require(camera == null) {
                        "Deferred Android camera capability must not contain a selected camera."
                    }

                    require(frameRequest == null) {
                        "Deferred Android camera capability must not contain a frame request."
                    }
                }
            }

            return AndroidCameraCapabilityIntegrationResult(
                status = status,
                perception = perception,
                camera = camera,
                frameRequest = frameRequest,
            )
        }
    }
}
