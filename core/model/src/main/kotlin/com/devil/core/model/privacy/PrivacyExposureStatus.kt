package com.devil.core.model.privacy

/**
 * Stage 46 result status for bounded privacy exposure evaluation.
 *
 * ALLOWED means the supplied privacy policy does not block the proposed
 * exposure.
 *
 * RESTRICTED means the exposure requires a stronger privacy context than was
 * supplied and must not proceed yet.
 *
 * BLOCKED means the proposed exposure is prohibited by the supplied privacy
 * policy.
 *
 * UNAVAILABLE means the policy cannot safely establish an exposure decision.
 *
 * ALLOWED
 * != Devil authorization
 * != Android permission
 * != execution approval
 * != successful disclosure.
 */
enum class PrivacyExposureStatus {
    ALLOWED,
    RESTRICTED,
    BLOCKED,
    UNAVAILABLE,
}
