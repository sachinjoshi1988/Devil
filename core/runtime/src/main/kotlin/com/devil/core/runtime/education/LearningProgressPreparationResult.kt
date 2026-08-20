package com.devil.core.runtime.education

import com.devil.core.model.common.TraceId
import com.devil.core.model.education.LearningProgressRecord

/**
 * Stable Stage 149 result of bounded Learning Progress preparation.
 *
 * PREPARED requires exactly one LearningProgressRecord.
 * DEFERRED must not contain one.
 *
 * This result establishes no verified mastery, global proficiency,
 * constitutional Observation, Verification, Outcome, Learning,
 * Memory commitment, persistence, execution, or guardian summary.
 */
@ConsistentCopyVisibility
data class LearningProgressPreparationResult private constructor(
    val traceId: TraceId,
    val status: LearningProgressPreparationStatus,
    val learningProgress: LearningProgressRecord?,
) {
    companion object {

        fun create(
            traceId: TraceId,
            status: LearningProgressPreparationStatus,
            learningProgress: LearningProgressRecord? = null,
        ): LearningProgressPreparationResult {
            when (status) {
                LearningProgressPreparationStatus.PREPARED -> {
                    require(learningProgress != null) {
                        "Prepared Learning Progress results require one progress context."
                    }
                }

                LearningProgressPreparationStatus.DEFERRED -> {
                    require(learningProgress == null) {
                        "Deferred Learning Progress results must not contain a progress context."
                    }
                }
            }

            return LearningProgressPreparationResult(
                traceId = traceId,
                status = status,
                learningProgress = learningProgress,
            )
        }
    }
}
