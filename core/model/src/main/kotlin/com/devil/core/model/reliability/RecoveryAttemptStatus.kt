package com.devil.core.model.reliability

/**
 * Stage 45 bounded state of one recovery-attempt accounting operation.
 *
 * RECORDED means exactly one attempt has been accounted for against the supplied
 * finite recovery budget.
 *
 * EXHAUSTED means no further attempt may be accounted for because the finite
 * budget has no remaining attempts.
 *
 * UNAVAILABLE means no valid recovery request was supplied for accounting.
 *
 * RECORDED
 * != recovery executed
 * != retry authorized
 * != recovery successful
 * != capability READY
 * != verified Outcome.
 */
enum class RecoveryAttemptStatus {
    RECORDED,
    EXHAUSTED,
    UNAVAILABLE,
}
