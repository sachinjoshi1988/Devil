package com.devil.core.runtime.education

import com.devil.core.model.common.TraceId
import com.devil.core.model.education.GermanEducationRecord

/**
 * Stable Stage 135 result of bounded German Education preparation.
 *
 * PREPARED requires one GermanEducationRecord.
 * DEFERRED must not contain one.
 *
 * This result creates no translation result, vocabulary result, grammar result,
 * pronunciation result, proficiency claim, Brain decision, Task, Plan,
 * execution, Observation, Verification, Outcome, constitutional Learning,
 * Memory commitment, or verified learner progress.
 */
@ConsistentCopyVisibility
data class GermanEducationPreparationResult private constructor(
    val traceId: TraceId,
    val status: GermanEducationPreparationStatus,
    val germanEducation: GermanEducationRecord?,
) {
    companion object {

        fun create(
            traceId: TraceId,
            status: GermanEducationPreparationStatus,
            germanEducation: GermanEducationRecord? = null,
        ): GermanEducationPreparationResult {
            when (status) {
                GermanEducationPreparationStatus.PREPARED -> {
                    require(germanEducation != null) {
                        "Prepared German Education results require one German Education context."
                    }
                }

                GermanEducationPreparationStatus.DEFERRED -> {
                    require(germanEducation == null) {
                        "Deferred German Education results must not contain a German Education context."
                    }
                }
            }

            return GermanEducationPreparationResult(
                traceId = traceId,
                status = status,
                germanEducation = germanEducation,
            )
        }
    }
}
