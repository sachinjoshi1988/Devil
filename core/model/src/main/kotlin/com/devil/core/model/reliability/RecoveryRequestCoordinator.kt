package com.devil.core.model.reliability

/**
 * Stage 45 bounded coordinator for recovery-request construction.
 *
 * Flow:
 *
 * ReliabilityAssessment
 * + RecoveryStrategy
 * + RecoveryAttemptBudget
 * -> RecoveryRequestPolicy
 * -> RecoveryRequestResult.
 *
 * This coordinator is not another Brain, Planner, Executive, Authorization
 * Authority, Security Authority, capability authority, execution mechanism, or
 * recovery executor.
 *
 * An AVAILABLE RecoveryRequest remains only a request for later constitutional
 * consideration.
 */
class RecoveryRequestCoordinator(
    private val policy: RecoveryRequestPolicy =
        RecoveryRequestPolicy(),
) {

    fun request(
        assessment: ReliabilityAssessment,
        strategy: RecoveryStrategy,
        attemptBudget: RecoveryAttemptBudget,
    ): RecoveryRequestResult {
        return policy.evaluate(
            assessment = assessment,
            strategy = strategy,
            attemptBudget = attemptBudget,
        )
    }
}
