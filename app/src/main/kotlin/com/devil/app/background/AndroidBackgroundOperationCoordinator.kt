package com.devil.app.background

/**
 * Stage 193 bounded Android Background Operation coordinator.
 *
 * It accepts only an explicitly supplied background-operation request.
 *
 * It does not:
 *
 * - use WorkManager, Worker, JobScheduler, or JobService;
 * - create or start a foreground/background service;
 * - add manifest components;
 * - schedule or execute work;
 * - perform retries or recovery;
 * - acquire wake locks;
 * - grant Devil authorization;
 * - establish execution, Observation, Verification, or Outcome;
 * - implement Stage 194 Android Reliability & Recovery.
 *
 * BACKGROUND_READY != SCHEDULED.
 * SCHEDULED != EXECUTED.
 * BACKGROUND_OPERATION != DEVIL_AUTHORIZATION.
 */
class AndroidBackgroundOperationCoordinator {
    fun prepare(
        request: AndroidBackgroundOperationRequest?,
    ): AndroidBackgroundOperationResult {
        if (request == null) {
            return AndroidBackgroundOperationResult.create(
                status = AndroidBackgroundOperationStatus.DEFERRED,
            )
        }

        return AndroidBackgroundOperationResult.create(
            status = AndroidBackgroundOperationStatus.READY,
            request = request,
        )
    }
}
