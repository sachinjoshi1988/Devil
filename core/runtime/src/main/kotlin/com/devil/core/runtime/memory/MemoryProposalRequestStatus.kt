package com.devil.core.runtime.memory

/**
 * Describes whether one bounded constitutional memory-proposal request is
 * available.
 *
 * This status reports request availability only. It does not create a memory
 * proposal, approve or commit logical memory, mutate world state, change task
 * or plan state, communicate externally, bypass the single Memory Authority,
 * or produce a runtime result.
 */
enum class MemoryProposalRequestStatus {
    AVAILABLE,
    UNAVAILABLE,
    FAILED,
}
