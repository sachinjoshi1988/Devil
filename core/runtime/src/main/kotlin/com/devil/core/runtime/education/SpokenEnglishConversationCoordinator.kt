package com.devil.core.runtime.education

import com.devil.core.model.common.TraceId
import com.devil.core.model.education.SpokenEnglishBeginnerSessionRecord
import com.devil.core.model.education.SpokenEnglishConversationPracticeRecord

/**
 * Stage 122 bounded Spoken English Conversation coordinator.
 *
 * This coordinator prepares one Education Domain conversation-practice context
 * from an existing Stage 121 Spoken English Beginner session and an explicitly
 * supplied topic.
 *
 * It does not create or replace Devil's general Conversation Domain.
 *
 * It does not:
 *
 * - create ConversationId, ConversationInput, ConversationRecord, or
 *   conversation persistence;
 * - create another intelligence, Brain, Constitution, Executive, Planner,
 *   Memory Authority, or Security Authority;
 * - authenticate or authorize a learner;
 * - conduct or complete an actual multi-turn conversation;
 * - evaluate pronunciation or listening comprehension;
 * - teach or assess grammar;
 * - assess proficiency or learner progress;
 * - create curriculum;
 * - create Tasks or Plans;
 * - invoke execution;
 * - establish Observation, Verification, or Outcome;
 * - perform constitutional Learning;
 * - create or persist Memory;
 * - or communicate with Android, microphone, speech-recognition, or voice APIs.
 *
 * EDUCATION_CONVERSATION_PRACTICE != CONVERSATION_DOMAIN.
 * PREPARED != CONVERSATION_COMPLETED.
 * PRACTICE_CONTEXT != VERIFIED_PROFICIENCY.
 */
class SpokenEnglishConversationCoordinator {

    fun prepare(
        traceId: TraceId,
        beginnerSession: SpokenEnglishBeginnerSessionRecord,
        topic: String,
    ): SpokenEnglishConversationPreparationResult {
        if (topic.isBlank()) {
            return deferred(
                traceId = traceId,
            )
        }

        val practice =
            SpokenEnglishConversationPracticeRecord.create(
                beginnerSession = beginnerSession,
                topic = topic,
            )

        return SpokenEnglishConversationPreparationResult.create(
            traceId = traceId,
            status = SpokenEnglishConversationPreparationStatus.PREPARED,
            practice = practice,
        )
    }

    private fun deferred(
        traceId: TraceId,
    ): SpokenEnglishConversationPreparationResult {
        return SpokenEnglishConversationPreparationResult.create(
            traceId = traceId,
            status = SpokenEnglishConversationPreparationStatus.DEFERRED,
        )
    }
}
