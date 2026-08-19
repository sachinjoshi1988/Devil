package com.devil.core.model.education

/**
 * Immutable Stage 137 representation of one bounded Russian Education context.
 *
 * This record preserves:
 *
 * - one existing Stage 133 Multilingual Teaching context whose preserved
 *   Stage 120 target language is Russian;
 * - one explicitly supplied nonblank Russian learning focus;
 * - one explicitly supplied nonblank Russian learning objective.
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
 * RUSSIAN_EDUCATION != TRANSLATION_ENGINE.
 * RUSSIAN_CONTEXT != RUSSIAN_TAUGHT.
 * RUSSIAN_CONTEXT != PRONUNCIATION_VERIFIED.
 * RUSSIAN_CONTEXT != CONVERSATION_COMPLETED.
 * RUSSIAN_CONTEXT != VERIFIED_PROFICIENCY.
 */
@ConsistentCopyVisibility
data class RussianEducationRecord private constructor(
    val multilingualTeaching: MultilingualTeachingRecord,
    val russianLearningFocus: String,
    val russianLearningObjective: String,
) {
    companion object {

        fun create(
            multilingualTeaching: MultilingualTeachingRecord,
            russianLearningFocus: String,
            russianLearningObjective: String,
        ): RussianEducationRecord {
            require(
                multilingualTeaching.languageEducationSession.targetLanguage.equals(
                    other = "Russian",
                    ignoreCase = true,
                ),
            ) {
                "Russian Education requires Russian as the target language."
            }

            val normalizedRussianLearningFocus =
                russianLearningFocus.trim()

            val normalizedRussianLearningObjective =
                russianLearningObjective.trim()

            require(normalizedRussianLearningFocus.isNotEmpty()) {
                "Russian Education learning focus must not be blank."
            }

            require(normalizedRussianLearningObjective.isNotEmpty()) {
                "Russian Education learning objective must not be blank."
            }

            return RussianEducationRecord(
                multilingualTeaching = multilingualTeaching,
                russianLearningFocus = normalizedRussianLearningFocus,
                russianLearningObjective = normalizedRussianLearningObjective,
            )
        }
    }
}
