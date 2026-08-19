package com.devil.core.runtime.education

import com.devil.core.model.common.TraceId
import com.devil.core.model.education.WritingCommunicationPracticeRecord

/**
 * Stable Stage 127 result of bounded Writing & Communication preparation.
 *
 * PREPARED requires one WritingCommunicationPracticeRecord.
 * DEFERRED must not contain one.
 *
 * This result creates no outbound message, communication authorization,
 * writing-quality score, proficiency claim, Brain decision, Task, Plan,
 * execution, Observation, Verification, Outcome, constitutional Learning,
 * Memory commitment, or verified learner progress.
 */
@ConsistentCopyVisibility
data class WritingCommunicationPreparationResult private constructor(
    val traceId: TraceId,
    val status: WritingCommunicationPreparationStatus,
    val practice: WritingCommunicationPracticeRecord?,
) {
    companion object {

        fun create(
            traceId: TraceId,
            status: WritingCommunicationPreparationStatus,
            practice: WritingCommunicationPracticeRecord? = null,
        ): WritingCommunicationPreparationResult {
            when (status) {
                WritingCommunicationPreparationStatus.PREPARED -> {
                    require(practice != null) {
                        "Prepared Writing & Communication results require one practice context."
                    }
                }

                WritingCommunicationPreparationStatus.DEFERRED -> {
                    require(practice == null) {
                        "Deferred Writing & Communication results must not contain a practice context."
                    }
                }
            }

            return WritingCommunicationPreparationResult(
                traceId = traceId,
                status = status,
                practice = practice,
            )
        }
    }
}
