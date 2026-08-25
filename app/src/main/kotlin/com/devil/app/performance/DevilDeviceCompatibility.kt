package com.devil.app.performance

import com.devil.app.device.AndroidDeviceKnowledgeSnapshot

/**
 * Stage 273 Device Compatibility.
 *
 * This bounded contract evaluates explicitly supplied Android compatibility evidence
 * while preserving the exact existing Stage 40 AndroidDeviceKnowledgeSnapshot as
 * authoritative upstream device provenance.
 *
 * Stage 40 remains authoritative for observed Android device facts.
 *
 * DEVICE_COMPATIBLE != PRODUCTION_VALIDATED.
 * DEVICE_COMPATIBLE != REDMI_NOTE_12_VALIDATED.
 * DEVICE_COMPATIBLE != HARDWARE_FEATURE_GUARANTEED.
 * DEVICE_COMPATIBLE != ANDROID_PERMISSION_GRANTED.
 * DEVICE_COMPATIBLE != DEVIL_AUTHORIZATION.
 * DEVICE_COMPATIBLE != EXECUTION_APPROVAL.
 * DEVICE_COMPATIBLE != VERIFIED_OUTCOME.
 * DEVICE_KNOWLEDGE != DEVICE_COMPATIBILITY.
 *
 * Stage 273 does not modify Android configuration, request permissions, probe hardware,
 * execute capabilities, alter build configuration, change manifest requirements,
 * establish constitutional Observation, Verification, or Outcome, or implement
 * Stage 274 Redmi Note 12 Production Validation.
 */
enum class DevilDeviceCompatibilityStatus {
    COMPATIBLE,
    NOT_COMPATIBLE,
}

/**
 * Explicitly supplied Stage 273 compatibility evidence.
 *
 * The exact Stage 40 device snapshot is retained unchanged.
 *
 * minimumSupportedSdk and targetSdk describe the application compatibility envelope
 * already established by build configuration. Required-capability flags are supplied
 * evidence only and do not themselves probe hardware, grant Android permission,
 * authorize execution, or prove production behavior.
 */
data class DevilDeviceCompatibilityEvidence(
    val deviceSnapshot: AndroidDeviceKnowledgeSnapshot,
    val minimumSupportedSdk: Int,
    val targetSdk: Int,
    val requiredAudioCapabilityCompatible: Boolean,
    val requiredCameraCapabilityCompatible: Boolean,
    val requiredInternetCapabilityCompatible: Boolean,
) {
    init {
        require(minimumSupportedSdk > 0) {
            "Stage 273 minimum supported SDK must be positive."
        }

        require(targetSdk >= minimumSupportedSdk) {
            "Stage 273 target SDK must not be below the minimum supported SDK."
        }
    }

    fun isCompatible(): Boolean =
        deviceSnapshot.sdkInt >= minimumSupportedSdk &&
            requiredAudioCapabilityCompatible &&
            requiredCameraCapabilityCompatible &&
            requiredInternetCapabilityCompatible
}

/**
 * Bounded Stage 273 Device Compatibility result.
 *
 * COMPATIBLE means only that the explicitly supplied compatibility evidence satisfies
 * the bounded Stage 273 prerequisites.
 *
 * It does not mean the device has completed production validation, that every Android
 * feature will work, that permissions are granted, or that any Devil action is
 * authorized or verified.
 */
@ConsistentCopyVisibility
data class DevilDeviceCompatibilityResult private constructor(
    val status: DevilDeviceCompatibilityStatus,
    val evidence: DevilDeviceCompatibilityEvidence,
) {
    companion object {
        fun create(
            evidence: DevilDeviceCompatibilityEvidence,
        ): DevilDeviceCompatibilityResult =
            DevilDeviceCompatibilityResult(
                status =
                    if (evidence.isCompatible()) {
                        DevilDeviceCompatibilityStatus.COMPATIBLE
                    } else {
                        DevilDeviceCompatibilityStatus.NOT_COMPATIBLE
                    },
                evidence = evidence,
            )
    }
}

/**
 * Stage 273 bounded Device Compatibility coordinator.
 *
 * It evaluates explicitly supplied evidence only and preserves the exact Stage 40
 * AndroidDeviceKnowledgeSnapshot carried by that evidence.
 *
 * It does not:
 *
 * - read Android Build fields;
 * - inspect hardware or configuration;
 * - request or grant Android permissions;
 * - alter Gradle or manifest configuration;
 * - execute Android capabilities;
 * - create device trust or authorization;
 * - establish constitutional Observation, Verification, or Outcome;
 * - perform Stage 274 Redmi Note 12 Production Validation.
 */
class DevilDeviceCompatibilityCoordinator {
    fun evaluate(
        evidence: DevilDeviceCompatibilityEvidence,
    ): DevilDeviceCompatibilityResult =
        DevilDeviceCompatibilityResult.create(
            evidence = evidence,
        )
}
