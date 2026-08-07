package com.devil.core.model.memory

/**
 * Represents one structured request for bounded review by the single
 * constitutional Memory Authority.
 *
 * The request preserves one existing MemoryProposalRequest after genuine
 * constitutional evidence established that one bounded memory proposal may be
 * eligible for Memory Authority review.
 *
 * Preserving the proposal request does not approve, create, persist, or commit
 * logical memory. It does not assign a memory class, sensitivity, retention
 * policy, confidence, source, owner-visible reason, or storage destination.
 *
 * This request does not mutate world state, change task or plan state,
 * communicate externally, bypass constitutional security review, or produce a
 * runtime result.
 */
@ConsistentCopyVisibility
data class MemoryAuthorityRequest private constructor(
    val proposal: MemoryProposalRequest,
) {
    companion object {
        fun create(
            proposal: MemoryProposalRequest,
        ): MemoryAuthorityRequest {
            return MemoryAuthorityRequest(
                proposal = proposal,
            )
        }
    }
}
