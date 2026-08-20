package com.devil.core.runtime.education

import com.devil.core.model.common.TraceId
import com.devil.core.model.education.HomeworkAssistanceRecord

/**
 * Stable Stage 147 result of bounded Homework Assistance preparation.
 *
 * PREPARED requires exactly one HomeworkAssistanceRecord.
 * DEFERRED must not contain one.
 *
 * This result establishes no assignment completion, submission, grade,
 * correctness verification, constitutional authorization, execution,
 * Observation, Verification, Outcome, Learning, or Memory commitment.
 */
@ConsistentCopyVisibility
data class HomeworkAssistancePreparationResult private constructor(
    val traceId: TraceId,
    val status: HomeworkAssistancePreparationStatus,
    val homeworkAssistance: HomeworkAssistanceRecord?,
) {
    companion object {

        fun create(
            traceId: TraceId,
            status: HomeworkAssistancePreparationStatus,
            homeworkAssistance: HomeworkAssistanceRecord? = null,
        ): HomeworkAssistancePreparationResult {
            when (status) {
                HomeworkAssistancePreparationStatus.PREPARED -> {
                    require(homeworkAssistance != null) {
                        "Prepared Homework Assistance results require one assistance context."
                    }
                }

                HomeworkAssistancePreparationStatus.DEFERRED -> {
                    require(homeworkAssistance == null) {
                        "Deferred Homework Assistance results must not contain an assistance context."
                    }
                }
            }

            return HomeworkAssistancePreparationResult(
                traceId = traceId,
                status = status,
                homeworkAssistance = homeworkAssistance,
            )
        }
    }
}
