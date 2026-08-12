package com.devil.core.runtime.execution

/**
 * Describes whether one constitutionally approved execution request was genuinely
 * attempted by an authorized execution embodiment.
 *
 * ATTEMPTED means a bounded execution implementation genuinely attempted the
 * preserved ExecutionRequest.
 *
 * ATTEMPTED does not mean that:
 *
 * - the intended effect occurred;
 * - the effect was observed;
 * - the effect was verified;
 * - an Outcome was established;
 * - the task completed;
 * - or the plan completed.
 *
 * DEFERRED means no justified execution attempt occurred.
 *
 * FAILED represents an operational execution-attempt failure with one matching
 * error.
 *
 * Execution APPROVED != ATTEMPTED.
 * ATTEMPTED != OBSERVED.
 * OBSERVED != VERIFIED.
 * VERIFIED != OUTCOME.
 */
enum class ExecutionAttemptStatus {
    ATTEMPTED,
    DEFERRED,
    FAILED,
}
