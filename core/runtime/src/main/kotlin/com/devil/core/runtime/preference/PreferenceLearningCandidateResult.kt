package com.devil.core.runtime.preference

import com.devil.core.model.preference.PreferenceLearningCandidate

/**
 * Stable result of bounded preference-candidate production.
 *
 * AVAILABLE contains exactly one qualified PreferenceLearningCandidate.
 *
 * UNAVAILABLE contains no candidate.
 *
 * This result creates no Memory Proposal and grants no Memory Authority.
 */
@ConsistentCopyVisibility
data class PreferenceLearningCandidateResult private constructor(
    val status: PreferenceLearningCandidateStatus,
    val candidate: PreferenceLearningCandidate?,
) {
    companion object {
        fun create(
            status: PreferenceLearningCandidateStatus,
            candidate: PreferenceLearningCandidate? = null,
        ): PreferenceLearningCandidateResult {
            when (status) {
                PreferenceLearningCandidateStatus.AVAILABLE -> {
                    require(candidate != null) {
                        "Available preference candidate result requires one candidate."
                    }
                }

                PreferenceLearningCandidateStatus.UNAVAILABLE -> {
                    require(candidate == null) {
                        "Unavailable preference candidate result must not contain a candidate."
                    }
                }
            }

            return PreferenceLearningCandidateResult(
                status = status,
                candidate = candidate,
            )
        }
    }
}
