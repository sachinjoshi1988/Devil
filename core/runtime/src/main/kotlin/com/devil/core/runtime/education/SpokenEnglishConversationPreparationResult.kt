package com.devil.core.runtime.education

import com.devil.core.model.common.TraceId
import com.devil.core.model.education.SpokenEnglishConversationPracticeRecord

/**
 * Stable Stage 122 result of bounded Spoken English conversation preparation.
 *
 * PREPARED requires one SpokenEnglishConversationPracticeRecord.
 * DEFERRED must not contain one.
 *
 * This result creates no general Conversation Domain identity or record,
 * Brain, Decision, Task, Plan, capability authority, execution, Observation,
 * Verification, Outcome, constitutional Learning, Memory commitment,
 * pronunciation assessment, listening assessment, grammar assessment,
 * proficiency result, or verified learner progress.
 */
@ConsistentCopyVisibility
data class SpokenEnglishConversationPreparationResult private constructor(
    val traceId: TraceId,
    val status: SpokenEnglishConversationPreparationStatus,
    val practice: SpokenEnglishConversationPracticeRecord?,
) {
    companion object {

        fun create(
            traceId: TraceId,
            status: SpokenEnglishConversationPreparationStatus,
            practice: SpokenEnglishConversationPracticeRecord? = null,
        ): SpokenEnglishConversationPreparationResult {
            when (status) {
                SpokenEnglishConversationPreparationStatus.PREPARED -> {
                    require(practice != null) {
                        "Prepared Spoken English conversation results require one practice context."
                    }
                }

                SpokenEnglishConversationPreparationStatus.DEFERRED -> {
                    require(practice == null) {
                        "Deferred Spoken English conversation results must not contain a practice context."
                    }
                }
            }

            return SpokenEnglishConversationPreparationResult(
                traceId = traceId,
                status = status,
                practice = practice,
            )
        }
    }
}
