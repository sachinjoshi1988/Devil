package com.devil.core.runtime.memory

/**
 * Describes the stable operational result of constitutional logical-memory
 * persistence evaluation.
 *
 * PERSISTABLE means genuine constitutional evidence established that one
 * bounded MemoryPersistenceRequest may proceed to a later explicitly
 * authorized persistence mechanism governed by the single Memory Authority.
 *
 * PERSISTABLE does not create, persist, store, expose, recall, delete, or
 * commit logical memory.
 *
 * It does not assign or alter memory class, sensitivity, confidence, retention
 * policy, source attribution, owner-visible reason, storage destination,
 * deletion policy, encryption policy, replication policy, or other
 * logical-memory metadata.
 *
 * DEFERRED means no justified logical-memory persistence is currently
 * available.
 *
 * FAILED represents an operational failure with one matching error.
 */
enum class MemoryPersistenceStatus {
    PERSISTABLE,
    DEFERRED,
    FAILED,
}
