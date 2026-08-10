package com.devil.app.permission

import android.Manifest
import com.devil.app.accessibility.AndroidAccessibilityCapability
import com.devil.app.device.AndroidDeviceKnowledgeCapability
import com.devil.app.internet.AndroidInternetKnowledgeCapability
import com.devil.app.vision.AndroidVisionCapability
import com.devil.core.model.capability.CapabilityContract

/**
 * Default Android capability-permission requirement source.
 *
 * Stage 38 accessibility click does not use an Android runtime permission
 * requested through Activity permission APIs.
 *
 * Stage 40 Device Knowledge requires no Android runtime permission.
 *
 * Stage 41 Vision requires Manifest.permission.CAMERA.
 *
 * Stage 42 Internet Knowledge requires Manifest.permission.INTERNET.
 *
 * INTERNET is an Android normal/install-time permission rather than a dangerous
 * runtime permission requested through the Activity permission flow.
 *
 * This mapping preserves the Android platform requirement only.
 *
 * INTERNET permission
 * != network connectivity
 * != destination reachability
 * != source trust
 * != content truth
 * != Devil authorization
 * != capability availability
 * != capability READY
 * != retrieval success.
 *
 * Permission requirement mapping itself does not inspect grant state.
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

            AndroidInternetKnowledgeCapability.matches(
                capability,
            ) ->
                listOf(
                    Manifest.permission.INTERNET,
                )

            else ->
                null
        }
    }
}
