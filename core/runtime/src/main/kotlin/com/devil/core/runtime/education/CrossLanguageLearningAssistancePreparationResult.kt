package com.devil.core.runtime.education

import com.devil.core.model.common.TraceId
import com.devil.core.model.education.CrossLanguageLearningAssistanceRecord

/**
 * Stable Stage 141 result of bounded Cross-Language Learning Assistance
 * preparation.
 *
 * PREPARED requires one CrossLanguageLearningAssistanceRecord.
 * DEFERRED must not contain one.
 *
 * This result creates no translation result, bilingual-generation result,
 * pronunciation result, proficiency claim, Brain decision, Task, Plan,
 * execution, Observation, Verification, Outcome, constitutional Learning,
 * Memory commitment, or verified learner progress.
 */
@ConsistentCopyVisibility
data class CrossLanguageLearningAssistancePreparationResult private constructor(
    val traceId: TraceId,
    val status: CrossLanguageLearningAssistancePreparationStatus,
    val assistance: CrossLanguageLearningAssistanceRecord?,
) {
    companion object {

        fun create(
            traceId: TraceId,
            status: CrossLanguageLearningAssistancePreparationStatus,
            assistance: CrossLanguageLearningAssistanceRecord? = null,
        ): CrossLanguageLearningAssistancePreparationResult {
            when (status) {
                CrossLanguageLearningAssistancePreparationStatus.PREPARED -> {
                    require(assistance != null) {
                        "Prepared Cross-Language Learning Assistance results require one assistance context."
                    }
                }

                CrossLanguageLearningAssistancePreparationStatus.DEFERRED -> {
                    require(assistance == null) {
                        "Deferred Cross-Language Learning Assistance results must not contain an assistance context."
                    }
                }
            }

            return CrossLanguageLearningAssistancePreparationResult(
                traceId = traceId,
                status = status,
                assistance = assistance,
            )
        }
    }
}
