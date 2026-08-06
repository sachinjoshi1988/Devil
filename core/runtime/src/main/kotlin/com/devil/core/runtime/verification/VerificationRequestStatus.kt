package com.devil.core.runtime.verification

/**
 * Describes whether one bounded constitutional verification request is available.
 *
 * This status reports request availability only. It does not establish
 * verification evidence, determine success, update world state, or produce a
 * final outcome.
 */
enum class VerificationRequestStatus {
    AVAILABLE,
    UNAVAILABLE,
    FAILED,
}
