package com.devil.core.model.education

/**
 * Immutable Stage 134 representation of one bounded French Education context.
 *
 * This record preserves:
 *
 * - one existing Stage 133 Multilingual Teaching context;
 * - one explicitly supplied nonblank French learning focus;
 * - one explicitly supplied nonblank French learning objective.
 *
 * The preserved Stage 133 context must ultimately target French through its
 * Stage 120 LanguageEducationSessionRecord.
 *
 * This record does not:
 *
 * - translate content;
 * - generate French vocabulary or grammar instruction;
 * - conduct conversation;
 * - recognize or synthesize speech;
 * - assess pronunciation;
 * - infer proficiency or mastery;
 * - execute curriculum;
 * - establish constitutional Observation, Verification, or Outcome;
 * - perform constitutional Learning;
 * - create Memory;
 * - or persist learner progress.
 *
 * FRENCH_EDUCATION != ANOTHER_INTELLIGENCE.
 * FRENCH_EDUCATION_REQUIRES_TARGET_LANGUAGE_FRENCH.
 * FRENCH_CONTEXT != TRANSLATION_ENGINE.
 * FRENCH_CONTEXT != SPEECH_RECOGNITION.
 * FRENCH_CONTEXT != PRONUNCIATION_VERIFIED.
 * FRENCH_CONTEXT != CONVERSATION_COMPLETED.
 */
@ConsistentCopyVisibility
data class FrenchEducationRecord private constructor(
    val multilingualTeaching: MultilingualTeachingRecord,
    val frenchLearningFocus: String,
    val frenchLearningObjective: String,
) {
    companion object {

        fun create(
            multilingualTeaching: MultilingualTeachingRecord,
            frenchLearningFocus: String,
            frenchLearningObjective: String,
        ): FrenchEducationRecord {
            require(
                multilingualTeaching.languageEducationSession.targetLanguage.equals(
                    other = "French",
                    ignoreCase = true,
                ),
            ) {
                "French Education requires French as the target language."
            }

            val normalizedFocus = frenchLearningFocus.trim()
            val normalizedObjective = frenchLearningObjective.trim()

            require(normalizedFocus.isNotEmpty()) {
                "French Education learning focus must not be blank."
            }

            require(normalizedObjective.isNotEmpty()) {
                "French Education learning objective must not be blank."
            }

            return FrenchEducationRecord(
                multilingualTeaching = multilingualTeaching,
                frenchLearningFocus = normalizedFocus,
                frenchLearningObjective = normalizedObjective,
            )
        }
    }
}
