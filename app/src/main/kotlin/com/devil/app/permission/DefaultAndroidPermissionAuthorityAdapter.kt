package com.devil.app.permission

import com.devil.core.model.capability.CapabilityContract

/**
 * Default Stage 29 Android Permission Authority Adapter.
 *
 * The adapter first obtains an approved capability-to-permission requirement
 * mapping.
 *
 * When no approved mapping exists it returns UNAVAILABLE and performs no
 * Android permission checks.
 *
 * When approved policy explicitly requires no runtime permission it returns
 * NOT_REQUIRED.
 *
 * When one or more explicit permissions are required, it inspects Android
 * grant state and returns GRANTED only when every required permission is
 * currently granted. Otherwise it returns DENIED.
 *
 * This adapter never requests permission, changes Android permission state,
 * grants Devil authorization, changes capability availability or health,
 * establishes Executive readiness, permits execution, performs execution,
 * observes execution, verifies outcomes, or claims success.
 */
class DefaultAndroidPermissionAuthorityAdapter(
    private val requirementSource:
        AndroidCapabilityPermissionRequirementSource =
        DefaultAndroidCapabilityPermissionRequirementSource(),
    private val grantChecker: AndroidPermissionGrantChecker,
) : AndroidPermissionAuthorityAdapter {

    override fun assess(
        capability: CapabilityContract,
    ): AndroidPermissionAssessment {
        val requiredPermissions =
            requirementSource.requiredPermissions(capability)
                ?: return AndroidPermissionAssessment.create(
                    capabilityId = capability.capabilityId,
                    status =
                        AndroidPermissionAssessmentStatus.UNAVAILABLE,
                )

        val normalizedPermissions =
            requiredPermissions
                .map { it.trim() }

        require(normalizedPermissions.none { it.isEmpty() }) {
            "Android capability permission requirements must not contain blank permission names."
        }

        val uniquePermissions = normalizedPermissions.distinct()

        if (uniquePermissions.isEmpty()) {
            return AndroidPermissionAssessment.create(
                capabilityId = capability.capabilityId,
                status =
                    AndroidPermissionAssessmentStatus.NOT_REQUIRED,
            )
        }

        val allGranted =
            uniquePermissions.all { permission ->
                grantChecker.isGranted(permission)
            }

        return AndroidPermissionAssessment.create(
            capabilityId = capability.capabilityId,
            status =
                if (allGranted) {
                    AndroidPermissionAssessmentStatus.GRANTED
                } else {
                    AndroidPermissionAssessmentStatus.DENIED
                },
            requiredPermissions = uniquePermissions,
        )
    }
}
