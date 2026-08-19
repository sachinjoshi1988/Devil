package com.devil.core.runtime.education

import com.devil.core.model.common.TraceId
import com.devil.core.model.education.AcademicEnglishPracticeRecord

/**
 * Stable Stage 129 result of bounded Academic English preparation.
 *
 * PREPARED requires one AcademicEnglishPracticeRecord.
 * DEFERRED must not contain one.
 *
 * This result creates no completed assignment, grade, citation-verification
 * result, proficiency claim, Brain decision, Task, Plan, execution,
 * Observation, Verification, Outcome, constitutional Learning,
 * Memory commitment, or verified learner progress.
 */
@ConsistentCopyVisibility
data class AcademicEnglishPreparationResult private constructor(
    val traceId: TraceId,
    val status: AcademicEnglishPreparationStatus,
    val practice: AcademicEnglishPracticeRecord?,
) {
    companion object {

        fun create(
            traceId: TraceId,
            status: AcademicEnglishPreparationStatus,
            practice: AcademicEnglishPracticeRecord? = null,
        ): AcademicEnglishPreparationResult {
            when (status) {
                AcademicEnglishPreparationStatus.PREPARED -> {
                    require(practice != null) {
                        "Prepared Academic English results require one practice context."
                    }
                }

                AcademicEnglishPreparationStatus.DEFERRED -> {
                    require(practice == null) {
                        "Deferred Academic English results must not contain a practice context."
                    }
                }
            }

            return AcademicEnglishPreparationResult(
                traceId = traceId,
                status = status,
                practice = practice,
            )
        }
    }
}
