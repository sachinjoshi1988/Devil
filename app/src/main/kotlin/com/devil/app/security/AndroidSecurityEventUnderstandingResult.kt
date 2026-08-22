package com.devil.app.security

import com.devil.core.model.surveillance.SecuritySurveillanceSignal

/**
 * Stage 226 bounded Security Event Understanding result.
 *
 * UNDERSTOOD preserves:
 *
 * - one exact AVAILABLE Stage 225 Camera / Network-Camera Adapter result;
 * - the exact Stage 90 SecuritySurveillanceSignal already preserved upstream;
 * - one normalized explicitly supplied bounded understanding description.
 *
 * DEFERRED preserves only the exact Stage 225 upstream result and must not contain
 * surveillance-signal or understanding metadata.
 *
 * This result does not establish that the supplied surveillance signal is true,
 * dangerous, criminal, suspicious, intrusive, or emergent.
 *
 * SECURITY_EVENT_UNDERSTOOD != VERIFIED_REALITY.
 * SECURITY_EVENT_UNDERSTOOD != CONSTITUTIONAL_OBSERVATION.
 * SECURITY_EVENT_UNDERSTOOD != CONSTITUTIONAL_VERIFICATION.
 * SECURITY_EVENT_UNDERSTOOD != THREAT.
 * SECURITY_EVENT_UNDERSTOOD != INTRUSION.
 * SECURITY_EVENT_UNDERSTOOD != EMERGENCY.
 * SECURITY_EVENT_UNDERSTOOD != AUTHORIZATION.
 * SECURITY_EVENT_UNDERSTOOD != SECURITY_RESPONSE.
 * SECURITY_EVENT_UNDERSTOOD != EXECUTION.
 * SURVEILLANCE_SIGNAL != VERIFIED_EVENT.
 * WATCHLIST_MATCH_CLAIM != VERIFIED_IDENTITY.
 * WATCHLIST_MATCH_CLAIM != CRIMINAL_STATUS.
 */
@ConsistentCopyVisibility
data class AndroidSecurityEventUnderstandingResult private constructor(
    val status: AndroidSecurityEventUnderstandingStatus,
    val cameraAdapter: AndroidSecurityCameraAdapterResult,
    val signal: SecuritySurveillanceSignal?,
    val understandingDescription: String?,
) {
    companion object {
        fun create(
            status: AndroidSecurityEventUnderstandingStatus,
            cameraAdapter: AndroidSecurityCameraAdapterResult,
            signal: SecuritySurveillanceSignal? = null,
            understandingDescription: String? = null,
        ): AndroidSecurityEventUnderstandingResult {
            return when (status) {
                AndroidSecurityEventUnderstandingStatus.UNDERSTOOD -> {
                    require(
                        cameraAdapter.status ==
                            AndroidSecurityCameraAdapterStatus.AVAILABLE,
                    ) {
                        "Understood Stage 226 Security Event requires available Stage 225 Camera Adapter."
                    }

                    val surveillanceRecord =
                        requireNotNull(
                            cameraAdapter
                                .surveillanceIntegration
                                .surveillancePreparation
                                .record,
                        ) {
                            "Understood Stage 226 Security Event requires one preserved Stage 90 surveillance record."
                        }

                    val suppliedSignal =
                        requireNotNull(signal) {
                            "Understood Stage 226 Security Event requires one preserved Stage 90 surveillance signal."
                        }

                    require(suppliedSignal === surveillanceRecord.signal) {
                        "Stage 226 Security Event Understanding must preserve the exact Stage 90 surveillance-signal provenance."
                    }

                    val normalizedDescription =
                        requireNotNull(understandingDescription)
                            .trim()

                    require(normalizedDescription.isNotEmpty()) {
                        "Stage 226 Security Event understanding description must not be blank."
                    }

                    AndroidSecurityEventUnderstandingResult(
                        status = status,
                        cameraAdapter = cameraAdapter,
                        signal = suppliedSignal,
                        understandingDescription = normalizedDescription,
                    )
                }

                AndroidSecurityEventUnderstandingStatus.DEFERRED -> {
                    require(signal == null) {
                        "Deferred Stage 226 Security Event Understanding must not contain a surveillance signal."
                    }

                    require(understandingDescription == null) {
                        "Deferred Stage 226 Security Event Understanding must not contain understanding metadata."
                    }

                    AndroidSecurityEventUnderstandingResult(
                        status = status,
                        cameraAdapter = cameraAdapter,
                        signal = null,
                        understandingDescription = null,
                    )
                }
            }
        }
    }
}
