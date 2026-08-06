package com.devil.core.runtime.learning

/**
 * Describes the stable operational result of constitutional learning evaluation.
 *
 * LEARNABLE means genuine constitutional evidence established that one bounded
 * learning proposal may be produced.
 *
 * LEARNABLE does not create learning, create or commit memory, mutate world
 * state, change task or plan state, communicate externally, or bypass unified
 * runtime handling.
 *
 * DEFERRED means no justified learning proposal is currently available.
 * FAILED represents an operational failure with one matching error.
 */
enum class LearningStatus {
    LEARNABLE,
    DEFERRED,
    FAILED,
}
