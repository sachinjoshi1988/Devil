package com.devil.app.device

import com.devil.app.device.pc.AndroidPcCapabilityAdapterResult
import com.devil.app.device.pc.AndroidPcCapabilityAdapterStatus
import com.devil.app.device.pc.AndroidPcEmbodimentResult
import com.devil.app.device.pc.AndroidPcEmbodimentStatus
import com.devil.app.device.tablet.AndroidEducationTabletExperienceResult
import com.devil.app.device.tablet.AndroidEducationTabletExperienceStatus
import com.devil.app.device.tablet.AndroidTabletEmbodimentResult
import com.devil.app.device.tablet.AndroidTabletEmbodimentStatus

/**
 * Stage 223 bounded Unified Multi-Device Validation result.
 *
 * VALIDATED preserves the exact coherent Phase N structural contexts from
 * Stages 213 through 222 together with normalized explicitly supplied
 * validation focus and evidence description.
 *
 * Exact provenance is required across the established chain:
 *
 * - Stage 213 and Stage 218 preserve the same Stage 84 relationship;
 * - Stage 215 preserves the exact Stage 214 tablet embodiment;
 * - Stage 217 preserves the exact Stage 216 PC embodiment;
 * - Stage 219 preserves the exact Stage 218 identity;
 * - Stage 220 preserves the exact Stage 219 session governance;
 * - Stage 221 preserves the exact Stage 220 task continuity;
 * - Stage 222 preserves the exact Stage 221 memory continuity.
 *
 * DEFERRED contains no validated Phase N context or validation metadata.
 *
 * MULTI_DEVICE_VALIDATED != CONSTITUTIONAL_VERIFICATION.
 * MULTI_DEVICE_VALIDATED != REAL_DEVICE_VALIDATED.
 * MULTI_DEVICE_VALIDATED != DEVICE_CONNECTIVITY.
 * MULTI_DEVICE_VALIDATED != AUTHENTICATION.
 * MULTI_DEVICE_VALIDATED != AUTHORIZATION.
 * MULTI_DEVICE_VALIDATED != REMOTE_EXECUTION.
 * MULTI_DEVICE_VALIDATED != MEMORY_SYNC.
 * TRUSTED_DEVICE_CONTEXT != EXECUTION_AUTHORITY.
 * REVOKED_DEVICE_CONTEXT != VALIDATED_MULTI_DEVICE_CONTEXT.
 * MULTIPLE_EMBODIMENTS != MULTIPLE_DEVILS.
 */
