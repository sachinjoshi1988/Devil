package com.devil.core.runtime.memory

/**
 * Describes the bounded result of constitutional logical-memory persistence
 * evaluation.
 *
 * PERSISTABLE means genuine constitutional evidence established that one
 * bounded MemoryPersistenceRequest may proceed to a later explicitly
 * authorized persistence mechanism governed by the single Memory Authority.
 *
 * UNAVAILABLE means no justified logical-memory persistence can currently be
 * established.
 *
 * FAILED represents an operational persistence-evaluation failure.
 *
 * This status does not create, persist, store, expose, recall, delete, or
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
enum class MemoryPersistenceEvaluationStatus {
    PERSISTABLE,
    UNAVAILABLE,
    FAILED,
}
