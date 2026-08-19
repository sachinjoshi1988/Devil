package com.devil.core.runtime.education

import com.devil.core.model.common.TraceId
import com.devil.core.model.education.FrenchEducationRecord

/**
 * Stable Stage 134 result of bounded French Education preparation.
 *
 * PREPARED requires one FrenchEducationRecord.
 * DEFERRED must not contain one.
 *
 * This result creates no translation result, vocabulary result, grammar result,
 * pronunciation result, proficiency claim, Brain decision, Task, Plan,
 * execution, Observation, Verification, Outcome, constitutional Learning,
 * Memory commitment, or verified learner progress.
 */
@ConsistentCopyVisibility
data class FrenchEducationPreparationResult private constructor(
    val traceId: TraceId,
    val status: FrenchEducationPreparationStatus,
    val frenchEducation: FrenchEducationRecord?,
) {
    companion object {

        fun create(
            traceId: TraceId,
            status: FrenchEducationPreparationStatus,
            frenchEducation: FrenchEducationRecord? = null,
        ): FrenchEducationPreparationResult {
            when (status) {
                FrenchEducationPreparationStatus.PREPARED -> {
                    require(frenchEducation != null) {
                        "Prepared French Education results require one French Education context."
                    }
                }

                FrenchEducationPreparationStatus.DEFERRED -> {
                    require(frenchEducation == null) {
                        "Deferred French Education results must not contain a French Education context."
                    }
                }
            }

            return FrenchEducationPreparationResult(
                traceId = traceId,
                status = status,
                frenchEducation = frenchEducation,
            )
        }
    }
}
