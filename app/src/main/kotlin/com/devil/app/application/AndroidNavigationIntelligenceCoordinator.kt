package com.devil.app.application

/**
 * Stage 182 bounded Navigation Intelligence coordinator.
 *
 * The coordinator consumes one exact Stage 177 application inspection.
 *
 * Only a genuinely FOUND and launchable application produces READY bounded
 * navigation information.
 *
 * NOT_FOUND and non-launchable applications remain DEFERRED.
 *
 * It does not:
 *
 * - call startActivity;
 * - obtain or execute a launch Intent;
 * - perform accessibility or global navigation actions;
 * - grant Devil authorization;
 * - establish constitutional Execution APPROVED;
 * - establish Observation, Verification, or Outcome;
 * - implement Stage 183 Notifications Intelligence.
 *
 * LAUNCHABLE_APPLICATION != EXECUTION_APPROVAL.
 * NAVIGATION_READY != APPLICATION_LAUNCHED.
 */
class AndroidNavigationIntelligenceCoordinator {

    fun prepare(
        applicationInspection: AndroidApplicationInspectionResult,
    ): AndroidNavigationIntelligenceResult {
        val application =
            applicationInspection.application

        if (
            applicationInspection.status !=
                AndroidApplicationInspectionStatus.FOUND ||
            application == null ||
            !application.launchable
        ) {
            return AndroidNavigationIntelligenceResult.create(
                status = AndroidNavigationIntelligenceStatus.DEFERRED,
                applicationInspection = applicationInspection,
            )
        }

        return AndroidNavigationIntelligenceResult.create(
            status = AndroidNavigationIntelligenceStatus.READY,
            applicationInspection = applicationInspection,
            packageName = application.packageName,
        )
    }
}
