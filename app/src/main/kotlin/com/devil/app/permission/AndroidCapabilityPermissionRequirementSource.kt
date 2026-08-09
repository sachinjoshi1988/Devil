package com.devil.app.permission

import com.devil.core.model.capability.CapabilityContract

/**
 * Supplies the approved Android runtime-permission requirements for one
 * registered capability.
 *
 * null means no approved capability-to-permission mapping is currently
 * available.
 *
 * An empty list means approved policy explicitly establishes that the
 * capability requires no Android runtime permission.
 *
 * A non-empty list contains the exact Android runtime permissions that must be
 * checked.
 *
 * This source does not inspect Android grant state, request permission, grant
 * Devil authorization, establish readiness, or execute capabilities.
 */
fun interface AndroidCapabilityPermissionRequirementSource {

    fun requiredPermissions(
        capability: CapabilityContract,
    ): List<String>?
}
