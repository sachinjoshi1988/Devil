package com.devil.app.vision

/**
 * Immutable Stage 41 description of one Android camera exposed by CameraManager.
 *
 * cameraId is Android platform camera identity only.
 *
 * It is not:
 *
 * - a user identity;
 * - a persistent person identifier;
 * - authentication evidence;
 * - authorization;
 * - visual interpretation;
 * - or execution approval.
 */
@ConsistentCopyVisibility
data class AndroidCameraDescriptor private constructor(
    val cameraId: String,
    val facing: AndroidCameraFacing,
) {
    companion object {

        fun create(
            cameraId: String,
            facing: AndroidCameraFacing,
        ): AndroidCameraDescriptor {
            val normalizedCameraId =
                cameraId.trim()

            require(normalizedCameraId.isNotEmpty()) {
                "Android camera identity must not be blank."
            }

            return AndroidCameraDescriptor(
                cameraId = normalizedCameraId,
                facing = facing,
            )
        }
    }
}
