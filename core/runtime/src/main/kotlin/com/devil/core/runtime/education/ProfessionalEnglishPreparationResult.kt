package com.devil.core.runtime.education

import com.devil.core.model.common.TraceId
import com.devil.core.model.education.ProfessionalEnglishPracticeRecord

/**
 * Stable Stage 130 result of bounded Professional English preparation.
 *
 * PREPARED requires one ProfessionalEnglishPracticeRecord.
 * DEFERRED must not contain one.
 *
 * This result creates no sent message, workplace execution, employment action,
 * interview outcome, proficiency claim, Brain decision, Task, Plan, execution,
 * Observation, Verification, Outcome, constitutional Learning,
 * Memory commitment, or verified learner progress.
 */
@ConsistentCopyVisibility
data class ProfessionalEnglishPreparationResult private constructor(
    val traceId: TraceId,
    val status: ProfessionalEnglishPreparationStatus,
    val practice: ProfessionalEnglishPracticeRecord?,
) {
    companion object {

        fun create(
            traceId: TraceId,
            status: ProfessionalEnglishPreparationStatus,
            practice: ProfessionalEnglishPracticeRecord? = null,
        ): ProfessionalEnglishPreparationResult {
            when (status) {
                ProfessionalEnglishPreparationStatus.PREPARED -> {
                    require(practice != null) {
                        "Prepared Professional English results require one practice context."
                    }
                }

                ProfessionalEnglishPreparationStatus.DEFERRED -> {
                    require(practice == null) {
                        "Deferred Professional English results must not contain a practice context."
                    }
                }
            }

            return ProfessionalEnglishPreparationResult(
                traceId = traceId,
                status = status,
                practice = practice,
            )
        }
    }
}
