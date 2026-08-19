package com.devil.core.model.education

/**
 * Immutable Stage 136 representation of one bounded Spanish Education context.
 *
 * This record preserves:
 *
 * - one existing Stage 133 Multilingual Teaching context whose preserved
 *   Stage 120 target language is Spanish;
 * - one explicitly supplied nonblank Spanish learning focus;
 * - one explicitly supplied nonblank Spanish learning objective.
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
 * SPANISH_EDUCATION != TRANSLATION_ENGINE.
 * SPANISH_CONTEXT != SPANISH_TAUGHT.
 * SPANISH_CONTEXT != PRONUNCIATION_VERIFIED.
 * SPANISH_CONTEXT != CONVERSATION_COMPLETED.
 * SPANISH_CONTEXT != VERIFIED_PROFICIENCY.
 */
@ConsistentCopyVisibility
data class SpanishEducationRecord private constructor(
    val multilingualTeaching: MultilingualTeachingRecord,
    val spanishLearningFocus: String,
    val spanishLearningObjective: String,
) {
    companion object {

        fun create(
            multilingualTeaching: MultilingualTeachingRecord,
            spanishLearningFocus: String,
            spanishLearningObjective: String,
        ): SpanishEducationRecord {
            require(
                multilingualTeaching.languageEducationSession.targetLanguage.equals(
                    other = "Spanish",
                    ignoreCase = true,
                ),
            ) {
                "Spanish Education requires Spanish as the target language."
            }

            val normalizedSpanishLearningFocus =
                spanishLearningFocus.trim()

            val normalizedSpanishLearningObjective =
                spanishLearningObjective.trim()

            require(normalizedSpanishLearningFocus.isNotEmpty()) {
                "Spanish Education learning focus must not be blank."
            }

            require(normalizedSpanishLearningObjective.isNotEmpty()) {
                "Spanish Education learning objective must not be blank."
            }

            return SpanishEducationRecord(
                multilingualTeaching = multilingualTeaching,
                spanishLearningFocus = normalizedSpanishLearningFocus,
                spanishLearningObjective = normalizedSpanishLearningObjective,
            )
        }
    }
}
