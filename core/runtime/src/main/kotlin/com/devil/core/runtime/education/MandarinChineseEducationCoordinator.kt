package com.devil.core.runtime.education

import com.devil.core.model.common.TraceId
import com.devil.core.model.education.MandarinChineseEducationRecord
import com.devil.core.model.education.MultilingualTeachingRecord

/**
 * Stage 138 bounded Mandarin Chinese Education coordinator.
 *
 * This coordinator specializes one existing Stage 133 Multilingual Teaching
 * context only when its preserved Stage 120 target language is Mandarin Chinese.
 *
 * Stages 134-137 are sibling language specializations and are not required
 * predecessors.
 *
 * It does not:
 *
 * - implement French, German, Spanish, Russian, or other languages;
 * - perform translation;
 * - generate Mandarin Chinese lessons;
 * - teach vocabulary, grammar, characters, pinyin, or tones;
 * - conduct multilingual or Mandarin Chinese conversation;
 * - recognize or synthesize speech;
 * - score pronunciation or tones;
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
 * MANDARIN_CHINESE_EDUCATION != TRANSLATION_ENGINE.
 * MANDARIN_CHINESE_CONTEXT != MANDARIN_CHINESE_TAUGHT.
 * PREPARED != CONVERSATION_COMPLETED.
 * PREPARED != PRONUNCIATION_VERIFIED.
 * PREPARED != PROFICIENCY_VERIFIED.
 */
class MandarinChineseEducationCoordinator {

    fun prepare(
        traceId: TraceId,
        multilingualTeaching: MultilingualTeachingRecord,
        mandarinChineseLearningFocus: String,
        mandarinChineseLearningObjective: String,
    ): MandarinChineseEducationPreparationResult {
        if (
            !multilingualTeaching
                .languageEducationSession
                .targetLanguage
                .equals(
                    other = "Mandarin Chinese",
                    ignoreCase = true,
                ) ||
            mandarinChineseLearningFocus.isBlank() ||
            mandarinChineseLearningObjective.isBlank()
        ) {
            return deferred(
                traceId = traceId,
            )
        }

        val mandarinChineseEducation =
            MandarinChineseEducationRecord.create(
                multilingualTeaching = multilingualTeaching,
                mandarinChineseLearningFocus =
                    mandarinChineseLearningFocus,
                mandarinChineseLearningObjective =
                    mandarinChineseLearningObjective,
            )

        return MandarinChineseEducationPreparationResult.create(
            traceId = traceId,
            status =
                MandarinChineseEducationPreparationStatus.PREPARED,
            mandarinChineseEducation = mandarinChineseEducation,
        )
    }

    private fun deferred(
        traceId: TraceId,
    ): MandarinChineseEducationPreparationResult {
        return MandarinChineseEducationPreparationResult.create(
            traceId = traceId,
            status =
                MandarinChineseEducationPreparationStatus.DEFERRED,
        )
    }
}
