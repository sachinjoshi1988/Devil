package com.devil.app.security

/**
 * Stage 225 bounded Camera / Network-Camera Adapter coordinator.
 *
 * It binds one exact Stage 224 Security Surveillance Integration result
 * to the exact Stage 90 surveillance source already preserved by that result
 * plus one explicitly supplied bounded adapter identifier.
 *
 * It does not infer hardware type from sourceType.
 *
 * It does not:
 *
 * - create or replace SecuritySurveillanceSource;
 * - create or mutate SecuritySurveillanceRecord;
 * - create AndroidCameraDescriptor;
 * - duplicate Stage 190 Camera Capability;
 * - open Android cameras;
 * - connect to CCTV, IP cameras, or network cameras;
 * - establish network reachability;
 * - store network addresses or credentials;
 * - implement RTSP or ONVIF;
 * - establish or consume a live stream;
 * - capture image or video bytes;
 * - create surveillance signals;
 * - analyze pixels;
 * - perform face recognition or biometric comparison;
 * - establish source trust;
 * - authenticate a subject or device;
 * - grant constitutional authorization;
 * - classify threat, intrusion, or emergency;
 * - establish constitutional Observation, Verification, or Outcome;
 * - prepare or execute Security Response;
 * - create, commit, or persist Memory;
 * - execute local or remote capabilities;
 * - implement Stage 226 Security Event Understanding.
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
class AndroidSecurityCameraAdapterCoordinator {

    fun integrate(
        surveillanceIntegration: AndroidSecuritySurveillanceIntegrationResult,
        adapterId: String?,
    ): AndroidSecurityCameraAdapterResult {
        val record =
            surveillanceIntegration
                .surveillancePreparation
                .record

        if (
            surveillanceIntegration.status !=
                AndroidSecuritySurveillanceIntegrationStatus.AVAILABLE ||
            record == null ||
            adapterId.isNullOrBlank()
        ) {
            return AndroidSecurityCameraAdapterResult.create(
                status = AndroidSecurityCameraAdapterStatus.DEFERRED,
                surveillanceIntegration = surveillanceIntegration,
            )
        }

        return AndroidSecurityCameraAdapterResult.create(
            status = AndroidSecurityCameraAdapterStatus.AVAILABLE,
            surveillanceIntegration = surveillanceIntegration,
            source = record.source,
            adapterId = adapterId,
        )
    }
}
