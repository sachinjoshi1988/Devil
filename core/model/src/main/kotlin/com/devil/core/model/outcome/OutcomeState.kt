package com.devil.core.model.outcome

/**
 * Describes the verified constitutional result of a task.
 *
 * This state does not describe task progress or execution activity. It reports
 * only what has been established through observation and verification.
 */
enum class OutcomeState {
    VERIFIED_SUCCESS,
    VERIFIED_FAILURE,
    PARTIAL_SUCCESS,
    INCONCLUSIVE,
}
