package com.devil.app.device.pc

import com.devil.core.model.capability.CapabilityContract

/**
 * Stage 217 bounded PC Capability Adapter result.
 *
 * AVAILABLE preserves one exact available Stage 216 PC Embodiment result, one
 * exact existing CapabilityContract, and one normalized explicitly supplied
 * bounded adapter identifier.
 *
 * DEFERRED preserves the exact Stage 216 result and capability contract without
 * claiming a PC capability adapter is available.
 *
 * PC_CAPABILITY_ADAPTER != CAPABILITY_REGISTRATION.
 * PC_CAPABILITY_ADAPTER != CAPABILITY_AVAILABILITY.
 * PC_CAPABILITY_ADAPTER != CAPABILITY_HEALTH.
 * PC_CAPABILITY_ADAPTER != AUTHORIZATION.
 * PC_CAPABILITY_ADAPTER != EXECUTION.
 * ADAPTER_IDENTIFIER != PLATFORM_IMPLEMENTATION.
 * PC_EMBODIMENT != EXECUTION_AUTHORITY.
 * REGISTERED != AVAILABLE != AUTHORIZED != READY != EXECUTED.
 */
@ConsistentCopyVisibility
data class AndroidPcCapabilityAdapterResult private constructor(
    val status: AndroidPcCapabilityAdapterStatus,
    val pcEmbodiment: AndroidPcEmbodimentResult,
    val capability: CapabilityContract,
    val adapterId: String?,
) {
    companion object {
        fun create(
            status: AndroidPcCapabilityAdapterStatus,
            pcEmbodiment: AndroidPcEmbodimentResult,
            capability: CapabilityContract,
            adapterId: String? = null,
        ): AndroidPcCapabilityAdapterResult {
            return when (status) {
                AndroidPcCapabilityAdapterStatus.AVAILABLE -> {
                    require(
                        pcEmbodiment.status ==
                            AndroidPcEmbodimentStatus.AVAILABLE,
                    ) {
                        "Available Stage 217 PC Capability Adapter requires available Stage 216 PC Embodiment."
                    }

                    val normalizedAdapterId =
                        requireNotNull(adapterId)
                            .trim()

                    require(normalizedAdapterId.isNotEmpty()) {
                        "Stage 217 PC capability adapter identifier must not be blank."
                    }

                    AndroidPcCapabilityAdapterResult(
                        status = status,
                        pcEmbodiment = pcEmbodiment,
                        capability = capability,
                        adapterId = normalizedAdapterId,
                    )
                }

                AndroidPcCapabilityAdapterStatus.DEFERRED -> {
                    require(adapterId == null) {
                        "Deferred Stage 217 PC Capability Adapter must not contain an adapter identifier."
                    }

                    AndroidPcCapabilityAdapterResult(
                        status = status,
                        pcEmbodiment = pcEmbodiment,
                        capability = capability,
                        adapterId = null,
                    )
                }
            }
        }
    }
}
