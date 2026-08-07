package com.devil.core.runtime.memory

/**
 * Describes the stable operational result of constitutional logical-memory
 * commitment evaluation.
 *
 * COMMITTABLE means genuine constitutional evidence established that one
 * bounded MemoryCommitmentRequest may proceed to a later controlled persistence
 * mechanism governed by the single Memory Authority.
 *
 * COMMITTABLE does not create, persist, store, expose, recall, or commit logical
 * memory. It does not assign memory class, sensitivity, confidence, retention
 * policy, source attribution, owner-visible reason, storage destination, or
 * deletion policy.
 *
 * DEFERRED means no justified logical-memory commitment is currently available.
 *
 * FAILED represents an operational failure with one matching error.
 */
enum class MemoryCommitmentStatus {
    COMMITTABLE,
    DEFERRED,
    FAILED,
}
