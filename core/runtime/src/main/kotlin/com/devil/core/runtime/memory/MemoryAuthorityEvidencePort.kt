package com.devil.core.runtime.memory

/**
 * Neutral Memory Authority evidence port between constitutional Memory Proposal
 * evaluation and the single constitutional Memory Authority.
 *
 * The single Unified Devil Runtime may approach this port only with the genuine
 * MemoryProposalResult produced by the constitutional Memory Proposal
 * Authority.
 *
 * Implementations may establish bounded Memory Authority evidence only through
 * authorized evidence mechanisms.
 *
 * This port grants no authority of its own and does not approve Memory, commit
 * Memory, persist Memory, assign memory class, sensitivity, confidence,
 * retention policy, source attribution, owner-visible reason, storage
 * destination, mutate world state, or report completion.
 *
 * MemoryProposalStatus.PROPOSABLE is necessary for Memory Authority evidence
 * but does not itself establish Memory Authority evidence or prove that Memory
 * Authority approval should occur.
 *
 * This contract contains no Android dependency and creates no alternate Brain,
 * Executive, Planner, Security Authority, Learning Authority, Memory Authority,
 * memory domain, or runtime.
 *
 * MEMORY_PROPOSAL != MEMORY_AUTHORITY_EVIDENCE.
 * MEMORY_AUTHORITY_EVIDENCE != MEMORY_AUTHORITY_APPROVAL.
 * MEMORY_AUTHORITY_APPROVAL != MEMORY_COMMITMENT.
 * MEMORY_COMMITMENT != MEMORY_PERSISTENCE.
 */
fun interface MemoryAuthorityEvidencePort {

    fun establish(
        memoryProposal: MemoryProposalResult,
    ): MemoryAuthorityEvidenceResult
}
