package com.devil.core.model.education

/**
 * Immutable Stage 133 representation of one bounded Multilingual Teaching
 * Architecture context.
 *
 * This record preserves:
 *
 * - one existing Stage 120 Language Education session;
 * - one explicitly supplied nonblank teaching focus;
 * - one explicitly supplied nonblank teaching objective.
 *
 * The target language remains owned by the preserved LanguageEducationSessionRecord.
 * This record does not create a second language identity or another intelligence.
 *
 * It does not:
 *
 * - implement French, German, Spanish, Russian, Mandarin Chinese, or another
 *   language-specific curriculum;
 * - translate content;
 * - recognize or synthesize speech;
 * - infer learner proficiency;
 * - generate or execute lessons;
 * - establish constitutional Observation, Verification, or Outcome;
 * - perform constitutional Learning;
 * - create Memory;
 * - or persist learner progress.
 *
 * MULTILINGUAL_TEACHING_ARCHITECTURE != ANOTHER_INTELLIGENCE.
 * TARGET_LANGUAGE != LANGUAGE_PROFICIENCY.
 * MULTILINGUAL_CONTEXT != TRANSLATION_ENGINE.
 * MULTILINGUAL_CONTEXT != SPEECH_RECOGNITION.
 * MULTILINGUAL_CONTEXT != LANGUAGE_TAUGHT.
 */
@ConsistentCopyVisibility
data class MultilingualTeachingRecord private constructor(
    val languageEducationSession: LanguageEducationSessionRecord,
    val teachingFocus: String,
    val teachingObjective: String,
) {
    companion object {

        fun create(
            languageEducationSession: LanguageEducationSessionRecord,
            teachingFocus: String,
            teachingObjective: String,
        ): MultilingualTeachingRecord {
            val normalizedTeachingFocus = teachingFocus.trim()
            val normalizedTeachingObjective = teachingObjective.trim()

            require(normalizedTeachingFocus.isNotEmpty()) {
                "Multilingual Teaching focus must not be blank."
            }

            require(normalizedTeachingObjective.isNotEmpty()) {
                "Multilingual Teaching objective must not be blank."
            }

            return MultilingualTeachingRecord(
                languageEducationSession = languageEducationSession,
                teachingFocus = normalizedTeachingFocus,
                teachingObjective = normalizedTeachingObjective,
            )
        }
    }
}
