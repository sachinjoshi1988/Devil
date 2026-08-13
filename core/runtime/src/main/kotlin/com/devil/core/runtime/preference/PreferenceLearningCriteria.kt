package com.devil.core.runtime.preference

/**
 * Explicit bounded criteria for deciding whether accumulated preference evidence
 * is strong enough to qualify as a preference-learning candidate.
 *
 * Repeated evidence is mandatory: minimumIndependentEvidence must always be at
 * least two.
 *
 * minimumConfidence must be greater than one half so a candidate supported by
 * no more evidence than its alternatives cannot qualify.
 *
 * These criteria do not establish Memory policy, Memory confidence metadata,
 * retention policy, sensitivity, source attribution, owner-visible reason, or
 * storage authority.
 */
@ConsistentCopyVisibility
data class PreferenceLearningCriteria private constructor(
    val minimumIndependentEvidence: Int,
    val minimumConfidence: Double,
) {
    companion object {
        fun create(
            minimumIndependentEvidence: Int,
            minimumConfidence: Double,
        ): PreferenceLearningCriteria {
            require(minimumIndependentEvidence >= 2) {
                "Preference learning requires repeated evidence from at least two independent traces."
            }

            require(
                minimumConfidence > 0.5 &&
                    minimumConfidence <= 1.0,
            ) {
                "Preference learning confidence threshold must be greater than 0.5 and at most 1.0."
            }

            return PreferenceLearningCriteria(
                minimumIndependentEvidence =
                    minimumIndependentEvidence,
                minimumConfidence = minimumConfidence,
            )
        }
    }
}
