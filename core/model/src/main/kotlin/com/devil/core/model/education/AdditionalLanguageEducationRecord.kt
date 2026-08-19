package com.devil.core.model.education

/**
 * Immutable Stage 139 representation of one bounded Additional Language
 * Expansion education context.
 *
 * This record preserves:
 *
 * - one existing Stage 133 Multilingual Teaching context;
 * - the exact target language already owned by its Stage 120
 *   LanguageEducationSessionRecord;
 * - one explicitly supplied nonblank additional-language learning focus;
 * - one explicitly supplied nonblank additional-language learning objective.
 *
 * Stage 139 is reserved for languages that do not already have dedicated
 * Stage 134-138 Education specializations.
 *
 * Therefore this generic expansion context must not be used for:
 *
 * - French;
 * - German;
 * - Spanish;
 * - Russian;
 * - Mandarin Chinese.
 *
 * Those languages remain governed by their dedicated sibling stages.
 *
 * This record does not:
 *
 * - create a new language identity;
 * - perform translation;
 * - teach vocabulary, grammar, writing systems, pronunciation, or culture;
 * - conduct conversation;
 * - recognize or synthesize speech;
 * - infer or verify proficiency;
 * - generate or execute curriculum;
 * - establish constitutional Observation, Verification, or Outcome;
 * - perform constitutional Learning;
 * - create Memory;
 * - or persist learner progress.
 *
 * ADDITIONAL_LANGUAGE_EXPANSION != LANGUAGE_REGISTRY.
 * ADDITIONAL_LANGUAGE_CONTEXT != LANGUAGE_TAUGHT.
 * ADDITIONAL_LANGUAGE_CONTEXT != TRANSLATION_ENGINE.
 * ADDITIONAL_LANGUAGE_CONTEXT != VERIFIED_PROFICIENCY.
 * DEDICATED_LANGUAGE_SPECIALIZATION != GENERIC_EXPANSION.
 */
@ConsistentCopyVisibility
data class AdditionalLanguageEducationRecord private constructor(
    val multilingualTeaching: MultilingualTeachingRecord,
    val additionalLanguageLearningFocus: String,
    val additionalLanguageLearningObjective: String,
) {
    companion object {

        private val dedicatedLanguages =
            setOf(
                "french",
                "german",
                "spanish",
                "russian",
                "mandarin chinese",
            )

        fun create(
            multilingualTeaching: MultilingualTeachingRecord,
            additionalLanguageLearningFocus: String,
            additionalLanguageLearningObjective: String,
        ): AdditionalLanguageEducationRecord {
            val targetLanguage =
                multilingualTeaching
                    .languageEducationSession
                    .targetLanguage

            require(
                targetLanguage.lowercase() !in dedicatedLanguages,
            ) {
                "Additional Language Expansion cannot replace a dedicated language Education specialization."
            }

            val normalizedFocus =
                additionalLanguageLearningFocus.trim()

            val normalizedObjective =
                additionalLanguageLearningObjective.trim()

            require(normalizedFocus.isNotEmpty()) {
                "Additional Language Expansion learning focus must not be blank."
            }

            require(normalizedObjective.isNotEmpty()) {
                "Additional Language Expansion learning objective must not be blank."
            }

            return AdditionalLanguageEducationRecord(
                multilingualTeaching = multilingualTeaching,
                additionalLanguageLearningFocus = normalizedFocus,
                additionalLanguageLearningObjective = normalizedObjective,
            )
        }
    }
}
