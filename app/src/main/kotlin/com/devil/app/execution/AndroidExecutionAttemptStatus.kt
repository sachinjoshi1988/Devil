package com.devil.app.execution

/**
 * Describes the Android embodiment result of one bounded execution attempt.
 *
 * ATTEMPTED means an authorized execution performer genuinely attempted the
 * bounded platform action.
 *
 * ATTEMPTED does not mean that the intended effect occurred, that the action
 * succeeded, or that an outcome was verified. Those responsibilities belong to
 * later Observation and Verification stages.
 *
 * DEFERRED means no justified Android execution attempt was made.
 *
 * FAILED means the bounded Android execution path failed operationally with one
 * matching error.
 */
enum class AndroidExecutionAttemptStatus {
    ATTEMPTED,
    DEFERRED,
    FAILED,
}
