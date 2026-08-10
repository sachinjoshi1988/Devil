package com.devil.app.permission

import com.devil.app.accessibility.AndroidAccessibilityCapability
import com.devil.app.device.AndroidDeviceKnowledgeCapability
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
 * Therefore an empty permission list means:
 *
 * Android runtime permission NOT_REQUIRED.
 *
 * It does NOT mean:
 *
 * - capability available;
 * - capability READY;
 * - owner authenticated;
 * - Devil authorization granted;
 * - Executive readiness established;
 * - Execution APPROVED;
 * - action permitted;
 * - memory persistence permitted;
 * - observation established;
 * - verification established;
 * - or Outcome established.
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

            else ->
                null
        }
    }
}
