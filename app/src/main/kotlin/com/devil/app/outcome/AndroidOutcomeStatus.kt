package com.devil.app.outcome

/**
 * Describes one bounded Android outcome determination.
 *
 * ESTABLISHED means an approved Android outcome mechanism produced genuine
 * evidence supporting one bounded outcome determination after genuine
 * verification.
 *
 * ESTABLISHED does not mean that the task or plan completed, world state was
 * updated, learning occurred, or memory was created.
 *
 * DEFERRED means no justified Android outcome determination was produced.
 *
 * FAILED means the bounded Android outcome path failed operationally with one
 * matching error.
 *
 * Attempted != Observed != Verified != Outcome Established != Completed.
 */
enum class AndroidOutcomeStatus {
    ESTABLISHED,
    DEFERRED,
    FAILED,
}
