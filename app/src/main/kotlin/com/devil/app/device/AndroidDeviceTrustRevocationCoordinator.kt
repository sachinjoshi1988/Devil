package com.devil.app.device

/**
 * Stage 222 bounded Device Trust & Revocation coordinator.
 *
 * It associates one exact Stage 221 Cross-Device Memory Continuity result
 * with one explicitly supplied trust/revocation disposition.
 *
 * It does not infer trust from identity, protocol participation, sessions,
 * tasks, memory continuity, Android permission, ownership, authentication,
 * or prior communication.
 *
 * It does not:
 *
 * - authenticate a subject or device;
 * - prove ownership;
 * - grant or deny constitutional authorization;
 * - create, renew, terminate, or revoke a session;
 * - create, rotate, or revoke credentials or tokens;
 * - delete, mutate, synchronize, replicate, or persist memory;
 * - block networking or communication;
 * - wipe or control a device;
 * - create an ExecutionRequest;
 * - execute local or remote capabilities;
 * - establish Observation, Verification, or Outcome;
 * - implement Stage 223 Unified Multi-Device Validation;
 * - implement Stage 224 Security Surveillance Integration.
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
class AndroidDeviceTrustRevocationCoordinator {

    fun integrate(
        memoryContinuity: AndroidCrossDeviceMemoryContinuityResult,
        disposition: AndroidDeviceTrustRevocationStatus?,
    ): AndroidDeviceTrustRevocationResult {
        val status =
            if (
                memoryContinuity.status ==
                    AndroidCrossDeviceMemoryContinuityStatus.AVAILABLE &&
                disposition != null &&
                disposition != AndroidDeviceTrustRevocationStatus.DEFERRED
            ) {
                disposition
            } else {
                AndroidDeviceTrustRevocationStatus.DEFERRED
            }

        return AndroidDeviceTrustRevocationResult.create(
            status = status,
            memoryContinuity = memoryContinuity,
        )
    }
}
