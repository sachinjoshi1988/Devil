package com.devil.core.model.education

/**
 * Immutable Stage 125 representation of one bounded Grammar-in-Conversation
 * practice context.
 *
 * This record preserves:
 *
 * - one existing Stage 122 Spoken English conversation-practice context;
 * - one explicitly supplied nonblank grammar target.
 *
 * This record does not parse arbitrary speech, detect grammatical errors,
 * correct learner output, score grammar, verify mastery, execute actions,
 * perform constitutional Learning, create Memory, or persist learner progress.
 *
 * GRAMMAR_IN_CONVERSATION != GENERAL_CONVERSATION_DOMAIN.
 * GRAMMAR_TARGET != GRAMMAR_MASTERY.
 * GRAMMAR_CONTEXT != VERIFIED_PROFICIENCY.
 */
@ConsistentCopyVisibility
data class GrammarInConversationPracticeRecord private constructor(
    val conversationPractice: SpokenEnglishConversationPracticeRecord,
    val grammarTarget: String,
) {
    companion object {

        fun create(
            conversationPractice: SpokenEnglishConversationPracticeRecord,
            grammarTarget: String,
        ): GrammarInConversationPracticeRecord {
            val normalizedGrammarTarget = grammarTarget.trim()

            require(normalizedGrammarTarget.isNotEmpty()) {
                "Grammar-in-Conversation target must not be blank."
            }

            return GrammarInConversationPracticeRecord(
                conversationPractice = conversationPractice,
                grammarTarget = normalizedGrammarTarget,
            )
        }
    }
}
