package com.devil.core.runtime.memory

import com.devil.core.runtime.learning.LearningResult

/**
 * Supplies one structured constitutional memory-proposal request when genuine
 * constitutional evidence established that one bounded learning proposal may
 * be produced.
 *
 * This provider does not create a memory proposal, approve or commit logical
 * memory, mutate world state, change task or plan state, communicate
 * externally, bypass the single Memory Authority, or produce a runtime result.
 */
interface MemoryProposalRequestProvider {

    fun provide(
        learning: LearningResult,
    ): MemoryProposalRequestResult
}
