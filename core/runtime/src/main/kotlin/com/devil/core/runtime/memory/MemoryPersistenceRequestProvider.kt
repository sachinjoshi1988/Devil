package com.devil.core.runtime.memory

/**
 * Supplies one bounded logical-memory persistence request when constitutional
 * logical-memory commitment evaluation established persistence eligibility.
 *
 * This provider does not create, persist, store, expose, recall, delete, or
 * commit logical memory.
 *
 * It does not assign or alter memory class, sensitivity, confidence, retention
 * policy, source attribution, owner-visible reason, storage destination,
 * deletion policy, encryption policy, replication policy, or other
 * logical-memory metadata.
 *
 * It does not invoke storage, mutate world state, change task or plan state,
 * communicate externally, bypass the single Memory Authority, or produce a
 * runtime result.
 */
interface MemoryPersistenceRequestProvider {

    fun provide(
        commitment: MemoryCommitmentResult,
    ): MemoryPersistenceRequestResult
}
