package com.devil.core.model.identity

/**
 * Identifies the origin of one identity-related observation.
 *
 * A source describes provenance only. It does not establish authenticity,
 * reliability, ownership, trust, or authorization.
 */
enum class IdentityEvidenceSource {
    DECLARED,
    DEVICE,
    VOICE,
    BIOMETRIC,
    CREDENTIAL,
    SESSION,
    TEST,
}
