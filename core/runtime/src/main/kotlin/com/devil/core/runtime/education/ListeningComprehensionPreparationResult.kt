package com.devil.core.runtime.education

import com.devil.core.model.common.TraceId
import com.devil.core.model.education.ListeningComprehensionPracticeRecord

/**
 * Stable Stage 124 result of bounded Listening Comprehension preparation.
 *
 * PREPARED requires one ListeningComprehensionPracticeRecord.
 * DEFERRED must not contain one.
 *
 * This result creates no audio evidence, speech-recognition result,
 * transcription, comprehension score, verified understanding, Brain decision,
 * Task, Plan, execution, Observation, Verification, Outcome, constitutional
 * Learning, Memory commitment, or verified learner progress.
 */
@ConsistentCopyVisibility
data class ListeningComprehensionPreparationResult private constructor(
    val traceId: TraceId,
    val status: ListeningComprehensionPreparationStatus,
    val practice: ListeningComprehensionPracticeRecord?,
) {
    companion object {

        fun create(
            traceId: TraceId,
            status: ListeningComprehensionPreparationStatus,
            practice: ListeningComprehensionPracticeRecord? = null,
        ): ListeningComprehensionPreparationResult {
            when (status) {
                ListeningComprehensionPreparationStatus.PREPARED -> {
                    require(practice != null) {
                        "Prepared Listening Comprehension results require one practice context."
                    }
                }

                ListeningComprehensionPreparationStatus.DEFERRED -> {
                    require(practice == null) {
                        "Deferred Listening Comprehension results must not contain a practice context."
                    }
                }
            }

            return ListeningComprehensionPreparationResult(
                traceId = traceId,
                status = status,
                practice = practice,
            )
        }
    }
}
