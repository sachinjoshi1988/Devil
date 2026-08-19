package com.devil.core.runtime.education

import com.devil.core.model.common.TraceId
import com.devil.core.model.education.AdditionalLanguageEducationRecord
import com.devil.core.model.education.MultilingualTeachingRecord

/**
 * Stage 139 bounded Additional Language Expansion coordinator.
 *
 * This coordinator prepares one reusable provider-neutral education context
 * for languages that do not already have dedicated Stage 134-138 Education
 * specializations.
 *
 * It preserves the exact target language already owned by the Stage 133
 * Multilingual Teaching context.
 *
 * Dedicated Stage 134-138 languages remain sibling specializations and must
 * not be routed through this generic expansion path:
 *
 * - French;
 * - German;
 * - Spanish;
 * - Russian;
 * - Mandarin Chinese.
 *
 * It does not:
 *
 * - infer aliases or alternate language identities;
 * - create an exhaustive supported-language registry;
 * - perform translation;
 * - generate lessons;
 * - teach vocabulary, grammar, writing systems, pronunciation, or culture;
 * - conduct multilingual conversation;
 * - recognize or synthesize speech;
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
 * ADDITIONAL_LANGUAGE_EXPANSION != LANGUAGE_REGISTRY.
 * GENERIC_EXPANSION != DEDICATED_LANGUAGE_SPECIALIZATION.
 * PREPARED != LANGUAGE_TAUGHT.
 * PREPARED != PROFICIENCY_VERIFIED.
 */
class AdditionalLanguageEducationCoordinator {

    fun prepare(
        traceId: TraceId,
        multilingualTeaching: MultilingualTeachingRecord,
        additionalLanguageLearningFocus: String,
        additionalLanguageLearningObjective: String,
    ): AdditionalLanguageEducationPreparationResult {
        val targetLanguage =
            multilingualTeaching
                .languageEducationSession
                .targetLanguage

        if (
            isDedicatedLanguage(targetLanguage) ||
            additionalLanguageLearningFocus.isBlank() ||
            additionalLanguageLearningObjective.isBlank()
        ) {
            return deferred(
                traceId = traceId,
            )
        }

        val additionalLanguageEducation =
            AdditionalLanguageEducationRecord.create(
                multilingualTeaching = multilingualTeaching,
                additionalLanguageLearningFocus =
                    additionalLanguageLearningFocus,
                additionalLanguageLearningObjective =
                    additionalLanguageLearningObjective,
            )

        return AdditionalLanguageEducationPreparationResult.create(
            traceId = traceId,
            status =
                AdditionalLanguageEducationPreparationStatus.PREPARED,
            additionalLanguageEducation = additionalLanguageEducation,
        )
    }

    private fun isDedicatedLanguage(
        targetLanguage: String,
    ): Boolean {
        return targetLanguage.equals(
            other = "French",
            ignoreCase = true,
        ) ||
            targetLanguage.equals(
                other = "German",
                ignoreCase = true,
            ) ||
            targetLanguage.equals(
                other = "Spanish",
                ignoreCase = true,
            ) ||
            targetLanguage.equals(
                other = "Russian",
                ignoreCase = true,
            ) ||
            targetLanguage.equals(
                other = "Mandarin Chinese",
                ignoreCase = true,
            )
    }

    private fun deferred(
        traceId: TraceId,
    ): AdditionalLanguageEducationPreparationResult {
        return AdditionalLanguageEducationPreparationResult.create(
            traceId = traceId,
            status =
                AdditionalLanguageEducationPreparationStatus.DEFERRED,
        )
    }
}