@ConsistentCopyVisibility
data class AndroidUnifiedMultiDeviceValidationResult private constructor(
    val status: AndroidUnifiedMultiDeviceValidationStatus,
    val deviceProtocol: AndroidDeviceProtocolIntegrationResult?,
    val tabletEmbodiment: AndroidTabletEmbodimentResult?,
    val educationTabletExperience: AndroidEducationTabletExperienceResult?,
    val pcEmbodiment: AndroidPcEmbodimentResult?,
    val pcCapabilityAdapter: AndroidPcCapabilityAdapterResult?,
    val crossDeviceIdentity: AndroidCrossDeviceIdentityResult?,
    val sessionGovernance: AndroidCrossDeviceSessionGovernanceResult?,
    val taskContinuity: AndroidCrossDeviceTaskContinuityResult?,
    val memoryContinuity: AndroidCrossDeviceMemoryContinuityResult?,
    val deviceTrustRevocation: AndroidDeviceTrustRevocationResult?,
    val validationFocus: String?,
    val validationEvidenceDescription: String?,
) {
    companion object {
        fun create(
            status: AndroidUnifiedMultiDeviceValidationStatus,
            deviceProtocol: AndroidDeviceProtocolIntegrationResult? = null,
            tabletEmbodiment: AndroidTabletEmbodimentResult? = null,
            educationTabletExperience: AndroidEducationTabletExperienceResult? = null,
            pcEmbodiment: AndroidPcEmbodimentResult? = null,
            pcCapabilityAdapter: AndroidPcCapabilityAdapterResult? = null,
            crossDeviceIdentity: AndroidCrossDeviceIdentityResult? = null,
            sessionGovernance: AndroidCrossDeviceSessionGovernanceResult? = null,
            taskContinuity: AndroidCrossDeviceTaskContinuityResult? = null,
            memoryContinuity: AndroidCrossDeviceMemoryContinuityResult? = null,
            deviceTrustRevocation: AndroidDeviceTrustRevocationResult? = null,
            validationFocus: String? = null,
            validationEvidenceDescription: String? = null,
        ): AndroidUnifiedMultiDeviceValidationResult {
            return when (status) {
                AndroidUnifiedMultiDeviceValidationStatus.VALIDATED -> {
                    val protocol = requireNotNull(deviceProtocol)
                    val tablet = requireNotNull(tabletEmbodiment)
                    val educationTablet = requireNotNull(educationTabletExperience)
                    val pc = requireNotNull(pcEmbodiment)
                    val pcAdapter = requireNotNull(pcCapabilityAdapter)
                    val identity = requireNotNull(crossDeviceIdentity)
                    val session = requireNotNull(sessionGovernance)
                    val task = requireNotNull(taskContinuity)
                    val memory = requireNotNull(memoryContinuity)
                    val trust = requireNotNull(deviceTrustRevocation)

                    require(
                        protocol.status ==
                            AndroidDeviceProtocolIntegrationStatus.AVAILABLE,
                    ) {
                        "Validated Stage 223 requires available Stage 213 Device Protocol Integration."
                    }
                    require(
                        tablet.status == AndroidTabletEmbodimentStatus.AVAILABLE,
                    ) {
                        "Validated Stage 223 requires available Stage 214 Tablet Embodiment."
                    }
                    require(
                        educationTablet.status ==
                            AndroidEducationTabletExperienceStatus.AVAILABLE,
                    ) {
                        "Validated Stage 223 requires available Stage 215 Education Tablet Experience."
                    }
                    require(
                        pc.status == AndroidPcEmbodimentStatus.AVAILABLE,
                    ) {
                        "Validated Stage 223 requires available Stage 216 PC Embodiment."
                    }
                    require(
                        pcAdapter.status ==
                            AndroidPcCapabilityAdapterStatus.AVAILABLE,
                    ) {
                        "Validated Stage 223 requires available Stage 217 PC Capability Adapter."
                    }
                    require(
                        identity.status ==
                            AndroidCrossDeviceIdentityStatus.AVAILABLE,
                    ) {
                        "Validated Stage 223 requires available Stage 218 Cross-Device Identity."
                    }
                    require(
                        session.status ==
                            AndroidCrossDeviceSessionGovernanceStatus.AVAILABLE,
                    ) {
                        "Validated Stage 223 requires available Stage 219 Cross-Device Session Governance."
                    }
                    require(
                        task.status ==
                            AndroidCrossDeviceTaskContinuityStatus.AVAILABLE,
                    ) {
                        "Validated Stage 223 requires available Stage 220 Cross-Device Task Continuity."
                    }
                    require(
                        memory.status ==
                            AndroidCrossDeviceMemoryContinuityStatus.AVAILABLE,
                    ) {
                        "Validated Stage 223 requires available Stage 221 Cross-Device Memory Continuity."
                    }
                    require(
                        trust.status ==
                            AndroidDeviceTrustRevocationStatus.TRUSTED,
                    ) {
                        "Validated Stage 223 requires trusted Stage 222 device context."
                    }

                    require(
                        protocol.relationshipRepresentation ===
                            identity.relationshipRepresentation,
                    ) {
                        "Stage 223 must preserve the exact Stage 84 relationship shared by Stages 213 and 218."
                    }
                    require(
                        educationTablet.tabletEmbodiment === tablet,
                    ) {
                        "Stage 223 must preserve exact Stage 214 to Stage 215 tablet provenance."
                    }
                    require(
                        pcAdapter.pcEmbodiment === pc,
                    ) {
                        "Stage 223 must preserve exact Stage 216 to Stage 217 PC provenance."
                    }
                    require(
                        session.crossDeviceIdentity === identity,
                    ) {
                        "Stage 223 must preserve exact Stage 218 to Stage 219 identity provenance."
                    }
                    require(
                        task.sessionGovernance === session,
                    ) {
                        "Stage 223 must preserve exact Stage 219 to Stage 220 session provenance."
                    }
                    require(
                        memory.taskContinuity === task,
                    ) {
                        "Stage 223 must preserve exact Stage 220 to Stage 221 task provenance."
                    }
                    require(
                        trust.memoryContinuity === memory,
                    ) {
                        "Stage 223 must preserve exact Stage 221 to Stage 222 memory provenance."
                    }

                    val normalizedFocus =
                        requireNotNull(validationFocus).trim()
                    val normalizedEvidence =
                        requireNotNull(validationEvidenceDescription).trim()

                    require(normalizedFocus.isNotEmpty()) {
                        "Stage 223 validation focus must not be blank."
                    }
                    require(normalizedEvidence.isNotEmpty()) {
                        "Stage 223 validation evidence description must not be blank."
                    }

                    AndroidUnifiedMultiDeviceValidationResult(
                        status = status,
                        deviceProtocol = protocol,
                        tabletEmbodiment = tablet,
                        educationTabletExperience = educationTablet,
                        pcEmbodiment = pc,
                        pcCapabilityAdapter = pcAdapter,
                        crossDeviceIdentity = identity,
                        sessionGovernance = session,
                        taskContinuity = task,
                        memoryContinuity = memory,
                        deviceTrustRevocation = trust,
                        validationFocus = normalizedFocus,
                        validationEvidenceDescription = normalizedEvidence,
                    )
                }

                AndroidUnifiedMultiDeviceValidationStatus.DEFERRED -> {
                    require(
                        listOf(
                            deviceProtocol,
                            tabletEmbodiment,
                            educationTabletExperience,
                            pcEmbodiment,
                            pcCapabilityAdapter,
                            crossDeviceIdentity,
                            sessionGovernance,
                            taskContinuity,
                            memoryContinuity,
                            deviceTrustRevocation,
                            validationFocus,
                            validationEvidenceDescription,
                        ).all { it == null },
                    ) {
                        "Deferred Stage 223 validation must not contain validated Phase N context or validation metadata."
                    }

                    AndroidUnifiedMultiDeviceValidationResult(
                        status = status,
                        deviceProtocol = null,
                        tabletEmbodiment = null,
                        educationTabletExperience = null,
                        pcEmbodiment = null,
                        pcCapabilityAdapter = null,
                        crossDeviceIdentity = null,
                        sessionGovernance = null,
                        taskContinuity = null,
                        memoryContinuity = null,
                        deviceTrustRevocation = null,
                        validationFocus = null,
                        validationEvidenceDescription = null,
                    )
                }
            }
        }
    }
}
