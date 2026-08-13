package com.devil.core.runtime.preference

/**
 * Neutral evidence boundary between a qualified preference-learning candidate
 * and later constitutional Memory Proposal processing.
 *
 * Only an AVAILABLE PreferenceLearningCandidateResult may be considered for an
 * ESTABLISHED result.
 *
 * AVAILABLE does not itself establish evidence.
 *
 * Implementations may establish evidence only through an explicitly authorized
 * mechanism.
 *
 * This port grants no authority of its own and does not create Learning, create
 * Memory Proposal evidence for the generic Memory Authority path, create a
 * Memory Proposal, invoke Memory Authority, commit Memory, persist Memory, or
 * mutate world state.
 *
 * This contract is platform-independent and creates no alternate Brain,
 * Learning Authority, Memory Authority, memory domain, or runtime.
 */
fun interface PreferenceMemoryProposalEvidencePort {

    fun establish(
        candidateResult: PreferenceLearningCandidateResult,
    ): PreferenceMemoryProposalEvidenceResult
}
