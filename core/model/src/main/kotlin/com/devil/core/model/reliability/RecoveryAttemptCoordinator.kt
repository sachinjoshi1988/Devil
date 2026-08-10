package com.devil.core.model.reliability

/**
 * Stage 45 bounded coordinator for recovery-attempt accounting.
 *
 * Flow:
 *
 * RecoveryRequestResult
 * -> RecoveryAttemptPolicy
 * -> RecoveryAttemptResult.
 *
 * This coordinator performs accounting only.
 *
 * It is not a Brain, Planner, Executive, Authorization Authority, Security
 * Authority, capability executor, retry engine, scheduler, or recovery engine.
 *
 * RECORDED means exactly one finite budget unit was consumed.
 *
 * RECORDED does not mean that any recovery action actually occurred.
 */
class RecoveryAttemptCoordinator(
    private val policy: RecoveryAttemptPolicy =
        RecoveryAttemptPolicy(),
) {

    fun record(
        requestResult: RecoveryRequestResult,
    ): RecoveryAttemptResult {
        return policy.record(
            requestResult = requestResult,
        )
    }
}
