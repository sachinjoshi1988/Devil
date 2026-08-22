package com.devil.app.device.pc

import com.devil.core.model.capability.CapabilityContract

/**
 * Stage 217 bounded PC Capability Adapter coordinator.
 *
 * It associates one exact Stage 216 PC Embodiment result with one exact existing
 * CapabilityContract and one explicitly supplied bounded adapter identifier.
 *
 * It does not:
 *
 * - register or select capabilities;
 * - establish capability availability;
 * - establish capability health;
 * - inspect desktop permissions;
 * - discover or invoke Windows, Linux, macOS, or other desktop APIs;
 * - create a platform implementation from an adapter identifier;
 * - authenticate a subject or device;
 * - grant authorization;
 * - establish Executive readiness;
 * - create an ExecutionRequest;
 * - execute desktop capabilities;
 * - establish Observation, Verification, or Outcome;
 * - synchronize Conversation, World Model, or Memory state;
 * - implement Stage 218 Cross-Device Identity.
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
class AndroidPcCapabilityAdapterCoordinator {

    fun integrate(
        pcEmbodiment: AndroidPcEmbodimentResult,
        capability: CapabilityContract,
        adapterId: String?,
    ): AndroidPcCapabilityAdapterResult {
        if (
            pcEmbodiment.status !=
                AndroidPcEmbodimentStatus.AVAILABLE ||
            adapterId.isNullOrBlank()
        ) {
            return AndroidPcCapabilityAdapterResult.create(
                status = AndroidPcCapabilityAdapterStatus.DEFERRED,
                pcEmbodiment = pcEmbodiment,
                capability = capability,
            )
        }

        return AndroidPcCapabilityAdapterResult.create(
            status = AndroidPcCapabilityAdapterStatus.AVAILABLE,
            pcEmbodiment = pcEmbodiment,
            capability = capability,
            adapterId = adapterId,
        )
    }
}
