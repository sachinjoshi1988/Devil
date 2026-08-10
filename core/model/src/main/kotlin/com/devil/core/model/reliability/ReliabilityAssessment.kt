package com.devil.core.model.reliability

/**
 * Immutable Stage 45 result of one bounded reliability assessment.
 *
 * The original RecoveryEvidence remains attached so the basis of the assessment
 * is explicit.
 *
 * This result describes recovery disposition only.
 *
 * It does not retry, restart, reinitialize, execute, mutate capability state,
 * erase error evidence, change authorization, or establish a verified Outcome.
 */
@ConsistentCopyVisibility
data class ReliabilityAssessment private constructor(
    val evidence: RecoveryEvidence,
    val disposition: RecoveryDisposition,
    val rationale: String,
) {
    companion object {

        fun create(
            evidence: RecoveryEvidence,
            disposition: RecoveryDisposition,
            rationale: String,
        ): ReliabilityAssessment {
            val normalizedRationale =
                rationale.trim()

            require(normalizedRationale.isNotEmpty()) {
                "Reliability assessment rationale must not be blank."
            }

            return ReliabilityAssessment(
                evidence = evidence,
                disposition = disposition,
                rationale = normalizedRationale,
            )
        }
    }
}
