package com.devil.core.runtime.outcome

/**
 * Describes the bounded result of constitutional outcome evaluation.
 *
 * ESTABLISHED means genuine constitutional outcome evidence established one
 * bounded outcome determination.
 *
 * UNAVAILABLE means no justified outcome determination could currently be
 * established.
 *
 * FAILED represents an operational outcome-evaluation failure.
 *
 * This status does not update world state, change task or plan state, create
 * memory or learning, communicate an outcome, or produce the final runtime
 * result.
 */
enum class OutcomeEvaluationStatus {
    ESTABLISHED,
    UNAVAILABLE,
    FAILED,
}
