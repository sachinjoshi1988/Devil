package com.devil.app.security

import com.devil.core.model.surveillance.SecuritySurveillanceSource

/**
 * Stage 225 bounded Camera / Network-Camera Adapter result.
 *
 * AVAILABLE preserves:
 *
 * - one exact AVAILABLE Stage 224 Security Surveillance Integration result;
 * - the exact Stage 90 SecuritySurveillanceSource already preserved by that result;
 * - one normalized explicitly supplied bounded adapter identifier.
 *
 * DEFERRED preserves the exact Stage 224 integration without claiming a
 * surveillance source or adapter is available.
 *
 * CAMERA_ADAPTER_AVAILABLE != CAMERA_CONNECTED.
 * CAMERA_ADAPTER_AVAILABLE != CAMERA_ACTIVE.
 * CAMERA_ADAPTER_AVAILABLE != STREAM_AVAILABLE.
 * ADAPTER_ID != PLATFORM_IMPLEMENTATION.
 * SURVEILLANCE_SOURCE != CAMERA_AVAILABILITY.
 * SURVEILLANCE_SOURCE_TYPE != VERIFIED_HARDWARE_TYPE.
 * NETWORK_CAMERA_ADAPTER != NETWORK_REACHABILITY.
 * NETWORK_CAMERA_ADAPTER != RTSP_SESSION.
 * NETWORK_CAMERA_ADAPTER != ONVIF_SESSION.
 * CAMERA_ADAPTER != AUTHORIZATION.
 * CAMERA_ADAPTER != OBSERVATION.
 * CAMERA_ADAPTER != SECURITY_EVENT.
 * CAMERA_ADAPTER != EXECUTION.
 */
@ConsistentCopyVisibility
data class AndroidSecurityCameraAdapterResult private constructor(
    val status: AndroidSecurityCameraAdapterStatus,
    val surveillanceIntegration: AndroidSecuritySurveillanceIntegrationResult,
    val source: SecuritySurveillanceSource?,
    val adapterId: String?,
) {
    companion object {
        fun create(
            status: AndroidSecurityCameraAdapterStatus,
            surveillanceIntegration: AndroidSecuritySurveillanceIntegrationResult,
            source: SecuritySurveillanceSource? = null,
            adapterId: String? = null,
        ): AndroidSecurityCameraAdapterResult {
            return when (status) {
                AndroidSecurityCameraAdapterStatus.AVAILABLE -> {
                    require(
                        surveillanceIntegration.status ==
                            AndroidSecuritySurveillanceIntegrationStatus.AVAILABLE,
                    ) {
                        "Available Stage 225 Camera Adapter requires available Stage 224 Security Surveillance Integration."
                    }

                    val record =
                        requireNotNull(
                            surveillanceIntegration
                                .surveillancePreparation
                                .record,
                        ) {
                            "Available Stage 225 Camera Adapter requires one preserved Stage 90 surveillance record."
                        }

                    val suppliedSource =
                        requireNotNull(source) {
                            "Available Stage 225 Camera Adapter requires one preserved Stage 90 surveillance source."
                        }

                    require(suppliedSource === record.source) {
                        "Stage 225 Camera Adapter must preserve the exact Stage 90 surveillance source provenance."
                    }

                    val normalizedAdapterId =
                        requireNotNull(adapterId)
                            .trim()

                    require(normalizedAdapterId.isNotEmpty()) {
                        "Stage 225 camera adapter identifier must not be blank."
                    }

                    AndroidSecurityCameraAdapterResult(
                        status = status,
                        surveillanceIntegration = surveillanceIntegration,
                        source = suppliedSource,
                        adapterId = normalizedAdapterId,
                    )
                }

                AndroidSecurityCameraAdapterStatus.DEFERRED -> {
                    require(source == null) {
                        "Deferred Stage 225 Camera Adapter must not contain a surveillance source."
                    }

                    require(adapterId == null) {
                        "Deferred Stage 225 Camera Adapter must not contain an adapter identifier."
                    }

                    AndroidSecurityCameraAdapterResult(
                        status = status,
                        surveillanceIntegration = surveillanceIntegration,
                        source = null,
                        adapterId = null,
                    )
                }
            }
        }
    }
}
