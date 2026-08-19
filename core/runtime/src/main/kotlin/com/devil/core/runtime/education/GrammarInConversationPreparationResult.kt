package com.devil.core.runtime.education

import com.devil.core.model.common.TraceId
import com.devil.core.model.education.GrammarInConversationPracticeRecord

/**
 * Stable Stage 125 result of bounded Grammar-in-Conversation preparation.
 *
 * PREPARED requires one GrammarInConversationPracticeRecord.
 * DEFERRED must not contain one.
 *
 * This result creates no grammar parse result, correction result, grammar score,
 * mastery claim, Brain decision, Task, Plan, execution, Observation,
 * Verification, Outcome, constitutional Learning, Memory commitment, or
 * verified learner progress.
 */
@ConsistentCopyVisibility
data class GrammarInConversationPreparationResult private constructor(
    val traceId: TraceId,
    val status: GrammarInConversationPreparationStatus,
    val practice: GrammarInConversationPracticeRecord?,
) {
    companion object {

        fun create(
            traceId: TraceId,
            status: GrammarInConversationPreparationStatus,
            practice: GrammarInConversationPracticeRecord? = null,
        ): GrammarInConversationPreparationResult {
            when (status) {
                GrammarInConversationPreparationStatus.PREPARED -> {
                    require(practice != null) {
                        "Prepared Grammar-in-Conversation results require one practice context."
                    }
                }

                GrammarInConversationPreparationStatus.DEFERRED -> {
                    require(practice == null) {
                        "Deferred Grammar-in-Conversation results must not contain a practice context."
                    }
                }
            }

            return GrammarInConversationPreparationResult(
                traceId = traceId,
                status = status,
                practice = practice,
            )
        }
    }
}
