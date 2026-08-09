package com.devil.app.observation

/**
 * Describes one bounded Android execution-observation result.
 *
 * OBSERVED means an approved Android observation mechanism produced genuine
 * evidence after a genuine Stage 30 Android execution attempt.
 *
 * OBSERVED does not mean that the intended outcome was verified, that the task
 * succeeded, that world state was updated, or that execution completed.
 *
 * DEFERRED means no justified Android observation was produced.
 *
 * FAILED means the bounded Android observation path failed operationally with
 * one matching error.
 *
 * Attempted != Observed != Verified != Completed.
 */
enum class AndroidObservationStatus {
    OBSERVED,
    DEFERRED,
    FAILED,
}
