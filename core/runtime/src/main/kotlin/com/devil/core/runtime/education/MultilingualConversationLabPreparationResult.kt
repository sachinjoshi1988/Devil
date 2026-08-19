package com.devil.core.runtime.education

import com.devil.core.model.common.TraceId
import com.devil.core.model.education.MultilingualConversationLabRecord

/**
 * Stable Stage 140 result of bounded Multilingual Conversation Lab preparation.
 *
 * PREPARED requires one MultilingualConversationLabRecord.
 * DEFERRED must not contain one.
 *
 * This result creates no actual conversation result, translation result,
 * speech result, pronunciation assessment, proficiency claim, Brain decision,
 * Task, Plan, execution, Observation, Verification, Outcome, constitutional
 * Learning, Memory commitment, or verified learner progress.
 */
@ConsistentCopyVisibility
data class MultilingualConversationLabPreparationResult private constructor(
    val traceId: TraceId,
    val status: MultilingualConversationLabPreparationStatus,
    val lab: MultilingualConversationLabRecord?,
) {
    companion object {

        fun create(
            traceId: TraceId,
            status: MultilingualConversationLabPreparationStatus,
            lab: MultilingualConversationLabRecord? = null,
        ): MultilingualConversationLabPreparationResult {
            when (status) {
                MultilingualConversationLabPreparationStatus.PREPARED -> {
                    require(lab != null) {
                        "Prepared Multilingual Conversation Lab results require one lab context."
                    }
                }

                MultilingualConversationLabPreparationStatus.DEFERRED -> {
                    require(lab == null) {
                        "Deferred Multilingual Conversation Lab results must not contain a lab context."
                    }
                }
            }

            return MultilingualConversationLabPreparationResult(
                traceId = traceId,
                status = status,
                lab = lab,
            )
        }
    }
}
