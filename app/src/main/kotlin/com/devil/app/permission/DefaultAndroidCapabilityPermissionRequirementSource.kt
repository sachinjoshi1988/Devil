package com.devil.app.permission

import android.Manifest
import com.devil.app.accessibility.AndroidAccessibilityCapability
import com.devil.app.device.AndroidDeviceKnowledgeCapability
import com.devil.app.vision.AndroidVisionCapability
import com.devil.core.model.capability.CapabilityContract

/**
 * Default Android capability-permission requirement source.
 *
 * Stage 38 accessibility click does not use an Android runtime permission
 * requested through Activity permission APIs. Accessibility-service enablement
 * remains a separate Android system setting and lifecycle boundary.
 *
 * Stage 40 Device Knowledge reads only the explicitly approved non-sensitive
 * Android Build facts and requires no Android runtime permission.
 *
 * Stage 41 Vision requires Android Manifest.permission.CAMERA before any future
 * camera-open or image-capture mechanism may proceed.
 *
 * Permission requirement mapping itself does not inspect grant state.
 *
 * Android CAMERA permission granted
 * != camera hardware available
 * != camera opened
 * != image captured
 * != visual understanding
 * != owner authentication
 * != Devil authorization
 * != Execution APPROVED.
 *
 * Unknown capability mappings remain null.
 */
class DefaultAndroidCapabilityPermissionRequirementSource :
    AndroidCapabilityPermissionRequirementSource {

    override fun requiredPermissions(
        capability: CapabilityContract,
    ): List<String>? {
        return when {
            AndroidAccessibilityCapability.matches(
                capability,
            ) ->
                emptyList()

            AndroidDeviceKnowledgeCapability.matches(
                capability,
            ) ->
                emptyList()

            AndroidVisionCapability.matches(
                capability,
            ) ->
                listOf(
                    Manifest.permission.CAMERA,
                )

            else ->
                null
        }
    }
}
