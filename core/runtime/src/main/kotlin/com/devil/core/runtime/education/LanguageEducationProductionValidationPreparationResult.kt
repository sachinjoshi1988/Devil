package com.devil.core.runtime.education

import com.devil.core.model.common.TraceId
import com.devil.core.model.education.LanguageEducationProductionValidationRecord

/**
 * Stable Stage 142 result of bounded Language Education Production Validation
 * preparation.
 *
 * VALIDATED requires one LanguageEducationProductionValidationRecord.
 * DEFERRED must not contain one.
 *
 * This result creates no learner-proficiency result, translation result,
 * conversation result, speech result, pronunciation result, Brain decision,
 * Task, Plan, execution, constitutional Observation, constitutional
 * Verification, Outcome, constitutional Learning, Memory commitment,
 * production-runtime success, or real-device validation.
 */
@ConsistentCopyVisibility
data class LanguageEducationProductionValidationPreparationResult private constructor(
    val traceId: TraceId,
    val status: LanguageEducationProductionValidationPreparationStatus,
    val validation: LanguageEducationProductionValidationRecord?,
) {
    companion object {

        fun create(
            traceId: TraceId,
            status: LanguageEducationProductionValidationPreparationStatus,
            validation: LanguageEducationProductionValidationRecord? = null,
        ): LanguageEducationProductionValidationPreparationResult {
            when (status) {
                LanguageEducationProductionValidationPreparationStatus.VALIDATED -> {
                    require(validation != null) {
                        "Validated Language Education Production results require one validation context."
                    }
                }

                LanguageEducationProductionValidationPreparationStatus.DEFERRED -> {
                    require(validation == null) {
                        "Deferred Language Education Production results must not contain a validation context."
                    }
                }
            }

            return LanguageEducationProductionValidationPreparationResult(
                traceId = traceId,
                status = status,
                validation = validation,
            )
        }
    }
}
