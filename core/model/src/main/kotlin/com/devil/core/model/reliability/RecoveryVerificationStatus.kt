package com.devil.core.model.reliability

/**
 * Stage 45 bounded post-attempt recovery-verification status.
 *
 * VERIFIED_RECOVERED means explicit post-attempt reliability evidence is HEALTHY.
 *
 * NOT_RECOVERED means explicit post-attempt evidence still represents a degraded,
 * unavailable, or failed reliability condition.
 *
 * UNAVAILABLE means no valid recovery verification can be established from the
 * supplied inputs.
 *
 * This is reliability-domain verification only.
 *
 * VERIFIED_RECOVERED
 * != constitutional Verification success
 * != verified Outcome
 * != capability-health mutation
 * != capability availability mutation
 * != authorization
 * != Executive readiness
 * != Android permission
 * != execution approval.
 */
enum class RecoveryVerificationStatus {
    VERIFIED_RECOVERED,
    NOT_RECOVERED,
    UNAVAILABLE,
}
