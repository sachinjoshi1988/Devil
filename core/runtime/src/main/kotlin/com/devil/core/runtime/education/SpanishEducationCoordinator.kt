package com.devil.core.runtime.education

import com.devil.core.model.common.TraceId
import com.devil.core.model.education.MultilingualTeachingRecord
import com.devil.core.model.education.SpanishEducationRecord

/**
 * Stage 136 bounded Spanish Education coordinator.
 *
 * This coordinator specializes one existing Stage 133 Multilingual Teaching
 * context only when its preserved Stage 120 target language is Spanish.
 *
 * Stage 134 French Education and Stage 135 German Education are sibling
 * specializations and are not required predecessors.
 *
 * It does not:
 *
 * - implement French, German, Russian, Mandarin Chinese, or other languages;
 * - perform translation;
 * - generate Spanish lessons;
 * - teach vocabulary or grammar;
 * - conduct multilingual or Spanish conversation;
 * - recognize or synthesize speech;
 * - score pronunciation;
 * - infer or verify learner proficiency;
 * - automatically assess learner progress;
 * - generate or execute curriculum;
 * - create another intelligence, Brain, Executive, Planner, Memory Authority,
 *   or Security Authority;
 * - create Tasks or Plans;
 * - invoke execution;
 * - establish Observation, Verification, or Outcome;
 * - perform constitutional Learning;
 * - create or persist Memory;
 * - invoke external model, language, or education providers;
 * - or communicate with Android or platform APIs.
 *
 * SPANISH_EDUCATION != TRANSLATION_ENGINE.
 * SPANISH_CONTEXT != SPANISH_TAUGHT.
 * PREPARED != CONVERSATION_COMPLETED.
 * PREPARED != PRONUNCIATION_VERIFIED.
 * PREPARED != PROFICIENCY_VERIFIED.
 */
class SpanishEducationCoordinator {

    fun prepare(
        traceId: TraceId,
        multilingualTeaching: MultilingualTeachingRecord,
        spanishLearningFocus: String,
        spanishLearningObjective: String,
    ): SpanishEducationPreparationResult {
        if (
            !multilingualTeaching
                .languageEducationSession
                .targetLanguage
                .equals(
                    other = "Spanish",
                    ignoreCase = true,
                ) ||
            spanishLearningFocus.isBlank() ||
            spanishLearningObjective.isBlank()
        ) {
            return deferred(
                traceId = traceId,
            )
        }

        val spanishEducation =
            SpanishEducationRecord.create(
                multilingualTeaching = multilingualTeaching,
                spanishLearningFocus = spanishLearningFocus,
                spanishLearningObjective = spanishLearningObjective,
            )

        return SpanishEducationPreparationResult.create(
            traceId = traceId,
            status = SpanishEducationPreparationStatus.PREPARED,
            spanishEducation = spanishEducation,
        )
    }

    private fun deferred(
        traceId: TraceId,
    ): SpanishEducationPreparationResult {
        return SpanishEducationPreparationResult.create(
            traceId = traceId,
            status = SpanishEducationPreparationStatus.DEFERRED,
        )
    }
}
