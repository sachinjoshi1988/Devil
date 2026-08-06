package com.devil.core.model.memory

import com.devil.core.model.learning.LearningRequest

/**
 * Represents one structured request for bounded constitutional memory-proposal
 * evaluation.
 *
 * The request preserves one existing LearningRequest after genuine
 * constitutional evidence established that one bounded learning proposal may
 * be produced. It does not create learning, reinterpret earlier constitutional
 * evidence, or claim that logical memory was created or committed.
 *
 * This request does not create a memory proposal, approve or commit logical
 * memory, mutate world state, change task or plan state, communicate
 * externally, bypass the single Memory Authority, or produce a runtime result.
 */
@ConsistentCopyVisibility
data class MemoryProposalRequest private constructor(
    val learning: LearningRequest,
) {
    companion object {
        fun create(
            learning: LearningRequest,
        ): MemoryProposalRequest {
            return MemoryProposalRequest(
                learning = learning,
            )
        }
    }
}
