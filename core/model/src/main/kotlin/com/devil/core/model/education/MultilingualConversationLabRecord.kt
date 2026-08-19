package com.devil.core.model.education

/**
 * Immutable Stage 140 representation of one bounded Multilingual Conversation Lab
 * practice context.
 *
 * This record preserves:
 *
 * - one existing Stage 133 Multilingual Teaching context;
 * - the exact target language already owned by its Stage 120
 *   LanguageEducationSessionRecord;
 * - one explicitly supplied nonblank conversation scenario;
 * - one explicitly supplied nonblank conversation objective.
 *
 * The lab is intentionally language-neutral. French, German, Spanish, Russian,
 * Mandarin Chinese, and Stage 139 additional-language contexts may all use this
 * same bounded conversation-practice architecture.
 *
 * This record does not:
 *
 * - create a new language identity;
 * - perform translation;
 * - conduct or complete an actual conversation;
 * - create the general Conversation Domain;
 * - recognize or synthesize speech;
 * - assess pronunciation;
 * - infer or verify proficiency;
 * - generate or execute curriculum;
 * - establish constitutional Observation, Verification, or Outcome;
 * - perform constitutional Learning;
 * - create Memory;
 * - or persist learner progress.
 *
 * MULTILINGUAL_CONVERSATION_LAB != CONVERSATION_DOMAIN.
 * LAB_CONTEXT != CONVERSATION_OCCURRED.
 * LAB_CONTEXT != TRANSLATION_ENGINE.
 * LAB_CONTEXT != VERIFIED_PROFICIENCY.
 */
@ConsistentCopyVisibility
data class MultilingualConversationLabRecord private constructor(
    val multilingualTeaching: MultilingualTeachingRecord,
    val conversationScenario: String,
    val conversationObjective: String,
) {
    companion object {

        fun create(
            multilingualTeaching: MultilingualTeachingRecord,
            conversationScenario: String,
            conversationObjective: String,
        ): MultilingualConversationLabRecord {
            val normalizedScenario =
                conversationScenario.trim()

            val normalizedObjective =
                conversationObjective.trim()

            require(normalizedScenario.isNotEmpty()) {
                "Multilingual Conversation Lab scenario must not be blank."
            }

            require(normalizedObjective.isNotEmpty()) {
                "Multilingual Conversation Lab objective must not be blank."
            }

            return MultilingualConversationLabRecord(
                multilingualTeaching = multilingualTeaching,
                conversationScenario = normalizedScenario,
                conversationObjective = normalizedObjective,
            )
        }
    }
}
