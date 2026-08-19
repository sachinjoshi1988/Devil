package com.devil.core.runtime.education

import com.devil.core.model.common.TraceId
import com.devil.core.model.education.EnglishConfidenceCoachPracticeRecord

/**
 * Stable Stage 128 result of bounded English Confidence Coach preparation.
 *
 * PREPARED requires one EnglishConfidenceCoachPracticeRecord.
 * DEFERRED must not contain one.
 *
 * This result creates no confidence score, psychological assessment,
 * improvement claim, Brain decision, Task, Plan, execution, Observation,
 * Verification, Outcome, constitutional Learning, Memory commitment,
 * or verified learner progress.
 */
@ConsistentCopyVisibility
data class EnglishConfidenceCoachPreparationResult private constructor(
    val traceId: TraceId,
    val status: EnglishConfidenceCoachPreparationStatus,
    val practice: EnglishConfidenceCoachPracticeRecord?,
) {
    companion object {

        fun create(
            traceId: TraceId,
            status: EnglishConfidenceCoachPreparationStatus,
            practice: EnglishConfidenceCoachPracticeRecord? = null,
        ): EnglishConfidenceCoachPreparationResult {
            when (status) {
                EnglishConfidenceCoachPreparationStatus.PREPARED -> {
                    require(practice != null) {
                        "Prepared English Confidence Coach results require one practice context."
                    }
                }

                EnglishConfidenceCoachPreparationStatus.DEFERRED -> {
                    require(practice == null) {
                        "Deferred English Confidence Coach results must not contain a practice context."
                    }
                }
            }

            return EnglishConfidenceCoachPreparationResult(
                traceId = traceId,
                status = status,
                practice = practice,
            )
        }
    }
}
