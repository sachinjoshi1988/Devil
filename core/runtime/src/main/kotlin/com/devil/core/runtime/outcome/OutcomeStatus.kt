package com.devil.core.runtime.outcome

/**
 * Describes the stable operational result of constitutional outcome evaluation.
 *
 * ESTABLISHED means genuine constitutional outcome evidence established one
 * bounded outcome determination.
 *
 * ESTABLISHED does not update world state, change task or plan state, create
 * memory or learning, communicate externally, or bypass unified runtime
 * handling.
 *
 * DEFERRED means no justified outcome determination is currently available.
 * FAILED represents an operational failure with one matching error.
 */
enum class OutcomeStatus {
    ESTABLISHED,
    DEFERRED,
    FAILED,
}
