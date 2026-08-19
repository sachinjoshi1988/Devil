package com.devil.core.model.education

/**
 * Immutable Stage 141 representation of one bounded Cross-Language Learning
 * Assistance context.
 *
 * This record preserves:
 *
 * - one existing Stage 140 Multilingual Conversation Lab context;
 * - the exact target language already preserved through Stage 133 and Stage 120;
 * - one explicitly supplied nonblank support language;
 * - one explicitly supplied nonblank cross-language assistance focus;
 * - one explicitly supplied nonblank cross-language assistance objective.
 *
 * The support language must differ from the preserved target language.
 *
 * The support language is not a second target language and does not replace the
 * language identity already owned by LanguageEducationSessionRecord.
 *
 * This record does not:
 *
 * - translate content;
 * - generate bilingual answers;
 * - infer a support language;
 * - detect language automatically;
 * - create a second target language;
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
 * CROSS_LANGUAGE_ASSISTANCE != TRANSLATION_ENGINE.
 * SUPPORT_LANGUAGE != SECOND_TARGET_LANGUAGE.
 * CROSS_LANGUAGE_CONTEXT != TRANSLATION_PERFORMED.
 * CROSS_LANGUAGE_CONTEXT != CONVERSATION_COMPLETED.
 * CROSS_LANGUAGE_CONTEXT != VERIFIED_LEARNING.
 * CROSS_LANGUAGE_CONTEXT != VERIFIED_PROFICIENCY.
 */
@ConsistentCopyVisibility
data class CrossLanguageLearningAssistanceRecord private constructor(
    val conversationLab: MultilingualConversationLabRecord,
    val supportLanguage: String,
    val assistanceFocus: String,
    val assistanceObjective: String,
) {
    companion object {

        fun create(
            conversationLab: MultilingualConversationLabRecord,
            supportLanguage: String,
            assistanceFocus: String,
            assistanceObjective: String,
        ): CrossLanguageLearningAssistanceRecord {
            val normalizedSupportLanguage =
                supportLanguage.trim()

            val normalizedAssistanceFocus =
                assistanceFocus.trim()

            val normalizedAssistanceObjective =
                assistanceObjective.trim()

            require(normalizedSupportLanguage.isNotEmpty()) {
                "Cross-Language Learning Assistance support language must not be blank."
            }

            require(normalizedAssistanceFocus.isNotEmpty()) {
                "Cross-Language Learning Assistance focus must not be blank."
            }

            require(normalizedAssistanceObjective.isNotEmpty()) {
                "Cross-Language Learning Assistance objective must not be blank."
            }

            val targetLanguage =
                conversationLab
                    .multilingualTeaching
                    .languageEducationSession
                    .targetLanguage

            require(
                !normalizedSupportLanguage.equals(
                    other = targetLanguage,
                    ignoreCase = true,
                ),
            ) {
                "Cross-Language Learning Assistance support language must differ from the target language."
            }

            return CrossLanguageLearningAssistanceRecord(
                conversationLab = conversationLab,
                supportLanguage = normalizedSupportLanguage,
                assistanceFocus = normalizedAssistanceFocus,
                assistanceObjective = normalizedAssistanceObjective,
            )
        }
    }
}
