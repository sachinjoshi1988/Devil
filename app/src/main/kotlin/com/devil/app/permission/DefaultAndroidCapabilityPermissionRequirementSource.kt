package com.devil.app.permission

import com.devil.app.accessibility.AndroidAccessibilityCapability
import com.devil.core.model.capability.CapabilityContract

/**
 * Default Android capability-permission requirement source.
 *
 * Stage 38 establishes that the bounded accessibility click capability does not
 * use an Android runtime permission requested through Activity permission APIs.
 *
 * Accessibility-service enablement is a separate Android system setting and
 * lifecycle boundary represented by DevilAccessibilityService connection state.
 *
 * Therefore an empty permission list means:
 *
 * Android runtime permission NOT_REQUIRED.
 *
 * It does NOT mean:
 *
 * - accessibility service enabled;
 * - capability available;
 * - capability READY;
 * - owner authenticated;
 * - Devil authorization granted;
 * - Execution APPROVED;
 * - accessibility action permitted;
 * - action attempted;
 * - effect observed;
 * - outcome verified.
 *
 * Unknown capability mappings remain null and therefore unavailable for a
 * justified Android permission assessment.
 */
class DefaultAndroidCapabilityPermissionRequirementSource :
    AndroidCapabilityPermissionRequirementSource {

    override fun requiredPermissions(
        capability: CapabilityContract,
    ): List<String>? {
        return if (
            AndroidAccessibilityCapability.matches(capability)
        ) {
            emptyList()
        } else {
            null
        }
    }
}
