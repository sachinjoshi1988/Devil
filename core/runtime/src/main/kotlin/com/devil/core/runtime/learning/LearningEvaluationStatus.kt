package com.devil.core.runtime.learning

/**
 * Describes the bounded result of constitutional learning evaluation.
 *
 * LEARNABLE means genuine constitutional learning evidence established that one
 * bounded learning proposal may be produced.
 *
 * UNAVAILABLE means no justified learning proposal can currently be established.
 *
 * FAILED represents an operational learning-evaluation failure.
 *
 * This status does not create learning, create or commit memory, mutate world
 * state, change task or plan state, communicate externally, or produce a
 * runtime result.
 */
enum class LearningEvaluationStatus {
    LEARNABLE,
    UNAVAILABLE,
    FAILED,
}
