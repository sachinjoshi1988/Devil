package com.devil.core.runtime.education

import com.devil.core.model.common.TraceId
import com.devil.core.model.education.RussianEducationRecord

/**
 * Stable Stage 137 result of bounded Russian Education preparation.
 *
 * PREPARED requires one RussianEducationRecord.
 * DEFERRED must not contain one.
 *
 * This result creates no translation result, vocabulary result, grammar result,
 * pronunciation result, proficiency claim, Brain decision, Task, Plan,
 * execution, Observation, Verification, Outcome, constitutional Learning,
 * Memory commitment, or verified learner progress.
 */
@ConsistentCopyVisibility
data class RussianEducationPreparationResult private constructor(
    val traceId: TraceId,
    val status: RussianEducationPreparationStatus,
    val russianEducation: RussianEducationRecord?,
) {
    companion object {

        fun create(
            traceId: TraceId,
            status: RussianEducationPreparationStatus,
            russianEducation: RussianEducationRecord? = null,
        ): RussianEducationPreparationResult {
            when (status) {
                RussianEducationPreparationStatus.PREPARED -> {
                    require(russianEducation != null) {
                        "Prepared Russian Education results require one Russian Education context."
                    }
                }

                RussianEducationPreparationStatus.DEFERRED -> {
                    require(russianEducation == null) {
                        "Deferred Russian Education results must not contain a Russian Education context."
                    }
                }
            }

            return RussianEducationPreparationResult(
                traceId = traceId,
                status = status,
                russianEducation = russianEducation,
            )
        }
    }
}
