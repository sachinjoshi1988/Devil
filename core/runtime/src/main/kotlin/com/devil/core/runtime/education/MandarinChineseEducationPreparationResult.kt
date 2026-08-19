package com.devil.core.runtime.education

import com.devil.core.model.common.TraceId
import com.devil.core.model.education.MandarinChineseEducationRecord

/**
 * Stable Stage 138 result of bounded Mandarin Chinese Education preparation.
 *
 * PREPARED requires one MandarinChineseEducationRecord.
 * DEFERRED must not contain one.
 *
 * This result creates no translation result, vocabulary result, grammar result,
 * character result, pinyin result, pronunciation result, proficiency claim,
 * Brain decision, Task, Plan, execution, Observation, Verification, Outcome,
 * constitutional Learning, Memory commitment, or verified learner progress.
 */
@ConsistentCopyVisibility
data class MandarinChineseEducationPreparationResult private constructor(
    val traceId: TraceId,
    val status: MandarinChineseEducationPreparationStatus,
    val mandarinChineseEducation: MandarinChineseEducationRecord?,
) {
    companion object {

        fun create(
            traceId: TraceId,
            status: MandarinChineseEducationPreparationStatus,
            mandarinChineseEducation: MandarinChineseEducationRecord? = null,
        ): MandarinChineseEducationPreparationResult {
            when (status) {
                MandarinChineseEducationPreparationStatus.PREPARED -> {
                    require(mandarinChineseEducation != null) {
                        "Prepared Mandarin Chinese Education results require one Mandarin Chinese Education context."
                    }
                }

                MandarinChineseEducationPreparationStatus.DEFERRED -> {
                    require(mandarinChineseEducation == null) {
                        "Deferred Mandarin Chinese Education results must not contain a Mandarin Chinese Education context."
                    }
                }
            }

            return MandarinChineseEducationPreparationResult(
                traceId = traceId,
                status = status,
                mandarinChineseEducation = mandarinChineseEducation,
            )
        }
    }
}
