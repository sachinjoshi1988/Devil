package com.devil.core.runtime.education

import com.devil.core.model.common.TraceId
import com.devil.core.model.education.LanguageProgressAssessmentRecord

/**
 * Stable Stage 132 result of bounded Language Progress & Assessment preparation.
 *
 * PREPARED requires one LanguageProgressAssessmentRecord.
 * DEFERRED must not contain one.
 *
 * This result creates no constitutional Observation, Verification, Outcome,
 * standardized proficiency score, mastery claim, Brain decision, Task, Plan,
 * execution, autonomous curriculum adaptation, constitutional Learning,
 * Memory commitment, or verified global learner progress.
 */
@ConsistentCopyVisibility
data class LanguageProgressAssessmentPreparationResult private constructor(
    val traceId: TraceId,
    val status: LanguageProgressAssessmentPreparationStatus,
    val assessment: LanguageProgressAssessmentRecord?,
) {
    companion object {

        fun create(
            traceId: TraceId,
            status: LanguageProgressAssessmentPreparationStatus,
            assessment: LanguageProgressAssessmentRecord? = null,
        ): LanguageProgressAssessmentPreparationResult {
            when (status) {
                LanguageProgressAssessmentPreparationStatus.PREPARED -> {
                    require(assessment != null) {
                        "Prepared Language Progress & Assessment results require one assessment context."
                    }
                }

                LanguageProgressAssessmentPreparationStatus.DEFERRED -> {
                    require(assessment == null) {
                        "Deferred Language Progress & Assessment results must not contain an assessment context."
                    }
                }
            }

            return LanguageProgressAssessmentPreparationResult(
                traceId = traceId,
                status = status,
                assessment = assessment,
            )
        }
    }
}
