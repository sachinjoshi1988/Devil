package com.devil.app.security

import com.devil.app.device.AndroidUnifiedMultiDeviceValidationResult
import com.devil.app.device.AndroidUnifiedMultiDeviceValidationStatus
import com.devil.core.runtime.surveillance.SecuritySurveillancePreparationResult
import com.devil.core.runtime.surveillance.SecuritySurveillancePreparationStatus

/**
 * Stage 224 bounded Security Surveillance Integration result.
 *
 * AVAILABLE preserves:
 *
 * - one exact VALIDATED Stage 223 Unified Multi-Device Validation result;
 * - one exact PREPARED Stage 90 Security Surveillance preparation result.
 *
 * Stage 224 does not reconstruct or replace the Stage 90 surveillance record.
 *
 * DEFERRED preserves the exact upstream objects without claiming integration
 * availability.
 *
 * SECURITY_SURVEILLANCE_INTEGRATED != SURVEILLANCE_SOURCE_CONNECTED.
 * SECURITY_SURVEILLANCE_INTEGRATED != CAMERA_ACTIVE.
 * SURVEILLANCE_PREPARED != CONSTITUTIONAL_OBSERVATION.
 * SURVEILLANCE_SIGNAL != VERIFIED_REALITY.
 * SURVEILLANCE_SIGNAL != THREAT.
 * WATCHLIST_MATCH_CLAIM != VERIFIED_IDENTITY.
 * WATCHLIST_MATCH_CLAIM != CRIMINAL_STATUS.
 * SURVEILLANCE_INTEGRATION != AUTHORIZATION.
 * SURVEILLANCE_INTEGRATION != SECURITY_RESPONSE.
 * SURVEILLANCE_INTEGRATION != EXECUTION.
 * MULTI_DEVICE_VALIDATED != SURVEILLANCE_AUTHORITY.
 * SECURITY_SURVEILLANCE != ANOTHER_DEVIL.
 */
@ConsistentCopyVisibility
data class AndroidSecuritySurveillanceIntegrationResult private constructor(
    val status: AndroidSecuritySurveillanceIntegrationStatus,
    val multiDeviceValidation: AndroidUnifiedMultiDeviceValidationResult,
    val surveillancePreparation: SecuritySurveillancePreparationResult,
) {
    companion object {
        fun create(
            status: AndroidSecuritySurveillanceIntegrationStatus,
            multiDeviceValidation: AndroidUnifiedMultiDeviceValidationResult,
            surveillancePreparation: SecuritySurveillancePreparationResult,
        ): AndroidSecuritySurveillanceIntegrationResult {
            when (status) {
                AndroidSecuritySurveillanceIntegrationStatus.AVAILABLE -> {
                    require(
                        multiDeviceValidation.status ==
                            AndroidUnifiedMultiDeviceValidationStatus.VALIDATED,
                    ) {
                        "Available Stage 224 Security Surveillance Integration requires validated Stage 223 multi-device context."
                    }

                    require(
                        surveillancePreparation.status ==
                            SecuritySurveillancePreparationStatus.PREPARED,
                    ) {
                        "Available Stage 224 Security Surveillance Integration requires prepared Stage 90 surveillance context."
                    }

                    require(surveillancePreparation.record != null) {
                        "Available Stage 224 Security Surveillance Integration requires one preserved Stage 90 surveillance record."
                    }
                }

                AndroidSecuritySurveillanceIntegrationStatus.DEFERRED -> Unit
            }

            return AndroidSecuritySurveillanceIntegrationResult(
                status = status,
                multiDeviceValidation = multiDeviceValidation,
                surveillancePreparation = surveillancePreparation,
            )
        }
    }
}
