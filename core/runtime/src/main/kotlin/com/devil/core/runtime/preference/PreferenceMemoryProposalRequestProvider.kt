package com.devil.core.runtime.preference

import com.devil.core.model.learning.LearningRequest

/**
 * Prepares one typed generic MemoryProposalRequest only after preference-specific
 * Memory-Proposal evidence has already been established.
 *
 * The supplied LearningRequest remains the constitutional Learning provenance.
 *
 * The established preference candidate is preserved structurally in the
 * optional typed preferenceCandidate field.
 *
 * This provider does not create Memory Proposal evidence, create a Memory
 * Proposal, invoke Memory Authority, approve Memory, commit Memory, persist
 * Memory, assign memory metadata, mutate world state, or communicate externally.
 */
fun interface PreferenceMemoryProposalRequestProvider {

    fun provide(
        learning: LearningRequest,
        evidence: PreferenceMemoryProposalEvidenceResult,
    ): PreferenceMemoryProposalRequestResult
}
