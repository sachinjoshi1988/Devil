package com.devil.app.device

import com.devil.core.model.security.SessionRecord
import com.devil.core.runtime.security.SessionValidityResult
import com.devil.core.runtime.security.SessionValidityStatus

/**
 * Stage 219 bounded Cross-Device Session Governance result.
 *
 * AVAILABLE preserves one exact available Stage 218 Cross-Device Identity result,
 * one exact existing SessionRecord, and one exact VALID SessionValidityResult whose
 * session belongs to the exact Stage 218 identity.
 *
 * DEFERRED preserves the exact upstream objects without claiming cross-device
 * session governance availability.
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
@ConsistentCopyVisibility
data class AndroidCrossDeviceSessionGovernanceResult private constructor(
    val status: AndroidCrossDeviceSessionGovernanceStatus,
    val crossDeviceIdentity: AndroidCrossDeviceIdentityResult,
    val session: SessionRecord,
    val sessionValidity: SessionValidityResult,
) {
    companion object {
        fun create(
            status: AndroidCrossDeviceSessionGovernanceStatus,
            crossDeviceIdentity: AndroidCrossDeviceIdentityResult,
            session: SessionRecord,
            sessionValidity: SessionValidityResult,
        ): AndroidCrossDeviceSessionGovernanceResult {
            when (status) {
                AndroidCrossDeviceSessionGovernanceStatus.AVAILABLE -> {
                    require(
                        crossDeviceIdentity.status ==
                            AndroidCrossDeviceIdentityStatus.AVAILABLE,
                    ) {
                        "Available Stage 219 Cross-Device Session Governance requires available Stage 218 Cross-Device Identity."
                    }

                    require(
                        sessionValidity.status ==
                            SessionValidityStatus.VALID,
                    ) {
                        "Available Stage 219 Cross-Device Session Governance requires VALID session validity."
                    }

                    val validityRequest =
                        requireNotNull(sessionValidity.request) {
                            "Valid Stage 219 session governance requires one preserved session-validity request."
                        }

                    require(
                        validityRequest.session === session,
                    ) {
                        "Stage 219 session governance must preserve the exact session from the session-validity result."
                    }

                    val identityId =
                        requireNotNull(crossDeviceIdentity.identityId) {
                            "Available Stage 218 Cross-Device Identity requires one identity."
                        }

                    require(
                        session.subjectIdentityId == identityId,
                    ) {
                        "Stage 219 session subject identity must match the exact Stage 218 Cross-Device Identity."
                    }
                }

                AndroidCrossDeviceSessionGovernanceStatus.DEFERRED -> Unit
            }

            return AndroidCrossDeviceSessionGovernanceResult(
                status = status,
                crossDeviceIdentity = crossDeviceIdentity,
                session = session,
                sessionValidity = sessionValidity,
            )
        }
    }
}
