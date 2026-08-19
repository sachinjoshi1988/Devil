package com.devil.core.runtime.education

import com.devil.core.model.common.TraceId
import com.devil.core.model.education.SpanishEducationRecord

/**
 * Stable Stage 136 result of bounded Spanish Education preparation.
 *
 * PREPARED requires one SpanishEducationRecord.
 * DEFERRED must not contain one.
 *
 * This result creates no translation result, vocabulary result, grammar result,
 * pronunciation result, proficiency claim, Brain decision, Task, Plan,
 * execution, Observation, Verification, Outcome, constitutional Learning,
 * Memory commitment, or verified learner progress.
 */
@ConsistentCopyVisibility
data class SpanishEducationPreparationResult private constructor(
    val traceId: TraceId,
    val status: SpanishEducationPreparationStatus,
    val spanishEducation: SpanishEducationRecord?,
) {
    companion object {

        fun create(
            traceId: TraceId,
            status: SpanishEducationPreparationStatus,
            spanishEducation: SpanishEducationRecord? = null,
        ): SpanishEducationPreparationResult {
            when (status) {
                SpanishEducationPreparationStatus.PREPARED -> {
                    require(spanishEducation != null) {
                        "Prepared Spanish Education results require one Spanish Education context."
                    }
                }

                SpanishEducationPreparationStatus.DEFERRED -> {
                    require(spanishEducation == null) {
                        "Deferred Spanish Education results must not contain a Spanish Education context."
                    }
                }
            }

            return SpanishEducationPreparationResult(
                traceId = traceId,
                status = status,
                spanishEducation = spanishEducation,
            )
        }
    }
}
