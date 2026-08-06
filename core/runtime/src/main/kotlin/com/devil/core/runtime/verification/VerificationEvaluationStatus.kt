package com.devil.core.runtime.verification

/**
 * Describes the bounded result of constitutional verification evaluation.
 *
 * VERIFIED means genuine verification evidence established that the bounded
 * observation satisfies approved constitutional verification requirements.
 *
 * UNAVAILABLE means no justified verification could currently be established.
 * FAILED represents an operational verification-evaluation failure.
 *
 * This status does not update world state, report final success or failure,
 * change task or plan state, or produce a final outcome.
 */
enum class VerificationEvaluationStatus {
    VERIFIED,
    UNAVAILABLE,
    FAILED,
}
