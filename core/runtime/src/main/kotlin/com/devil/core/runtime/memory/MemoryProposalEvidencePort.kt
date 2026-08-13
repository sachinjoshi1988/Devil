package com.devil.core.runtime.memory

import com.devil.core.runtime.learning.LearningResult

/**
 * Neutral Memory Proposal evidence port between constitutional Learning and
 * constitutional Memory Proposal evaluation.
 *
 * The single Unified Devil Runtime may approach this port only with the genuine
 * LearningResult produced by the constitutional Learning Authority.
 *
 * Implementations may obtain bounded Memory Proposal evidence only through
 * authorized evidence mechanisms.
 *
 * This port grants no authority of its own and does not create a Memory
 * Proposal, invoke or bypass Memory Authority, commit Memory, persist Memory,
 * mutate world state, or report completion.
 *
 * LearningStatus.LEARNABLE is necessary for Memory Proposal evidence but does
 * not itself establish Memory Proposal evidence or prove that anything should
 * be remembered.
 *
 * This contract contains no Android dependency and creates no alternate Brain,
 * Executive, Planner, Security Authority, Learning Authority, Memory Authority,
 * memory domain, or runtime.
 *
 * LEARNING != MEMORY_PROPOSAL_EVIDENCE.
 * MEMORY_PROPOSAL_EVIDENCE != MEMORY_PROPOSAL.
 * MEMORY_PROPOSAL != MEMORY_AUTHORITY_APPROVAL.
 * MEMORY_AUTHORITY_APPROVAL != MEMORY_COMMITMENT.
 * MEMORY_COMMITMENT != MEMORY_PERSISTENCE.
 */
fun interface MemoryProposalEvidencePort {

    fun establish(
        learning: LearningResult,
    ): MemoryProposalEvidenceResult
}
