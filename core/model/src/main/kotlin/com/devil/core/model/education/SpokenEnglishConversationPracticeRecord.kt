package com.devil.core.model.education

/**
 * Immutable Stage 122 representation of one bounded Spoken English
 * conversation-practice context.
 *
 * This record preserves:
 *
 * - one existing Stage 121 Spoken English Beginner session;
 * - one explicitly supplied nonblank conversation-practice topic.
 *
 * This is an Education Domain activity context only.
 *
 * It does not create ConversationId, ConversationInput, ConversationRecord,
 * multi-turn ordering, conversation persistence, pronunciation assessment,
 * listening assessment, grammar coaching, proficiency verification, execution,
 * constitutional Learning, Memory, or learner-progress persistence.
 *
 * SPOKEN_ENGLISH_CONVERSATION_PRACTICE != CONVERSATION_DOMAIN.
 * PRACTICE_CONTEXT != CONVERSATION_COMPLETED.
 * PRACTICE_CONTEXT != VERIFIED_PROFICIENCY.
 */
@ConsistentCopyVisibility
data class SpokenEnglishConversationPracticeRecord private constructor(
    val beginnerSession: SpokenEnglishBeginnerSessionRecord,
    val topic: String,
) {
    companion object {

        fun create(
            beginnerSession: SpokenEnglishBeginnerSessionRecord,
            topic: String,
        ): SpokenEnglishConversationPracticeRecord {
            val normalizedTopic = topic.trim()

            require(normalizedTopic.isNotEmpty()) {
                "Spoken English conversation-practice topic must not be blank."
            }

            return SpokenEnglishConversationPracticeRecord(
                beginnerSession = beginnerSession,
                topic = normalizedTopic,
            )
        }
    }
}
