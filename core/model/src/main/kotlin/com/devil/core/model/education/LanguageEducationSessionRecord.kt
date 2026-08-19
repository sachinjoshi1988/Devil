package com.devil.core.model.education

/**
 * Immutable Stage 120 representation of one bounded Language Education
 * foundation session.
 *
 * This record extends an already prepared Stage 85 EducationSessionRecord with
 * one explicitly supplied target language.
 *
 * It does not infer a language, create curriculum, generate lessons, conduct
 * conversation practice, evaluate pronunciation, assess mastery, authorize
 * anything, execute actions, perform constitutional Learning, create Memory,
 * or persist learner progress.
 *
 * LANGUAGE_EDUCATION = DOMAIN OF THE ONE DEVIL INTELLIGENCE.
 * LANGUAGE_EDUCATION != ANOTHER INTELLIGENCE.
 * LANGUAGE_EDUCATION_SESSION != AUTHORIZATION.
 * LANGUAGE_EDUCATION_SESSION != VERIFIED_LEARNING_PROGRESS.
 */
@ConsistentCopyVisibility
data class LanguageEducationSessionRecord private constructor(
    val educationSession: EducationSessionRecord,
    val targetLanguage: String,
) {
    companion object {

        fun create(
            educationSession: EducationSessionRecord,
            targetLanguage: String,
        ): LanguageEducationSessionRecord {
            val normalizedTargetLanguage = targetLanguage.trim()

            require(normalizedTargetLanguage.isNotEmpty()) {
                "Language Education target language must not be blank."
            }

            return LanguageEducationSessionRecord(
                educationSession = educationSession,
                targetLanguage = normalizedTargetLanguage,
            )
        }
    }
}
