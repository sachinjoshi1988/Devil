package com.devil.core.runtime.education

import com.devil.core.model.common.TraceId
import com.devil.core.model.education.AdaptiveLanguageCurriculumRecord

/**
 * Stable Stage 131 result of bounded Adaptive Language Curriculum preparation.
 *
 * PREPARED requires one AdaptiveLanguageCurriculumRecord.
 * DEFERRED must not contain one.
 *
 * This result creates no learner assessment, proficiency score, mastery claim,
 * lesson, executed curriculum, Strategy Adaptation record, Brain decision, Task,
 * Plan, execution, Observation, Verification, Outcome, constitutional Learning,
 * Memory commitment, or verified learner progress.
 */
@ConsistentCopyVisibility
data class AdaptiveLanguageCurriculumPreparationResult private constructor(
    val traceId: TraceId,
    val status: AdaptiveLanguageCurriculumPreparationStatus,
    val curriculum: AdaptiveLanguageCurriculumRecord?,
) {
    companion object {

        fun create(
            traceId: TraceId,
            status: AdaptiveLanguageCurriculumPreparationStatus,
            curriculum: AdaptiveLanguageCurriculumRecord? = null,
        ): AdaptiveLanguageCurriculumPreparationResult {
            when (status) {
                AdaptiveLanguageCurriculumPreparationStatus.PREPARED -> {
                    require(curriculum != null) {
                        "Prepared Adaptive Language Curriculum results require one curriculum context."
                    }
                }

                AdaptiveLanguageCurriculumPreparationStatus.DEFERRED -> {
                    require(curriculum == null) {
                        "Deferred Adaptive Language Curriculum results must not contain a curriculum context."
                    }
                }
            }

            return AdaptiveLanguageCurriculumPreparationResult(
                traceId = traceId,
                status = status,
                curriculum = curriculum,
            )
        }
    }
}
