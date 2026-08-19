package com.devil.core.model.education

/**
 * Immutable Stage 124 representation of one bounded Listening Comprehension
 * practice context.
 *
 * This record preserves:
 *
 * - one existing Stage 122 Spoken English conversation-practice context;
 * - one explicitly supplied nonblank listening target or material description.
 *
 * This record does not capture audio, recognize speech, transcribe content,
 * score comprehension, grade answers, verify understanding, execute actions,
 * perform constitutional Learning, create Memory, or persist learner progress.
 *
 * LISTENING_COMPREHENSION != SPEECH_RECOGNITION.
 * LISTENING_CONTEXT != AUDIO_CAPTURE.
 * LISTENING_CONTEXT != COMPREHENSION_VERIFIED.
 * LISTENING_CONTEXT != VERIFIED_PROFICIENCY.
 */
@ConsistentCopyVisibility
data class ListeningComprehensionPracticeRecord private constructor(
    val conversationPractice: SpokenEnglishConversationPracticeRecord,
    val listeningTarget: String,
) {
    companion object {

        fun create(
            conversationPractice: SpokenEnglishConversationPracticeRecord,
            listeningTarget: String,
        ): ListeningComprehensionPracticeRecord {
            val normalizedListeningTarget = listeningTarget.trim()

            require(normalizedListeningTarget.isNotEmpty()) {
                "Listening Comprehension target must not be blank."
            }

            return ListeningComprehensionPracticeRecord(
                conversationPractice = conversationPractice,
                listeningTarget = normalizedListeningTarget,
            )
        }
    }
}
