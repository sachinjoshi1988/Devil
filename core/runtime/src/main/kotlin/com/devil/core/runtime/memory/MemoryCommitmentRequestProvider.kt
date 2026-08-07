package com.devil.core.runtime.memory

/**
 * Supplies one bounded logical-memory commitment request when the single
 * constitutional Memory Authority has established commitment eligibility.
 *
 * This provider does not create, persist, store, expose, recall, or commit
 * logical memory. It does not assign memory class, sensitivity, confidence,
 * retention policy, source attribution, owner-visible reason, storage
 * destination, or deletion policy.
 *
 * It does not invoke storage, mutate world state, change task or plan state,
 * communicate externally, bypass the single Memory Authority, or produce a
 * runtime result.
 */
interface MemoryCommitmentRequestProvider {

    fun provide(
        memory: MemoryAuthorityResult,
    ): MemoryCommitmentRequestResult
}
