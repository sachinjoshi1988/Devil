package com.devil.core.model.education

/**
 * Immutable Stage 127 representation of one bounded Writing & Communication
 * educational practice context.
 *
 * This record preserves:
 *
 * - one existing Stage 120 Language Education session;
 * - one explicitly supplied nonblank writing target;
 * - one explicitly supplied nonblank communication purpose.
 *
 * This record does not send messages, create external communication authority,
 * execute actions, score writing quality, verify proficiency, perform
 * constitutional Learning, create Memory, or persist learner progress.
 *
 * WRITING_COMMUNICATION_EDUCATION != EXTERNAL_COMMUNICATION.
 * WRITING_TARGET != MESSAGE_SENT.
 * WRITING_CONTEXT != WRITING_QUALITY_VERIFIED.
 */
@ConsistentCopyVisibility
data class WritingCommunicationPracticeRecord private constructor(
    val languageEducationSession: LanguageEducationSessionRecord,
    val writingTarget: String,
    val communicationPurpose: String,
) {
    companion object {

        fun create(
            languageEducationSession: LanguageEducationSessionRecord,
            writingTarget: String,
            communicationPurpose: String,
        ): WritingCommunicationPracticeRecord {
            val normalizedWritingTarget = writingTarget.trim()
            val normalizedCommunicationPurpose = communicationPurpose.trim()

            require(normalizedWritingTarget.isNotEmpty()) {
                "Writing target must not be blank."
            }

            require(normalizedCommunicationPurpose.isNotEmpty()) {
                "Communication purpose must not be blank."
            }

            return WritingCommunicationPracticeRecord(
                languageEducationSession = languageEducationSession,
                writingTarget = normalizedWritingTarget,
                communicationPurpose = normalizedCommunicationPurpose,
            )
        }
    }
}
