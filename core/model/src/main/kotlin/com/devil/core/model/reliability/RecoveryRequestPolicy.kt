package com.devil.core.model.reliability

/**
 * Stage 45 pure policy controlling creation of bounded recovery requests.
 *
 * This policy performs no recovery.
 *
 * It does not:
 *
 * - consume an attempt;
 * - grant authorization;
 * - invoke the Brain;
 * - invoke the Executive;
 * - invoke UnifiedDevilRuntime;
 * - execute a capability;
 * - mutate capability health;
 * - clear failure evidence;
 * - persist memory;
 * - or establish Outcome.
 */
class RecoveryRequestPolicy {

    fun evaluate(
        assessment: ReliabilityAssessment,
        strategy: RecoveryStrategy,
        attemptBudget: RecoveryAttemptBudget,
    ): RecoveryRequestResult {
        if (
            assessment.disposition !=
            RecoveryDisposition.RECOVERY_ELIGIBLE
        ) {
            return RecoveryRequestResult.unavailable(
                reason =
                    "Recovery request is unavailable because the reliability assessment is not RECOVERY_ELIGIBLE.",
            )
        }

        if (attemptBudget.exhausted) {
            return RecoveryRequestResult.exhausted(
                reason =
                    "Recovery request is unavailable because the finite recovery-attempt budget is exhausted.",
            )
        }

        return RecoveryRequestResult.available(
            request =
                RecoveryRequest.create(
                    assessment = assessment,
                    strategy = strategy,
                    attemptBudget = attemptBudget,
                ),
        )
    }
}
