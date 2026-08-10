package com.devil.core.model.reliability

/**
 * Stage 45 pure policy for bounded post-attempt recovery verification.
 *
 * Recovery is established only from explicitly supplied post-attempt evidence.
 *
 * The existence of:
 *
 * - a RecoveryRequest;
 * - a RecoveryAttemptRecord;
 * - a recovery strategy;
 * - a consumed attempt;
 * - or remaining retry budget
 *
 * is never recovery-success evidence.
 *
 * This policy performs:
 *
 * - no retry;
 * - no restart;
 * - no reconnection;
 * - no I/O;
 * - no runtime invocation;
 * - no execution;
 * - no capability-state mutation;
 * - no constitutional Verification;
 * - no Outcome establishment;
 * - and no logical-memory persistence.
 */
class RecoveryVerificationPolicy {

    fun verify(
        postRecoveryEvidence: PostRecoveryEvidence,
    ): RecoveryVerificationResult {
        return when (
            postRecoveryEvidence.evidence.condition
        ) {
            ReliabilityCondition.HEALTHY ->
                RecoveryVerificationResult.create(
                    status =
                        RecoveryVerificationStatus.VERIFIED_RECOVERED,
                    postRecoveryEvidence = postRecoveryEvidence,
                    rationale =
                        "Explicit post-attempt reliability evidence is HEALTHY.",
                )

            ReliabilityCondition.DEGRADED ->
                RecoveryVerificationResult.create(
                    status =
                        RecoveryVerificationStatus.NOT_RECOVERED,
                    postRecoveryEvidence = postRecoveryEvidence,
                    rationale =
                        "Post-attempt reliability evidence remains DEGRADED.",
                )

            ReliabilityCondition.UNAVAILABLE ->
                RecoveryVerificationResult.create(
                    status =
                        RecoveryVerificationStatus.NOT_RECOVERED,
                    postRecoveryEvidence = postRecoveryEvidence,
                    rationale =
                        "Post-attempt reliability evidence remains UNAVAILABLE.",
                )

            ReliabilityCondition.FAILED ->
                RecoveryVerificationResult.create(
                    status =
                        RecoveryVerificationStatus.NOT_RECOVERED,
                    postRecoveryEvidence = postRecoveryEvidence,
                    rationale =
                        "Post-attempt reliability evidence remains FAILED.",
                )
        }
    }
}
