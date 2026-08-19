package com.devil.core.runtime.education

import com.devil.core.model.common.TraceId
import com.devil.core.model.education.AdditionalLanguageEducationRecord

/**
 * Stable Stage 139 result of bounded Additional Language Expansion
 * preparation.
 *
 * PREPARED requires one AdditionalLanguageEducationRecord.
 * DEFERRED must not contain one.
 *
 * This result creates no translation result, curriculum result,
 * pronunciation result, proficiency claim, Brain decision, Task, Plan,
 * execution, Observation, Verification, Outcome, constitutional Learning,
 * Memory commitment, or verified learner progress.
 */
@ConsistentCopyVisibility
data class AdditionalLanguageEducationPreparationResult private constructor(
    val traceId: TraceId,
    val status: AdditionalLanguageEducationPreparationStatus,
    val additionalLanguageEducation: AdditionalLanguageEducationRecord?,
) {
    companion object {

        fun create(
            traceId: TraceId,
            status: AdditionalLanguageEducationPreparationStatus,
            additionalLanguageEducation: AdditionalLanguageEducationRecord? = null,
        ): AdditionalLanguageEducationPreparationResult {
            when (status) {
                AdditionalLanguageEducationPreparationStatus.PREPARED -> {
                    require(additionalLanguageEducation != null) {
                        "Prepared Additional Language Expansion results require one education context."
                    }
                }

                AdditionalLanguageEducationPreparationStatus.DEFERRED -> {
                    require(additionalLanguageEducation == null) {
                        "Deferred Additional Language Expansion results must not contain an education context."
                    }
                }
            }

            return AdditionalLanguageEducationPreparationResult(
                traceId = traceId,
                status = status,
                additionalLanguageEducation = additionalLanguageEducation,
            )
        }
    }
}
