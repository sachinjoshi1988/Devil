package com.devil.core.model.education

/**
 * Immutable Stage 135 representation of one bounded German Education context.
 *
 * This record preserves:
 *
 * - one existing Stage 133 Multilingual Teaching context whose preserved
 *   Stage 120 target language is German;
 * - one explicitly supplied nonblank German learning focus;
 * - one explicitly supplied nonblank German learning objective.
 *
 * This record does not:
 *
 * - perform translation;
 * - teach vocabulary or grammar;
 * - conduct conversation;
 * - recognize or synthesize speech;
 * - assess pronunciation;
 * - infer or verify proficiency;
 * - generate or execute curriculum;
 * - establish constitutional Observation, Verification, or Outcome;
 * - perform constitutional Learning;
 * - create Memory;
 * - or persist learner progress.
 *
 * GERMAN_EDUCATION != TRANSLATION_ENGINE.
 * GERMAN_CONTEXT != GERMAN_TAUGHT.
 * GERMAN_CONTEXT != PRONUNCIATION_VERIFIED.
 * GERMAN_CONTEXT != CONVERSATION_COMPLETED.
 * GERMAN_CONTEXT != VERIFIED_PROFICIENCY.
 */
@ConsistentCopyVisibility
data class GermanEducationRecord private constructor(
    val multilingualTeaching: MultilingualTeachingRecord,
    val germanLearningFocus: String,
    val germanLearningObjective: String,
) {
    companion object {

        fun create(
            multilingualTeaching: MultilingualTeachingRecord,
            germanLearningFocus: String,
            germanLearningObjective: String,
        ): GermanEducationRecord {
            require(
                multilingualTeaching.languageEducationSession.targetLanguage.equals(
                    other = "German",
                    ignoreCase = true,
                ),
            ) {
                "German Education requires German as the target language."
            }

            val normalizedGermanLearningFocus =
                germanLearningFocus.trim()

            val normalizedGermanLearningObjective =
                germanLearningObjective.trim()

            require(normalizedGermanLearningFocus.isNotEmpty()) {
                "German Education learning focus must not be blank."
            }

            require(normalizedGermanLearningObjective.isNotEmpty()) {
                "German Education learning objective must not be blank."
            }

            return GermanEducationRecord(
                multilingualTeaching = multilingualTeaching,
                germanLearningFocus = normalizedGermanLearningFocus,
                germanLearningObjective = normalizedGermanLearningObjective,
            )
        }
    }
}
