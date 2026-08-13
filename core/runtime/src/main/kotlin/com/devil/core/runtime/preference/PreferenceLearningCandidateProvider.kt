package com.devil.core.runtime.preference

import com.devil.core.model.preference.PreferenceEvidenceSet

/**
 * Bounded bridge from an already-evaluated preference-learning result to a
 * stable preference candidate.
 *
 * The provider must preserve the original evidence provenance and may never turn
 * INSUFFICIENT_EVIDENCE or AMBIGUOUS assessment into an available candidate.
 *
 * It does not create constitutional Learning, Memory Proposal evidence, a Memory
 * Proposal, Memory Authority approval, Memory Commitment, or Memory Persistence.
 */
fun interface PreferenceLearningCandidateProvider {

    fun provide(
        evidenceSet: PreferenceEvidenceSet,
        learningResult: PreferenceLearningResult,
    ): PreferenceLearningCandidateResult
}
