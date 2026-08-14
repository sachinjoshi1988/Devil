package com.devil.app.device.tablet

import com.devil.core.model.common.TraceId
import com.devil.core.model.embodiment.EmbodimentRecord

/**
 * Stage 82 bounded Android tablet-form-factor coordinator.
 *
 * This coordinator classifies one existing Stage 81 Android embodiment
 * using genuine Android screen-configuration evidence.
 *
 * Android remains the platform.
 * Tablet is a form factor of that Android embodiment.
 *
 * The coordinator does not:
 *
 * - create another Devil intelligence;
 * - create another Brain;
 * - create another Unified Devil Runtime;
 * - mutate EmbodimentRecord;
 * - infer subject identity;
 * - evaluate trust;
 * - authenticate a subject;
 * - grant authorization;
 * - establish or validate a session;
 * - enter Owner Mode;
 * - register capabilities;
 * - establish capability availability or health;
 * - grant Android permission;
 * - create Tasks or Plans;
 * - invoke UnifiedDevilRuntime;
 * - execute actions;
 * - establish Observation, Verification, or Outcome;
 * - mutate World Model state;
 * - perform Learning;
 * - commit Memory;
 * - or persist tablet state.
 *
 * ANDROID = PLATFORM.
 * TABLET = FORM FACTOR.
 * FORM_FACTOR != INTELLIGENCE.
 */
class AndroidTabletFormFactorCoordinator(
    private val evidenceSource: AndroidTabletFormFactorEvidenceSource,
) {
    fun assess(
        traceId: TraceId,
        embodiment: EmbodimentRecord,
    ): AndroidTabletFormFactorAssessmentResult {
        if (!embodiment.platformId.value.equals(
                other = ANDROID_PLATFORM_ID,
                ignoreCase = true,
            )
        ) {
            return deferred(
                traceId = traceId,
                embodiment = embodiment,
            )
        }

        val evidence =
            evidenceSource.evidence()
                ?: return deferred(
                    traceId = traceId,
                    embodiment = embodiment,
                )

        val status =
            if (
                evidence.smallestScreenWidthDp >=
                    TABLET_MINIMUM_SMALLEST_WIDTH_DP
            ) {
                AndroidTabletFormFactorAssessmentStatus.TABLET
            } else {
                AndroidTabletFormFactorAssessmentStatus.NON_TABLET
            }

        return AndroidTabletFormFactorAssessmentResult.create(
            traceId = traceId,
            status = status,
            embodiment = embodiment,
            evidence = evidence,
        )
    }

    private fun deferred(
        traceId: TraceId,
        embodiment: EmbodimentRecord,
    ): AndroidTabletFormFactorAssessmentResult {
        return AndroidTabletFormFactorAssessmentResult.create(
            traceId = traceId,
            status = AndroidTabletFormFactorAssessmentStatus.DEFERRED,
            embodiment = embodiment,
        )
    }

    companion object {
        const val TABLET_MINIMUM_SMALLEST_WIDTH_DP: Int =
            600

        private const val ANDROID_PLATFORM_ID: String =
            "android"
    }
}
