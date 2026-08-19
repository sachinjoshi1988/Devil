package com.devil.core.model.education

/**
 * Immutable Stage 126 representation of one bounded Reading & Vocabulary
 * Development practice context.
 *
 * This record preserves:
 *
 * - one existing Stage 120 Language Education session;
 * - one explicitly supplied nonblank reading target or material description;
 * - one explicitly supplied nonblank vocabulary target.
 *
 * This record does not score reading comprehension, generate verified
 * definitions, verify vocabulary mastery, execute actions, perform
 * constitutional Learning, create Memory, or persist learner progress.
 *
 * READING_TARGET != READING_COMPREHENSION_VERIFIED.
 * VOCABULARY_TARGET != VOCABULARY_MASTERED.
 * READING_VOCABULARY_CONTEXT != VERIFIED_PROFICIENCY.
 */
@ConsistentCopyVisibility
data class ReadingVocabularyPracticeRecord private constructor(
    val languageEducationSession: LanguageEducationSessionRecord,
    val readingTarget: String,
    val vocabularyTarget: String,
) {
    companion object {

        fun create(
            languageEducationSession: LanguageEducationSessionRecord,
            readingTarget: String,
            vocabularyTarget: String,
        ): ReadingVocabularyPracticeRecord {
            val normalizedReadingTarget = readingTarget.trim()
            val normalizedVocabularyTarget = vocabularyTarget.trim()

            require(normalizedReadingTarget.isNotEmpty()) {
                "Reading target must not be blank."
            }

            require(normalizedVocabularyTarget.isNotEmpty()) {
                "Vocabulary target must not be blank."
            }

            return ReadingVocabularyPracticeRecord(
                languageEducationSession = languageEducationSession,
                readingTarget = normalizedReadingTarget,
                vocabularyTarget = normalizedVocabularyTarget,
            )
        }
    }
}
