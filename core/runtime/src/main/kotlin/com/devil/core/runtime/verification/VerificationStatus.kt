package com.devil.core.runtime.verification

/**
 * Describes the stable operational result of constitutional verification.
 *
 * VERIFIED means genuine verification evidence established that one bounded
 * VerificationRequest satisfied approved constitutional verification
 * requirements.
 *
 * VERIFIED does not update world state, report final task success, change task
 * or plan state, or produce a final Outcome.
 *
 * DEFERRED means no justified verification evidence is currently available.
 * FAILED represents an operational failure with one matching error.
 */
enum class VerificationStatus {
    VERIFIED,
    DEFERRED,
    FAILED,
}
