package com.devil.core.model.reliability

/**
 * Stage 45 bounded coordinator for post-attempt recovery verification.
 *
 * Flow:
 *
 * RecoveryAttemptRecord
 * + explicit post-attempt RecoveryEvidence
 * -> PostRecoveryEvidence
 * -> RecoveryVerificationPolicy
 * -> RecoveryVerificationResult.
 *
 * This coordinator is not the constitutional Verification Authority.
 *
 * It does not retry, restart, reconnect, execute, mutate capability state,
 * establish an Outcome, invoke UnifiedDevilRuntime, or persist logical memory.
 *
 * VERIFIED_RECOVERED means only that the supplied post-attempt reliability
 * evidence is HEALTHY.
 */
class RecoveryVerificationCoordinator(
    private val policy: RecoveryVerificationPolicy =
        RecoveryVerificationPolicy(),
) {

    fun verify(
        attempt: RecoveryAttemptRecord,
        evidence: RecoveryEvidence,
    ): RecoveryVerificationResult {
        val postRecoveryEvidence =
            PostRecoveryEvidence.create(
                attempt = attempt,
                evidence = evidence,
            )

        return policy.verify(
            postRecoveryEvidence = postRecoveryEvidence,
        )
    }
}
