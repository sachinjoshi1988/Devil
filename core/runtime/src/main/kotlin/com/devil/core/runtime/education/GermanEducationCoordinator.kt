package com.devil.core.runtime.education

import com.devil.core.model.common.TraceId
import com.devil.core.model.education.GermanEducationRecord
import com.devil.core.model.education.MultilingualTeachingRecord

/**
 * Stage 135 bounded German Education coordinator.
 *
 * This coordinator specializes one existing Stage 133 Multilingual Teaching
 * context only when its preserved Stage 120 target language is German.
 *
 * Stage 134 French Education is a sibling specialization and is not a required
 * predecessor.
 *
 * It does not:
 *
 * - implement French, Spanish, Russian, Mandarin Chinese, or other languages;
 * - perform translation;
 * - generate German lessons;
 * - teach vocabulary or grammar;
 * - conduct multilingual or German conversation;
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
 * GERMAN_EDUCATION != TRANSLATION_ENGINE.
 * GERMAN_CONTEXT != GERMAN_TAUGHT.
 * PREPARED != CONVERSATION_COMPLETED.
 * PREPARED != PRONUNCIATION_VERIFIED.
 * PREPARED != PROFICIENCY_VERIFIED.
 */
class GermanEducationCoordinator {

    fun prepare(
        traceId: TraceId,
        multilingualTeaching: MultilingualTeachingRecord,
        germanLearningFocus: String,
        germanLearningObjective: String,
    ): GermanEducationPreparationResult {
        if (
            !multilingualTeaching
                .languageEducationSession
                .targetLanguage
                .equals(
                    other = "German",
                    ignoreCase = true,
                ) ||
            germanLearningFocus.isBlank() ||
            germanLearningObjective.isBlank()
        ) {
            return deferred(
                traceId = traceId,
            )
        }

        val germanEducation =
            GermanEducationRecord.create(
                multilingualTeaching = multilingualTeaching,
                germanLearningFocus = germanLearningFocus,
                germanLearningObjective = germanLearningObjective,
            )

        return GermanEducationPreparationResult.create(
            traceId = traceId,
            status = GermanEducationPreparationStatus.PREPARED,
            germanEducation = germanEducation,
        )
    }

    private fun deferred(
        traceId: TraceId,
    ): GermanEducationPreparationResult {
        return GermanEducationPreparationResult.create(
            traceId = traceId,
            status = GermanEducationPreparationStatus.DEFERRED,
        )
    }
}
