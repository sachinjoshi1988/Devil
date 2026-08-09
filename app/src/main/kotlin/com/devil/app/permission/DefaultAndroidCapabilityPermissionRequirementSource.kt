package com.devil.app.permission

import com.devil.core.model.capability.CapabilityContract

/**
 * Default Stage 29 Android capability-permission requirement source.
 *
 * No production Android capability has yet established an approved mapping
 * between its CapabilityContract and Android runtime permissions.
 *
 * Therefore this source returns null rather than guessing from capability
 * category, capability name, Android APIs, manifest declarations, hardware
 * features, or planned future behavior.
 */
class DefaultAndroidCapabilityPermissionRequirementSource :
    AndroidCapabilityPermissionRequirementSource {

    override fun requiredPermissions(
        capability: CapabilityContract,
    ): List<String>? {
        return null
    }
}
