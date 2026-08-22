package com.devil.app.device

/**
 * Stage 222 bounded Device Trust & Revocation result.
 *
 * TRUSTED preserves one exact AVAILABLE Stage 221 Cross-Device Memory
 * Continuity result while representing an explicitly supplied trusted state.
 *
 * REVOKED preserves one exact AVAILABLE Stage 221 Cross-Device Memory
 * Continuity result while representing an explicitly supplied revoked state.
 *
 * DEFERRED preserves the exact upstream Stage 221 result without claiming
 * either trust or revocation.
 *
 * This result does not authenticate a device or subject, grant or deny
 * constitutional authorization, terminate sessions, revoke credentials,
 * delete memory, block networking, wipe a device, execute capabilities,
 * or establish Observation, Verification, or Outcome.
 *
 * DEVICE_IDENTITY != DEVICE_TRUST.
 * DEVICE_TRUST != AUTHENTICATION.
 * DEVICE_TRUST != AUTHORIZATION.
 * TRUSTED != EXECUTION_AUTHORITY.
 * REVOKED != SESSION_TERMINATION.
 * REVOKED != CREDENTIAL_REVOCATION.
 * REVOKED != MEMORY_DELETION.
 * REVOCATION_STATE != REVOCATION_EXECUTION.
 */
@ConsistentCopyVisibility
data class AndroidDeviceTrustRevocationResult private constructor(
    val status: AndroidDeviceTrustRevocationStatus,
    val memoryContinuity: AndroidCrossDeviceMemoryContinuityResult,
) {
    companion object {
        fun create(
            status: AndroidDeviceTrustRevocationStatus,
            memoryContinuity: AndroidCrossDeviceMemoryContinuityResult,
        ): AndroidDeviceTrustRevocationResult {
            when (status) {
                AndroidDeviceTrustRevocationStatus.TRUSTED,
                AndroidDeviceTrustRevocationStatus.REVOKED,
                -> {
                    require(
                        memoryContinuity.status ==
                            AndroidCrossDeviceMemoryContinuityStatus.AVAILABLE,
                    ) {
                        "Stage 222 trusted or revoked state requires available Stage 221 Cross-Device Memory Continuity."
                    }
                }

                AndroidDeviceTrustRevocationStatus.DEFERRED -> Unit
            }

            return AndroidDeviceTrustRevocationResult(
                status = status,
                memoryContinuity = memoryContinuity,
            )
        }
    }
}
