package com.devil.core.runtime.preference

/**
 * Stable result of one bounded preference-learning assessment.
 *
 * The result describes only the strength of supplied preference evidence.
 *
 * QUALIFIED is a candidate for later constitutional processing. It is not
 * LearningResult.LEARNABLE, Memory Proposal evidence, a Memory Proposal,
 * Memory Authority approval, Memory Commitment, or Memory Persistence.
 */
@ConsistentCopyVisibility
data class PreferenceLearningResult private constructor(
    val key: String,
    val status: PreferenceLearningStatus,
    val candidateValue: String?,
    val confidence: Double,
    val supportingEvidenceCount: Int,
    val totalEvidenceCount: Int,
) {
    companion object {
        fun create(
            key: String,
            status: PreferenceLearningStatus,
            candidateValue: String?,
            confidence: Double,
            supportingEvidenceCount: Int,
            totalEvidenceCount: Int,
        ): PreferenceLearningResult {
            val normalizedKey = key.trim()
            val normalizedCandidate =
                candidateValue
                    ?.trim()
                    ?.takeIf {
                        it.isNotEmpty()
                    }

            require(normalizedKey.isNotEmpty()) {
                "Preference learning result requires a nonblank key."
            }

            require(totalEvidenceCount > 0) {
                "Preference learning result requires evidence."
            }

            require(
                supportingEvidenceCount > 0 &&
                    supportingEvidenceCount <= totalEvidenceCount,
            ) {
                "Preference learning support count must be positive and cannot exceed total evidence."
            }

            require(
                confidence >= 0.0 &&
                    confidence <= 1.0,
            ) {
                "Preference learning confidence must be between 0.0 and 1.0."
            }

            when (status) {
                PreferenceLearningStatus.QUALIFIED,
                PreferenceLearningStatus.INSUFFICIENT_EVIDENCE -> {
                    require(normalizedCandidate != null) {
                        "Qualified or insufficient preference learning results require a candidate value."
                    }
                }

                PreferenceLearningStatus.AMBIGUOUS -> {
                    require(normalizedCandidate == null) {
                        "Ambiguous preference learning results must not select a candidate value."
                    }
                }
            }

            return PreferenceLearningResult(
                key = normalizedKey,
                status = status,
                candidateValue = normalizedCandidate,
                confidence = confidence,
                supportingEvidenceCount =
                    supportingEvidenceCount,
                totalEvidenceCount =
                    totalEvidenceCount,
            )
        }
    }
}
