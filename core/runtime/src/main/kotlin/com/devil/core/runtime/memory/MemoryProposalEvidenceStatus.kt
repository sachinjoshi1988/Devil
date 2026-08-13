package com.devil.core.runtime.memory

/**
 * Describes whether genuine bounded constitutional Memory Proposal evidence was
 * established.
 *
 * ESTABLISHED means an authorized proposal-evidence mechanism genuinely
 * established evidence supporting one bounded Memory Proposal request.
 *
 * ESTABLISHED does not mean that:
 *
 * - logical memory exists;
 * - Memory Authority approved the proposal;
 * - Memory was committed;
 * - Memory was persisted;
 * - World Model state changed;
 * - broader task or plan completion occurred.
 *
 * DEFERRED means no justified Memory Proposal evidence was established.
 *
 * FAILED represents an operational Memory Proposal evidence failure with one
 * matching constitutional error.
 *
 * LEARNING != MEMORY_PROPOSAL_EVIDENCE.
 * MEMORY_PROPOSAL_EVIDENCE != MEMORY_PROPOSAL.
 * MEMORY_PROPOSAL != MEMORY_AUTHORITY_APPROVAL.
 * MEMORY_AUTHORITY_APPROVAL != MEMORY_COMMITMENT.
 * MEMORY_COMMITMENT != MEMORY_PERSISTENCE.
 */
enum class MemoryProposalEvidenceStatus {
    ESTABLISHED,
    DEFERRED,
    FAILED,
}
