package com.devil.core.model.education

/**
 * Immutable Stage 121 representation of one bounded Spoken English Beginner
 * learning context.
 *
 * This record preserves an already prepared Stage 120 Language Education
 * session whose explicitly supplied target language is English.
 *
 * BEGINNER here identifies the selected educational context only.
 * It is not a measured or verified statement about learner proficiency.
 *
 * This record does not conduct conversation practice, evaluate pronunciation,
 * assess listening, teach grammar, verify mastery, create curriculum, execute
 * actions, perform constitutional Learning, create Memory, or persist progress.
 *
 * SPOKEN_ENGLISH_BEGINNER != ANOTHER INTELLIGENCE.
 * BEGINNER_CONTEXT != VERIFIED_PROFICIENCY.
 * BEGINNER_CONTEXT != VERIFIED_PROGRESS.
 */
@ConsistentCopyVisibility
data class SpokenEnglishBeginnerSessionRecord private constructor(
    val languageEducationSession: LanguageEducationSessionRecord,
) {
    companion object {

        fun create(
            languageEducationSession: LanguageEducationSessionRecord,
        ): SpokenEnglishBeginnerSessionRecord {
            require(
                languageEducationSession.targetLanguage.equals(
                    other = "English",
                    ignoreCase = true,
                ),
            ) {
                "Spoken English Beginner requires English as the target language."
            }

            return SpokenEnglishBeginnerSessionRecord(
                languageEducationSession = languageEducationSession,
            )
        }
    }
}
