package com.devil.app.device

/**
 * Stage 222 bounded Device Trust & Revocation status.
 *
 * TRUSTED and REVOKED are explicitly supplied governance representations.
 * DEFERRED means Stage 222 does not claim either trust or revocation.
 *
 * DEVICE_TRUST != AUTHENTICATION.
 * DEVICE_TRUST != AUTHORIZATION.
 * TRUSTED != EXECUTION_AUTHORITY.
 * REVOKED != SESSION_TERMINATION.
 * REVOKED != CREDENTIAL_REVOCATION.
 * REVOKED != MEMORY_DELETION.
 * REVOCATION_STATE != REVOCATION_EXECUTION.
 */
enum class AndroidDeviceTrustRevocationStatus {
    TRUSTED,
    REVOKED,
    DEFERRED,
}
