package com.devil.core.runtime.memory

/**
 * Represents the availability of one bounded logical-memory persistence request.
 *
 * AVAILABLE means one constitutionally valid MemoryPersistenceRequest is
 * available for later controlled persistence evaluation.
 *
 * UNAVAILABLE means no justified persistence request can currently be
 * established.
 *
 * FAILED means persistence-request preparation failed with one matching error.
 *
 * This status does not create, persist, store, expose, recall, delete, or commit
 * logical memory.
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
enum class MemoryPersistenceRequestStatus {
    AVAILABLE,
    UNAVAILABLE,
    FAILED,
}
