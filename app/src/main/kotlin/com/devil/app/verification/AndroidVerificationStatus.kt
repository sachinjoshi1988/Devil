package com.devil.app.verification

/**
 * Describes one bounded Android verification result.
 *
 * VERIFIED means an approved Android verification mechanism produced genuine
 * evidence that independently verified the intended effect represented by one
 * genuine Stage 31 Android observation.
 *
 * VERIFIED does not mean that the task completed, the plan completed, world
 * state was updated, or a final Outcome was established.
 *
 * DEFERRED means no justified Android verification was produced.
 *
 * FAILED means the bounded Android verification path failed operationally with
 * one matching error.
 *
 * Attempted != Observed != Verified != Completed.
 */
enum class AndroidVerificationStatus {
    VERIFIED,
    DEFERRED,
    FAILED,
}
