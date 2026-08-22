package com.devil.app.reliability

import com.devil.core.model.reliability.RecoveryAttemptBudget
import com.devil.core.model.reliability.RecoveryRequestCoordinator
import com.devil.core.model.reliability.RecoveryRequestStatus
import com.devil.core.model.reliability.RecoveryStrategy
import com.devil.core.model.reliability.ReliabilityAssessment

/**
 * Stage 194 bounded Android Reliability & Recovery coordinator.
 *
 * It delegates recovery-request construction to the existing Stage 45
 * Reliability & Recovery authority.
 *
 * It does not:
 *
 * - fabricate reliability evidence;
 * - automatically retry, restart, reconnect, or recover;
 * - consume a recovery attempt;
 * - use WorkManager or another scheduler;
 * - mutate capability health;
 * - erase failure evidence;
 * - grant Devil authorization;
 * - establish constitutional Verification or Outcome;
 * - implement Stage 195 Voice Architecture V2.
 *
 * RECOVERY_ELIGIBLE != RETRY_AUTHORIZED.
 * RECOVERY_REQUEST != RECOVERY_EXECUTED.
 * RECOVERY_RECORDED != RECOVERED.
 */
class AndroidReliabilityRecoveryCoordinator(
    private val recoveryRequestCoordinator: RecoveryRequestCoordinator =
        RecoveryRequestCoordinator(),
) {
    fun prepare(
        assessment: ReliabilityAssessment,
        strategy: RecoveryStrategy,
        attemptBudget: RecoveryAttemptBudget,
    ): AndroidReliabilityRecoveryResult {
        val requestResult =
            recoveryRequestCoordinator.request(
                assessment = assessment,
                strategy = strategy,
                attemptBudget = attemptBudget,
            )

        val status =
            when (requestResult.status) {
                RecoveryRequestStatus.AVAILABLE ->
                    AndroidReliabilityRecoveryStatus.AVAILABLE

                RecoveryRequestStatus.UNAVAILABLE,
                RecoveryRequestStatus.EXHAUSTED,
                ->
                    AndroidReliabilityRecoveryStatus.DEFERRED
            }

        return AndroidReliabilityRecoveryResult.create(
            status = status,
            assessment = assessment,
            recoveryRequestResult = requestResult,
        )
    }
}
