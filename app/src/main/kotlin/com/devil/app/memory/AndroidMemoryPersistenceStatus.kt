package com.devil.app.memory

/**
 * Describes the Android-embodiment result of one controlled logical-memory
 * persistence attempt.
 *
 * PERSISTED may be reported only by an explicitly authorized Android persistence
 * mechanism after genuine durable persistence occurred.
 *
 * DEFERRED means Android persistence was not attempted because no approved
 * mechanism or required policy is currently available.
 *
 * FAILED represents an operational persistence failure with one matching
 * constitutional trace error.
 *
 * Core MemoryPersistenceStatus.PERSISTABLE is not equivalent to PERSISTED.
 */
enum class AndroidMemoryPersistenceStatus {
    PERSISTED,
    DEFERRED,
    FAILED,
}
