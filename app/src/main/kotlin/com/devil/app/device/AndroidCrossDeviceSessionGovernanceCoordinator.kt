package com.devil.app.device

import com.devil.core.model.security.SessionRecord
import com.devil.core.runtime.security.SessionValidityResult
import com.devil.core.runtime.security.SessionValidityStatus

/**
 * Stage 219 bounded Cross-Device Session Governance coordinator.
 *
 * It evaluates whether one exact Stage 218 Cross-Device Identity context and one
 * exact existing session-validity result can be represented together without
 * creating or transferring a security session.
 *
 * It does not:
 *
 * - create a session;
 * - copy or transfer a session between devices;
 * - renew, extend, revoke, or mutate a session;
 * - replicate credentials or tokens;
 * - authenticate a subject or device;
 * - establish device trust;
 * - grant authorization;
 * - establish remote execution authority;
 * - synchronize Conversation, World Model, or Memory state;
 * - establish Observation, Verification, or Outcome;
 * - implement Stage 220 Cross-Device Task Continuity.
 *
 * CROSS_DEVICE_SESSION_GOVERNANCE != SESSION_CREATION.
 * CROSS_DEVICE_SESSION_GOVERNANCE != SESSION_TRANSFER.
 * CROSS_DEVICE_SESSION_GOVERNANCE != SESSION_RENEWAL.
 * CROSS_DEVICE_SESSION_GOVERNANCE != SESSION_REPLICATION.
 * SESSION_VALID != AUTHENTICATION.
 * SESSION_VALID != AUTHORIZATION.
 * IDENTITY_MATCH != AUTHENTICATION.
 * CROSS_DEVICE_IDENTITY != DEVICE_TRUST.
 * SESSION_CONTEXT != REMOTE_EXECUTION_AUTHORITY.
 * SESSION_CONTINUITY != MEMORY_SYNC.
 */
class AndroidCrossDeviceSessionGovernanceCoordinator {

    fun govern(
        crossDeviceIdentity: AndroidCrossDeviceIdentityResult,
        session: SessionRecord,
        sessionValidity: SessionValidityResult,
    ): AndroidCrossDeviceSessionGovernanceResult {
        val validRequest =
            if (sessionValidity.status == SessionValidityStatus.VALID) {
                sessionValidity.request
            } else {
                null
            }

        val identityMatches =
            crossDeviceIdentity.identityId != null &&
                session.subjectIdentityId ==
                crossDeviceIdentity.identityId

        val preservesExactSession =
            validRequest?.session === session

        val status =
            if (
                crossDeviceIdentity.status ==
                    AndroidCrossDeviceIdentityStatus.AVAILABLE &&
                sessionValidity.status ==
                    SessionValidityStatus.VALID &&
                preservesExactSession &&
                identityMatches
            ) {
                AndroidCrossDeviceSessionGovernanceStatus.AVAILABLE
            } else {
                AndroidCrossDeviceSessionGovernanceStatus.DEFERRED
            }

        return AndroidCrossDeviceSessionGovernanceResult.create(
            status = status,
            crossDeviceIdentity = crossDeviceIdentity,
            session = session,
            sessionValidity = sessionValidity,
        )
    }
}
