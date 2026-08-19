package com.devil.core.model.education

/**
 * Immutable Stage 123 representation of one bounded pronunciation-practice
 * context.
 *
 * This record preserves:
 *
 * - one existing Stage 122 Spoken English conversation-practice context;
 * - one explicitly supplied nonblank pronunciation target.
 *
 * The target may describe a word or phrase selected for pronunciation work.
 *
 * This record does not capture audio, recognize speech, extract phonemes,
 * classify accent, calculate pronunciation scores, verify pronunciation,
 * assess proficiency, execute actions, perform constitutional Learning,
 * create Memory, or persist learner progress.
 *
 * PRONUNCIATION_INTELLIGENCE != SPEECH_RECOGNITION.
 * PRONUNCIATION_TARGET != VERIFIED_PRONUNCIATION.
 * PRONUNCIATION_CONTEXT != VERIFIED_PROFICIENCY.
 */
@ConsistentCopyVisibility
data class PronunciationPracticeRecord private constructor(
    val conversationPractice: SpokenEnglishConversationPracticeRecord,
    val target: String,
) {
    companion object {

        fun create(
            conversationPractice: SpokenEnglishConversationPracticeRecord,
            target: String,
        ): PronunciationPracticeRecord {
            val normalizedTarget = target.trim()

            require(normalizedTarget.isNotEmpty()) {
                "Pronunciation target must not be blank."
            }

            return PronunciationPracticeRecord(
                conversationPractice = conversationPractice,
                target = normalizedTarget,
            )
        }
    }
}
