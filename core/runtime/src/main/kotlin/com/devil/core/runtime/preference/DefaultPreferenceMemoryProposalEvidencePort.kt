package com.devil.core.runtime.preference

/**
 * Default fail-closed preference Memory-Proposal-evidence implementation.
 *
 * No authorized production mechanism for establishing preference-specific
 * Memory Proposal evidence is configured inside core runtime.
 *
 * Therefore an AVAILABLE qualified preference candidate remains DEFERRED.
 *
 * This is intentional:
 *
 * preference qualification
 * != evidence that the preference should enter Memory Proposal processing.
 *
 * The implementation preserves constitutional separation and invents no
 * evidence, Memory Proposal, Memory Authority approval, commitment, persistence,
 * or storage policy.
 */
class DefaultPreferenceMemoryProposalEvidencePort :
    PreferenceMemoryProposalEvidencePort {

    override fun establish(
        candidateResult: PreferenceLearningCandidateResult,
    ): PreferenceMemoryProposalEvidenceResult {
        return when (candidateResult.status) {
            PreferenceLearningCandidateStatus.AVAILABLE -> {
                requireNotNull(candidateResult.candidate)

                PreferenceMemoryProposalEvidenceResult.create(
                    status =
                        PreferenceMemoryProposalEvidenceStatus.DEFERRED,
                )
            }

            PreferenceLearningCandidateStatus.UNAVAILABLE ->
                PreferenceMemoryProposalEvidenceResult.create(
                    status =
                        PreferenceMemoryProposalEvidenceStatus.DEFERRED,
                )
        }
    }
}
