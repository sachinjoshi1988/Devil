package com.devil.core.model.reliability

/**
 * Immutable Stage 45 result of one bounded post-attempt recovery verification.
 *
 * The complete PostRecoveryEvidence remains attached so the basis of the result
 * is explicit.
 *
 * This result does not rewrite or delete the original failure evidence retained
 * through the RecoveryAttemptRecord -> RecoveryRequest -> ReliabilityAssessment
 * chain.
 *
 * VERIFIED_RECOVERED describes the supplied post-attempt reliability evidence
 * only.
 *
 * It must never be treated as constitutional Verification or Outcome success.
 */
@ConsistentCopyVisibility
data class RecoveryVerificationResult private constructor(
    val status: RecoveryVerificationStatus,
    val postRecoveryEvidence: PostRecoveryEvidence,
    val rationale: String,
) {
    companion object {

        fun create(
            status: RecoveryVerificationStatus,
            postRecoveryEvidence: PostRecoveryEvidence,
            rationale: String,
        ): RecoveryVerificationResult {
            val normalizedRationale =
                rationale.trim()

            require(normalizedRationale.isNotEmpty()) {
                "Recovery verification rationale must not be blank."
            }

            return RecoveryVerificationResult(
                status = status,
                postRecoveryEvidence = postRecoveryEvidence,
                rationale = normalizedRationale,
            )
        }
    }
}
