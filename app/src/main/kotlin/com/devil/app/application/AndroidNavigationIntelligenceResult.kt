package com.devil.app.application

/**
 * Stage 182 bounded Navigation Intelligence result.
 *
 * READY preserves one exact Stage 177 application inspection together with
 * the exact package name prepared for bounded future navigation.
 *
 * DEFERRED preserves the Stage 177 inspection and no navigation package.
 *
 * It does not:
 *
 * - call startActivity;
 * - obtain or execute a launch Intent;
 * - perform accessibility or global navigation actions;
 * - grant Devil authorization;
 * - establish constitutional Execution APPROVED;
 * - establish Observation, Verification, or Outcome.
 *
 * LAUNCHABLE_APPLICATION != EXECUTION_APPROVAL.
 * NAVIGATION_READY != APPLICATION_LAUNCHED.
 * APPLICATION_LAUNCHED != VERIFIED_OUTCOME.
 */
@ConsistentCopyVisibility
data class AndroidNavigationIntelligenceResult private constructor(
    val status: AndroidNavigationIntelligenceStatus,
    val applicationInspection: AndroidApplicationInspectionResult,
    val packageName: String?,
) {
    companion object {

        fun create(
            status: AndroidNavigationIntelligenceStatus,
            applicationInspection: AndroidApplicationInspectionResult,
            packageName: String? = null,
        ): AndroidNavigationIntelligenceResult {
            val normalizedPackageName =
                packageName
                    ?.trim()
                    ?.takeIf { it.isNotEmpty() }

            when (status) {
                AndroidNavigationIntelligenceStatus.READY -> {
                    val application =
                        requireNotNull(applicationInspection.application) {
                            "Ready Android navigation intelligence requires a found application."
                        }

                    require(
                        applicationInspection.status ==
                            AndroidApplicationInspectionStatus.FOUND,
                    ) {
                        "Ready Android navigation intelligence requires a found Stage 177 application inspection."
                    }

                    require(application.launchable) {
                        "Ready Android navigation intelligence requires a launchable application."
                    }

                    require(normalizedPackageName == application.packageName) {
                        "Ready Android navigation intelligence must preserve the exact Stage 177 application package name."
                    }
                }

                AndroidNavigationIntelligenceStatus.DEFERRED ->
                    require(normalizedPackageName == null) {
                        "Deferred Android navigation intelligence must not contain a navigation package name."
                    }
            }

            return AndroidNavigationIntelligenceResult(
                status = status,
                applicationInspection = applicationInspection,
                packageName = normalizedPackageName,
            )
        }
    }
}
