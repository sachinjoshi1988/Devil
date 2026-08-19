package com.devil.core.runtime.education

import com.devil.core.model.common.TraceId
import com.devil.core.model.education.PronunciationPracticeRecord

/**
 * Stable Stage 123 result of bounded Pronunciation Intelligence preparation.
 *
 * PREPARED requires one PronunciationPracticeRecord.
 * DEFERRED must not contain one.
 *
 * This result creates no speech-recognition result, phoneme analysis,
 * pronunciation score, accent classification, verified pronunciation,
 * Brain decision, Task, Plan, execution, Observation, Verification, Outcome,
 * constitutional Learning, Memory commitment, or verified learner progress.
 */
@ConsistentCopyVisibility
data class PronunciationIntelligencePreparationResult private constructor(
    val traceId: TraceId,
    val status: PronunciationIntelligencePreparationStatus,
    val practice: PronunciationPracticeRecord?,
) {
    companion object {

        fun create(
            traceId: TraceId,
            status: PronunciationIntelligencePreparationStatus,
            practice: PronunciationPracticeRecord? = null,
        ): PronunciationIntelligencePreparationResult {
            when (status) {
                PronunciationIntelligencePreparationStatus.PREPARED -> {
                    require(practice != null) {
                        "Prepared Pronunciation Intelligence results require one practice context."
                    }
                }

                PronunciationIntelligencePreparationStatus.DEFERRED -> {
                    require(practice == null) {
                        "Deferred Pronunciation Intelligence results must not contain a practice context."
                    }
                }
            }

            return PronunciationIntelligencePreparationResult(
                traceId = traceId,
                status = status,
                practice = practice,
            )
        }
    }
}
