package com.devil.core.runtime.embodiment

import com.devil.core.model.common.TraceId
import com.devil.core.model.embodiment.EmbodimentRecord
import com.devil.core.model.embodiment.PcEmbodimentEvidence

/**
 * Stage 83 bounded PC-embodiment coordinator.
 *
 * This coordinator evaluates already-supplied architectural embodiment metadata.
 * It performs no platform discovery and communicates with no desktop API.
 *
 * Stage 81 EmbodimentPlatformId determines whether the represented embodiment
 * belongs to the PC platform family.
 *
 * Genuine PC-specific evidence is required only after that existing platform
 * identity identifies the embodiment as PC-class.
 *
 * A PC embodiment remains only another bounded host around the same Devil
 * intelligence.
 *
 * This coordinator does not:
 *
 * - create another Devil intelligence;
 * - create another Brain;
 * - create another Unified Devil Runtime;
 * - create a PC-specific Memory Authority or Security Authority;
 * - resolve subject identity;
 * - evaluate trust;
 * - authenticate a subject;
 * - grant authorization;
 * - establish or validate a session;
 * - enter Owner Mode;
 * - register capabilities;
 * - establish capability availability, health, or readiness;
 * - inspect desktop permissions;
 * - create Tasks or Plans;
 * - invoke UnifiedDevilRuntime;
 * - execute desktop actions;
 * - establish Observation, Verification, or Outcome;
 * - mutate World Model state;
 * - perform Learning;
 * - commit Memory;
 * - persist PC state;
 * - discover Windows, Linux, macOS, or another operating system;
 * - or communicate with a platform API.
 *
 * PC = EMBODIMENT PLATFORM FAMILY.
 * PC != DEVIL IDENTITY.
 * PC != INTELLIGENCE.
 * PC != AUTHORITY.
 * PC != EXECUTION.
 */
class PcEmbodimentCoordinator {

    fun assess(
        traceId: TraceId,
        embodiment: EmbodimentRecord,
        evidence: PcEmbodimentEvidence?,
    ): PcEmbodimentAssessmentResult {
        if (
            !embodiment.platformId.value.equals(
                other = PC_PLATFORM_ID,
                ignoreCase = true,
            )
        ) {
            return PcEmbodimentAssessmentResult.create(
                traceId = traceId,
                status = PcEmbodimentAssessmentStatus.NON_PC,
                embodiment = embodiment,
            )
        }

        if (evidence == null) {
            return deferred(
                traceId = traceId,
                embodiment = embodiment,
            )
        }

        return PcEmbodimentAssessmentResult.create(
            traceId = traceId,
            status = PcEmbodimentAssessmentStatus.PC,
            embodiment = embodiment,
            evidence = evidence,
        )
    }

    private fun deferred(
        traceId: TraceId,
        embodiment: EmbodimentRecord,
    ): PcEmbodimentAssessmentResult {
        return PcEmbodimentAssessmentResult.create(
            traceId = traceId,
            status = PcEmbodimentAssessmentStatus.DEFERRED,
            embodiment = embodiment,
        )
    }

    companion object {
        const val PC_PLATFORM_ID: String =
            "pc"
    }
}
