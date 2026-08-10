package com.devil.app.vision

import android.content.Context
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager

/**
 * Default Stage 41 Android camera-inventory source.
 *
 * The source performs read-only CameraManager metadata inspection.
 *
 * It does not open any camera and does not capture image data.
 *
 * CameraManager metadata
 * != CAMERA permission grant
 * != active visual perception
 * != trusted visual content
 * != Devil authorization.
 */
class DefaultAndroidCameraInventorySource(
    context: Context,
) : AndroidCameraInventorySource {

    private val cameraManager =
        context.applicationContext.getSystemService(
            CameraManager::class.java,
        )

    override fun inventory(): AndroidCameraInventory {
        val cameras =
            cameraManager
                ?.cameraIdList
                ?.map { cameraId ->
                    val characteristics =
                        cameraManager.getCameraCharacteristics(
                            cameraId,
                        )

                    AndroidCameraDescriptor.create(
                        cameraId = cameraId,
                        facing =
                            mapFacing(
                                characteristics.get(
                                    CameraCharacteristics.LENS_FACING,
                                ),
                            ),
                    )
                }
                .orEmpty()

        return AndroidCameraInventory(
            cameras = cameras,
        )
    }

    private fun mapFacing(
        lensFacing: Int?,
    ): AndroidCameraFacing {
        return when (lensFacing) {
            CameraCharacteristics.LENS_FACING_FRONT ->
                AndroidCameraFacing.FRONT

            CameraCharacteristics.LENS_FACING_BACK ->
                AndroidCameraFacing.BACK

            CameraCharacteristics.LENS_FACING_EXTERNAL ->
                AndroidCameraFacing.EXTERNAL

            else ->
                AndroidCameraFacing.UNKNOWN
        }
    }
}
