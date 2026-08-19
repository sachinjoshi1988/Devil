package com.devil.core.runtime.education

import com.devil.core.model.common.TraceId
import com.devil.core.model.education.ReadingVocabularyPracticeRecord

/**
 * Stable Stage 126 result of bounded Reading & Vocabulary Development
 * preparation.
 *
 * PREPARED requires one ReadingVocabularyPracticeRecord.
 * DEFERRED must not contain one.
 *
 * This result creates no comprehension score, vocabulary mastery claim,
 * dictionary/provider result, Brain decision, Task, Plan, execution,
 * Observation, Verification, Outcome, constitutional Learning,
 * Memory commitment, or verified learner progress.
 */
@ConsistentCopyVisibility
data class ReadingVocabularyPreparationResult private constructor(
    val traceId: TraceId,
    val status: ReadingVocabularyPreparationStatus,
    val practice: ReadingVocabularyPracticeRecord?,
) {
    companion object {

        fun create(
            traceId: TraceId,
            status: ReadingVocabularyPreparationStatus,
            practice: ReadingVocabularyPracticeRecord? = null,
        ): ReadingVocabularyPreparationResult {
            when (status) {
                ReadingVocabularyPreparationStatus.PREPARED -> {
                    require(practice != null) {
                        "Prepared Reading & Vocabulary results require one practice context."
                    }
                }

                ReadingVocabularyPreparationStatus.DEFERRED -> {
                    require(practice == null) {
                        "Deferred Reading & Vocabulary results must not contain a practice context."
                    }
                }
            }

            return ReadingVocabularyPreparationResult(
                traceId = traceId,
                status = status,
                practice = practice,
            )
        }
    }
}
