package com.devil.core.runtime.education

import com.devil.core.model.common.TraceId
import com.devil.core.model.education.GrammarInConversationPracticeRecord
import com.devil.core.model.education.SpokenEnglishConversationPracticeRecord

/**
 * Stage 125 bounded Grammar-in-Conversation coordinator.
 *
 * This coordinator prepares one Education Domain grammar-practice context from
 * an existing Stage 122 Spoken English conversation-practice context and one
 * explicitly supplied grammar target.
 *
 * Stage 123 Pronunciation Intelligence and Stage 124 Listening Comprehension
 * are sibling capabilities, not required predecessors.
 *
 * It does not:
 *
 * - parse arbitrary speech or text for grammar;
 * - detect, correct, or score grammatical errors;
 * - verify grammar correctness or mastery;
 * - assess proficiency or learner progress;
 * - implement reading, vocabulary, or writing capabilities;
 * - invoke speech recognition or voice APIs;
 * - create another intelligence, Brain, Executive, Planner, Memory Authority,
 *   or Security Authority;
 * - create Tasks or Plans;
 * - invoke execution;
 * - establish Observation, Verification, or Outcome;
 * - perform constitutional Learning;
 * - create or persist Memory;
 * - or communicate with Android or platform APIs.
 *
 * PREPARED != TAUGHT.
 * PREPARED != GRAMMAR_CORRECT.
 * PREPARED != MASTERY_VERIFIED.
 */
class GrammarInConversationCoordinator {

    fun prepare(
        traceId: TraceId,
        conversationPractice: SpokenEnglishConversationPracticeRecord,
        grammarTarget: String,
    ): GrammarInConversationPreparationResult {
        if (grammarTarget.isBlank()) {
            return deferred(
                traceId = traceId,
            )
        }

        val practice =
            GrammarInConversationPracticeRecord.create(
                conversationPractice = conversationPractice,
                grammarTarget = grammarTarget,
            )

        return GrammarInConversationPreparationResult.create(
            traceId = traceId,
            status = GrammarInConversationPreparationStatus.PREPARED,
            practice = practice,
        )
    }

    private fun deferred(
        traceId: TraceId,
    ): GrammarInConversationPreparationResult {
        return GrammarInConversationPreparationResult.create(
            traceId = traceId,
            status = GrammarInConversationPreparationStatus.DEFERRED,
        )
    }
}
