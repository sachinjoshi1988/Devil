package com.devil.core.model.reliability

/**
 * Stage 45 explicit post-attempt evidence supplied after one accounted recovery
 * attempt.
 *
 * The attempt record establishes only that one finite attempt budget unit was
 * consumed.
 *
 * The post-attempt RecoveryEvidence is separately supplied evidence describing
 * the reliability condition observed after that accounted attempt.
 *
 * Creating this record does not:
 *
 * - prove that the recovery strategy was actually executed;
 * - fabricate an observation;
 * - mutate capability health;
 * - clear the original failure;
 * - authorize another attempt;
 * - establish constitutional Verification;
 * - establish Outcome success;
 * - persist logical memory;
 * - or execute an action.
 */
@ConsistentCopyVisibility
data class PostRecoveryEvidence private constructor(
    val attempt: RecoveryAttemptRecord,
    val evidence: RecoveryEvidence,
) {
    companion object {

        fun create(
            attempt: RecoveryAttemptRecord,
            evidence: RecoveryEvidence,
        ): PostRecoveryEvidence {
            return PostRecoveryEvidence(
                attempt = attempt,
                evidence = evidence,
            )
        }
    }
}
