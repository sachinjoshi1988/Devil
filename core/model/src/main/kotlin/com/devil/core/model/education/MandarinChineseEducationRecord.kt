package com.devil.core.model.education

/**
 * Immutable Stage 138 representation of one bounded Mandarin Chinese Education context.
 *
 * This record preserves:
 *
 * - one existing Stage 133 Multilingual Teaching context whose preserved
 *   Stage 120 target language is Mandarin Chinese;
 * - one explicitly supplied nonblank Mandarin Chinese learning focus;
 * - one explicitly supplied nonblank Mandarin Chinese learning objective.
 *
 * This record does not:
 *
 * - perform translation;
 * - teach vocabulary, grammar, characters, pinyin, or tones;
 * - conduct conversation;
 * - recognize or synthesize speech;
 * - assess pronunciation or tones;
 * - infer or verify proficiency;
 * - generate or execute curriculum;
 * - establish constitutional Observation, Verification, or Outcome;
 * - perform constitutional Learning;
 * - create Memory;
 * - or persist learner progress.
 *
 * MANDARIN_CHINESE_EDUCATION != TRANSLATION_ENGINE.
 * MANDARIN_CHINESE_CONTEXT != MANDARIN_CHINESE_TAUGHT.
 * MANDARIN_CHINESE_CONTEXT != PRONUNCIATION_VERIFIED.
 * MANDARIN_CHINESE_CONTEXT != CONVERSATION_COMPLETED.
 * MANDARIN_CHINESE_CONTEXT != VERIFIED_PROFICIENCY.
 */
@ConsistentCopyVisibility
data class MandarinChineseEducationRecord private constructor(
    val multilingualTeaching: MultilingualTeachingRecord,
    val mandarinChineseLearningFocus: String,
    val mandarinChineseLearningObjective: String,
) {
    companion object {

        fun create(
            multilingualTeaching: MultilingualTeachingRecord,
            mandarinChineseLearningFocus: String,
            mandarinChineseLearningObjective: String,
        ): MandarinChineseEducationRecord {
            require(
                multilingualTeaching
                    .languageEducationSession
                    .targetLanguage
                    .equals(
                        other = "Mandarin Chinese",
                        ignoreCase = true,
                    ),
            ) {
                "Mandarin Chinese Education requires Mandarin Chinese as the target language."
            }

            val normalizedMandarinChineseLearningFocus =
                mandarinChineseLearningFocus.trim()

            val normalizedMandarinChineseLearningObjective =
                mandarinChineseLearningObjective.trim()

            require(
                normalizedMandarinChineseLearningFocus.isNotEmpty(),
            ) {
                "Mandarin Chinese Education learning focus must not be blank."
            }

            require(
                normalizedMandarinChineseLearningObjective.isNotEmpty(),
            ) {
                "Mandarin Chinese Education learning objective must not be blank."
            }

            return MandarinChineseEducationRecord(
                multilingualTeaching = multilingualTeaching,
                mandarinChineseLearningFocus =
                    normalizedMandarinChineseLearningFocus,
                mandarinChineseLearningObjective =
                    normalizedMandarinChineseLearningObjective,
            )
        }
    }
}
