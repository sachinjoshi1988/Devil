package com.devil.core.runtime.preference

/**
 * Describes whether one preference-specific typed Memory Proposal request
 * is available after preference Memory-Proposal evidence evaluation.
 *
 * AVAILABLE means one already-established preference Memory-Proposal evidence
 * result preserved a qualified preference candidate and one typed
 * MemoryProposalRequest may be prepared.
 *
 * UNAVAILABLE means no justified typed request is currently available.
 *
 * AVAILABLE does not mean that:
 *
 * - a Memory Proposal was approved;
 * - Memory Authority approved anything;
 * - Memory was committed;
 * - Memory was persisted;
 * - memory metadata was assigned;
 * - or storage occurred.
 *
 * PREFERENCE_MEMORY_PROPOSAL_EVIDENCE
 * != MEMORY_PROPOSAL_REQUEST
 * != MEMORY_PROPOSAL
 * != MEMORY_AUTHORITY_APPROVAL
 * != MEMORY_COMMITMENT
 * != MEMORY_PERSISTENCE.
 */
enum class PreferenceMemoryProposalRequestStatus {
    AVAILABLE,
    UNAVAILABLE,
}
