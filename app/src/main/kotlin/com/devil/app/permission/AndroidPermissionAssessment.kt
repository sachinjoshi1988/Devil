package com.devil.app.permission

import com.devil.core.model.capability.CapabilityId

/**
 * Preserves one bounded Android permission assessment for one capability.
 *
 * requiredPermissions contains Android permission names only when an approved
 * capability-to-permission requirement mapping exists and one or more runtime
 * permissions are required.
 *
 * This contract does not request permissions, grant permissions, grant Devil
 * authorization, establish capability availability or health, establish
 * Executive readiness, execute a capability, observe execution, verify an
 * outcome, or mutate constitutional state.
 */
@ConsistentCopyVisibility
data class AndroidPermissionAssessment private constructor(
    val capabilityId: CapabilityId,
    val status: AndroidPermissionAssessmentStatus,
    val requiredPermissions: List<String>,
) {
    companion object {
        fun create(
            capabilityId: CapabilityId,
            status: AndroidPermissionAssessmentStatus,
            requiredPermissions: List<String> = emptyList(),
        ): AndroidPermissionAssessment {
            val preservedPermissions =
                requiredPermissions
                    .map { it.trim() }
                    .also { permissions ->
                        require(permissions.none { it.isEmpty() }) {
                            "Android permission names must not be blank."
                        }
                    }
                    .distinct()

            when (status) {
                AndroidPermissionAssessmentStatus.GRANTED,
                AndroidPermissionAssessmentStatus.DENIED,
                -> require(preservedPermissions.isNotEmpty()) {
                    "Granted or denied Android permission assessments require at least one explicit permission."
                }

                AndroidPermissionAssessmentStatus.NOT_REQUIRED,
                AndroidPermissionAssessmentStatus.UNAVAILABLE,
                -> require(preservedPermissions.isEmpty()) {
                    "Not-required or unavailable Android permission assessments must not contain permissions."
                }
            }

            return AndroidPermissionAssessment(
                capabilityId = capabilityId,
                status = status,
                requiredPermissions = preservedPermissions,
            )
        }
    }
}
