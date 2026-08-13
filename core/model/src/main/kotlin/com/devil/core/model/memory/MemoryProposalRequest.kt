package com.devil.core.model.memory

import com.devil.core.model.learning.LearningRequest
import com.devil.core.model.preference.PreferenceLearningCandidate

/**
 * Represents one structured request for bounded constitutional memory-proposal
 * evaluation.
 *
 * The request preserves one existing LearningRequest after genuine
 * constitutional evidence established that one bounded learning proposal may
 * be produced.
 *
 * It may also preserve one already-qualified typed PreferenceLearningCandidate
 * when an explicitly authorized preference-specific evidence path later
 * establishes that the candidate is eligible to enter constitutional Memory
 * Proposal processing.
 *
 * Preserving a preference candidate does not establish preference-specific
 * Memory Proposal evidence, create a Memory Proposal, invoke Memory Authority,
 * approve Memory, commit Memory, persist Memory, assign memory class,
 * sensitivity, retention policy, storage destination, owner-visible reason,
 * or authorization.
 *
 * A null preferenceCandidate preserves the existing generic constitutional
 * Memory Proposal request shape and keeps all legacy callers source-compatible.
 *
 * This request does not create learning, reinterpret earlier constitutional
 * evidence, claim that logical memory was created or committed, mutate world
 * state, change task or plan state, communicate externally, bypass the single
 * Memory Authority, or produce a runtime result.
 *
 * PREFERENCE_CANDIDATE != MEMORY_PROPOSAL.
 * MEMORY_PROPOSAL != MEMORY_AUTHORITY_APPROVAL.
 * MEMORY_AUTHORITY_APPROVAL != MEMORY_COMMITMENT.
 * MEMORY_COMMITMENT != MEMORY_PERSISTENCE.
 */
@ConsistentCopyVisibility
data class MemoryProposalRequest private constructor(
    val learning: LearningRequest,
    val preferenceCandidate: PreferenceLearningCandidate?,
) {
    companion object {
        fun create(
            learning: LearningRequest,
            preferenceCandidate: PreferenceLearningCandidate? = null,
        ): MemoryProposalRequest {
            return MemoryProposalRequest(
                learning = learning,
                preferenceCandidate = preferenceCandidate,
            )
        }
    }
}
