package com.devil.core.runtime.preference

/**
 * Describes whether genuine bounded constitutional evidence exists for allowing
 * one qualified preference candidate to approach later Memory Proposal
 * processing.
 *
 * ESTABLISHED means an authorized mechanism genuinely established evidence for
 * the exact qualified preference candidate.
 *
 * DEFERRED means no such evidence is currently established.
 *
 * A qualified preference candidate is necessary input to this boundary but is
 * never itself proof that Memory should be proposed.
 *
 * QUALIFIED_PREFERENCE
 * != PREFERENCE_MEMORY_PROPOSAL_EVIDENCE
 * != MEMORY_PROPOSAL
 * != MEMORY_AUTHORITY_APPROVAL
 * != MEMORY_COMMITMENT
 * != MEMORY_PERSISTENCE.
 */
enum class PreferenceMemoryProposalEvidenceStatus {
    ESTABLISHED,
    DEFERRED,
}
