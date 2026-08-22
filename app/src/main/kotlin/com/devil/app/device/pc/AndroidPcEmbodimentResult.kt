package com.devil.app.device.pc

import com.devil.core.model.embodiment.EmbodimentRecord
import com.devil.core.runtime.embodiment.PcEmbodimentAssessmentResult
import com.devil.core.runtime.embodiment.PcEmbodimentAssessmentStatus

/**
 * Stage 216 bounded PC Embodiment result.
 *
 * AVAILABLE preserves the exact Stage 83 PC-embodiment assessment and the exact
 * EmbodimentRecord already preserved by that assessment.
 *
 * DEFERRED preserves the exact Stage 83 assessment without claiming PC
 * embodiment availability.
 *
 * PC_EMBODIMENT != NEW_DEVIL.
 * PC_EMBODIMENT != NEW_RUNTIME.
 * PC = EMBODIMENT_PLATFORM_FAMILY.
 * PC_EMBODIMENT != DEVICE_TRUST.
 * PC_EMBODIMENT != AUTHENTICATION.
 * PC_EMBODIMENT != AUTHORIZATION.
 * PC_EMBODIMENT != CAPABILITY_AVAILABILITY.
 * PC_EMBODIMENT != SESSION_CONTINUITY.
 * PC_EMBODIMENT != EXECUTION.
 * PC_EMBODIMENT != MEMORY_SYNC.
 */
@ConsistentCopyVisibility
data class AndroidPcEmbodimentResult private constructor(
    val status: AndroidPcEmbodimentStatus,
    val pcAssessment: PcEmbodimentAssessmentResult,
    val embodiment: EmbodimentRecord,
) {
    companion object {
        fun create(
            status: AndroidPcEmbodimentStatus,
            pcAssessment: PcEmbodimentAssessmentResult,
            embodiment: EmbodimentRecord,
        ): AndroidPcEmbodimentResult {
            require(
                embodiment === pcAssessment.embodiment,
            ) {
                "Stage 216 PC Embodiment must preserve the exact Stage 83 embodiment provenance."
            }

            when (status) {
                AndroidPcEmbodimentStatus.AVAILABLE -> {
                    require(
                        pcAssessment.status ==
                            PcEmbodimentAssessmentStatus.PC,
                    ) {
                        "Available Stage 216 PC Embodiment requires a Stage 83 PC assessment."
                    }
                }

                AndroidPcEmbodimentStatus.DEFERRED -> Unit
            }

            return AndroidPcEmbodimentResult(
                status = status,
                pcAssessment = pcAssessment,
                embodiment = embodiment,
            )
        }
    }
}
