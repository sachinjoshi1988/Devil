package com.devil.app.permission

import com.devil.core.model.capability.CapabilityContract

/**
 * Android platform adapter for bounded runtime-permission assessment.
 *
 * The adapter reports Android operating-system permission state only.
 *
 * It is not the Devil Authorization Authority and must never translate Android
 * permission into constitutional authorization.
 *
 * Android permission != Devil authorization.
 */
fun interface AndroidPermissionAuthorityAdapter {

    fun assess(
        capability: CapabilityContract,
    ): AndroidPermissionAssessment
}
