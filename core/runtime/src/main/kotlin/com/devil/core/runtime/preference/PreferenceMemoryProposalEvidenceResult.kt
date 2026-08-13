package com.devil.core.runtime.preference

import com.devil.core.model.preference.PreferenceLearningCandidate

/**
 * Stable neutral result of one bounded preference Memory-Proposal-evidence
 * attempt.
 *
 * ESTABLISHED preserves the exact qualified PreferenceLearningCandidate whose
 * structured key, value, confidence, support counts, and evidence provenance
 * were evaluated.
 *
 * DEFERRED contains no candidate.
 *
 * This result does not create constitutional Learning, create a Memory Proposal,
 * invoke Memory Authority, approve Memory, commit Memory, persist Memory, assign
 * memory class, sensitivity, retention policy, storage destination, or
 * owner-visible reason.
 */
@ConsistentCopyVisibility
data class PreferenceMemoryProposalEvidenceResult private constructor(
    val status: PreferenceMemoryProposalEvidenceStatus,
    val candidate: PreferenceLearningCandidate?,
) {
    companion object {
        fun create(
            status: PreferenceMemoryProposalEvidenceStatus,
            candidate: PreferenceLearningCandidate? = null,
        ): PreferenceMemoryProposalEvidenceResult {
            when (status) {
                PreferenceMemoryProposalEvidenceStatus.ESTABLISHED -> {
                    require(candidate != null) {
                        "Established preference Memory Proposal evidence requires one qualified preference candidate."
                    }
                }

                PreferenceMemoryProposalEvidenceStatus.DEFERRED -> {
                    require(candidate == null) {
                        "Deferred preference Memory Proposal evidence must not contain a preference candidate."
                    }
                }
            }

            return PreferenceMemoryProposalEvidenceResult(
                status = status,
                candidate = candidate,
            )
        }
    }
}
