package com.devil.core.runtime.memory

/**
 * Supplies one bounded Memory Authority request when constitutional memory
 * proposal evaluation established that one proposal is eligible for later
 * review by the single Memory Authority.
 *
 * This provider does not approve, create, persist, or commit logical memory.
 * It does not assign memory class, sensitivity, retention policy, confidence,
 * source, owner-visible reason, or storage destination.
 *
 * It does not mutate world state, change task or plan state, communicate
 * externally, bypass constitutional security review, or produce a runtime
 * result.
 */
interface MemoryAuthorityRequestProvider {

    fun provide(
        proposal: MemoryProposalResult,
    ): MemoryAuthorityRequestResult
}
