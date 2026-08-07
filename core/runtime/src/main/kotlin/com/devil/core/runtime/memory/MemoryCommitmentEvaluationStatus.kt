package com.devil.core.runtime.memory

/**
 * Describes the bounded result of constitutional logical-memory commitment
 * evaluation.
 *
 * COMMITTABLE means genuine constitutional evidence established that one
 * bounded MemoryCommitmentRequest may proceed to a later controlled persistence
 * mechanism governed by the single Memory Authority.
 *
 * UNAVAILABLE means no justified logical-memory commitment can currently be
 * established.
 *
 * FAILED represents an operational evaluation failure.
 *
 * This status does not create, persist, store, expose, recall, or commit logical
 * memory. It does not assign memory class, sensitivity, confidence, retention
 * policy, source attribution, owner-visible reason, storage destination, or
 * deletion policy.
 *
 * It does not invoke storage, mutate world state, change task or plan state,
 * communicate externally, bypass the single Memory Authority, or produce a
 * runtime result.
 */
enum class MemoryCommitmentEvaluationStatus {
    COMMITTABLE,
    UNAVAILABLE,
    FAILED,
}
