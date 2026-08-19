package com.devil.core.runtime.education

import com.devil.core.model.common.TraceId
import com.devil.core.model.education.CrossLanguageLearningAssistanceRecord
import com.devil.core.model.education.MultilingualConversationLabRecord

/**
 * Stage 141 bounded Cross-Language Learning Assistance coordinator.
 *
 * This coordinator prepares one provider-neutral educational assistance context
 * from an existing Stage 140 Multilingual Conversation Lab.
 *
 * It preserves the lab's existing Stage 133 Multilingual Teaching context and
 * the exact Stage 120 target language.
 *
 * One explicitly supplied support language may be used to frame later
 * cross-language educational assistance, but it must differ from the target
 * language.
 *
 * It does not:
 *
 * - perform translation;
 * - generate bilingual answers;
 * - infer or detect languages;
 * - create a second target language;
 * - replace the Stage 120 target language;
 * - conduct multilingual conversation;
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
 * - invoke external model, translation, language, or education providers;
 * - or communicate with Android or platform APIs.
 *
 * CROSS_LANGUAGE_ASSISTANCE != TRANSLATION_ENGINE.
 * SUPPORT_LANGUAGE != SECOND_TARGET_LANGUAGE.
 * PREPARED != TRANSLATION_PERFORMED.
 * PREPARED != CONVERSATION_COMPLETED.
 * PREPARED != LEARNING_VERIFIED.
 * PREPARED != PROFICIENCY_VERIFIED.
 */
class CrossLanguageLearningAssistanceCoordinator {

    fun prepare(
        traceId: TraceId,
        conversationLab: MultilingualConversationLabRecord,
        supportLanguage: String,
        assistanceFocus: String,
        assistanceObjective: String,
    ): CrossLanguageLearningAssistancePreparationResult {
        val targetLanguage =
            conversationLab
                .multilingualTeaching
                .languageEducationSession
                .targetLanguage

        if (
            supportLanguage.isBlank() ||
            assistanceFocus.isBlank() ||
            assistanceObjective.isBlank() ||
            supportLanguage.trim().equals(
                other = targetLanguage,
                ignoreCase = true,
            )
        ) {
            return deferred(
                traceId = traceId,
            )
        }

        val assistance =
            CrossLanguageLearningAssistanceRecord.create(
                conversationLab = conversationLab,
                supportLanguage = supportLanguage,
                assistanceFocus = assistanceFocus,
                assistanceObjective = assistanceObjective,
            )

        return CrossLanguageLearningAssistancePreparationResult.create(
            traceId = traceId,
            status =
                CrossLanguageLearningAssistancePreparationStatus.PREPARED,
            assistance = assistance,
        )
    }

    private fun deferred(
        traceId: TraceId,
    ): CrossLanguageLearningAssistancePreparationResult {
        return CrossLanguageLearningAssistancePreparationResult.create(
            traceId = traceId,
            status =
                CrossLanguageLearningAssistancePreparationStatus.DEFERRED,
        )
    }
}
