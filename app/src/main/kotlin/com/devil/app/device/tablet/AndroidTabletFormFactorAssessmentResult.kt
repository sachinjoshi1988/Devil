package com.devil.app.device.tablet

import com.devil.core.model.common.TraceId
import com.devil.core.model.embodiment.EmbodimentRecord

/**
 * Stable Stage 82 result of one bounded tablet-form-factor assessment.
 *
 * The result always preserves the Stage 81 embodiment being assessed.
 *
 * TABLET and NON_TABLET require genuine Android configuration evidence.
 * DEFERRED must not contain fabricated evidence.
 *
 * This result describes form factor only.
 *
 * FORM_FACTOR != INTELLIGENCE.
 * FORM_FACTOR != IDENTITY_AUTHORITY.
 * FORM_FACTOR != AUTHORIZATION.
 * FORM_FACTOR != CAPABILITY.
 * FORM_FACTOR != EXECUTION.
 */
@ConsistentCopyVisibility
data class AndroidTabletFormFactorAssessmentResult private constructor(
    val traceId: TraceId,
    val status: AndroidTabletFormFactorAssessmentStatus,
    val embodiment: EmbodimentRecord,
    val evidence: AndroidTabletFormFactorEvidence?,
) {
    companion object {

        fun create(
            traceId: TraceId,
            status: AndroidTabletFormFactorAssessmentStatus,
            embodiment: EmbodimentRecord,
            evidence: AndroidTabletFormFactorEvidence? = null,
        ): AndroidTabletFormFactorAssessmentResult {
            when (status) {
                AndroidTabletFormFactorAssessmentStatus.TABLET,
                AndroidTabletFormFactorAssessmentStatus.NON_TABLET,
                -> {
                    require(evidence != null) {
                        "Determined tablet form-factor results require genuine evidence."
                    }
                }

                AndroidTabletFormFactorAssessmentStatus.DEFERRED -> {
                    require(evidence == null) {
                        "Deferred tablet form-factor results must not contain evidence."
                    }
                }
            }

            return AndroidTabletFormFactorAssessmentResult(
                traceId = traceId,
                status = status,
                embodiment = embodiment,
                evidence = evidence,
            )
        }
    }
}
