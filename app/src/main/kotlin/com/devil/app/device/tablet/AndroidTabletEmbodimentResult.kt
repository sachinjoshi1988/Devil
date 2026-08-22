package com.devil.app.device.tablet

import com.devil.core.model.embodiment.EmbodimentRecord

/**
 * Stage 214 bounded Tablet Embodiment result.
 *
 * AVAILABLE preserves the exact Stage 82 tablet-form-factor assessment and the
 * exact EmbodimentRecord already preserved by that assessment.
 *
 * DEFERRED preserves the exact Stage 82 assessment without claiming tablet
 * embodiment availability.
 *
 * TABLET_EMBODIMENT != NEW_DEVIL.
 * TABLET_EMBODIMENT != NEW_RUNTIME.
 * TABLET = FORM_FACTOR.
 * FORM_FACTOR != DEVICE_TRUST.
 * TABLET_EMBODIMENT != AUTHENTICATION.
 * TABLET_EMBODIMENT != AUTHORIZATION.
 * TABLET_EMBODIMENT != CAPABILITY_AVAILABILITY.
 * TABLET_EMBODIMENT != SESSION_CONTINUITY.
 * TABLET_EMBODIMENT != EXECUTION.
 * TABLET_EMBODIMENT != MEMORY_SYNC.
 */
@ConsistentCopyVisibility
data class AndroidTabletEmbodimentResult private constructor(
    val status: AndroidTabletEmbodimentStatus,
    val formFactorAssessment: AndroidTabletFormFactorAssessmentResult,
    val embodiment: EmbodimentRecord,
) {
    companion object {
        fun create(
            status: AndroidTabletEmbodimentStatus,
            formFactorAssessment: AndroidTabletFormFactorAssessmentResult,
            embodiment: EmbodimentRecord,
        ): AndroidTabletEmbodimentResult {
            require(
                embodiment === formFactorAssessment.embodiment,
            ) {
                "Stage 214 Tablet Embodiment must preserve the exact Stage 82 embodiment provenance."
            }

            when (status) {
                AndroidTabletEmbodimentStatus.AVAILABLE -> {
                    require(
                        formFactorAssessment.status ==
                            AndroidTabletFormFactorAssessmentStatus.TABLET,
                    ) {
                        "Available Stage 214 Tablet Embodiment requires a Stage 82 TABLET assessment."
                    }
                }

                AndroidTabletEmbodimentStatus.DEFERRED -> Unit
            }

            return AndroidTabletEmbodimentResult(
                status = status,
                formFactorAssessment = formFactorAssessment,
                embodiment = embodiment,
            )
        }
    }
}
