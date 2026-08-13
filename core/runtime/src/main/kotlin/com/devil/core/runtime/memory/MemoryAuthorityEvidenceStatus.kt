package com.devil.core.runtime.memory

/**
 * Describes whether genuine bounded constitutional Memory Authority evidence was
 * established for one Memory Proposal result.
 *
 * ESTABLISHED means an authorized evidence mechanism independently established
 * evidence sufficient for later Memory Authority evaluation.
 *
 * DEFERRED means no justified Memory Authority evidence was established.
 *
 * FAILED means the evidence mechanism failed operationally.
 *
 * A PROPOSABLE MemoryProposalResult is necessary before Memory Authority
 * evidence may be considered, but is not itself Memory Authority evidence.
 *
 * MEMORY_PROPOSAL != MEMORY_AUTHORITY_EVIDENCE.
 * MEMORY_AUTHORITY_EVIDENCE != MEMORY_AUTHORITY_APPROVAL.
 * MEMORY_AUTHORITY_APPROVAL != MEMORY_COMMITMENT.
 * MEMORY_COMMITMENT != MEMORY_PERSISTENCE.
 */
enum class MemoryAuthorityEvidenceStatus {
    ESTABLISHED,
    DEFERRED,
    FAILED,
}
