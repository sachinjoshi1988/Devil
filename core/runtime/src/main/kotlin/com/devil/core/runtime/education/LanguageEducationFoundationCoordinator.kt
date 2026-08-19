package com.devil.core.runtime.education

import com.devil.core.model.common.TraceId
import com.devil.core.model.education.EducationSessionRecord
import com.devil.core.model.education.LanguageEducationSessionRecord

/**
 * Stage 120 bounded Language Education Foundation coordinator.
 *
 * This coordinator accepts an already prepared Stage 85 education session and
 * adds one explicitly supplied target language.
 *
 * It does not recreate the Education Foundation and does not infer learner
 * intent or language from raw conversation.
 *
 * It does not:
 *
 * - create another Devil intelligence, Brain, Constitution, Executive,
 *   Planner, Unified Devil Runtime, Memory Authority, or Security Authority;
 * - authenticate a learner or grant authorization;
 * - classify age or apply child/guardian policy;
 * - create curriculum or lessons;
 * - conduct Spoken English instruction or conversation;
 * - evaluate pronunciation or listening comprehension;
 * - teach grammar, reading, vocabulary, or writing;
 * - provide confidence, academic, or professional coaching;
 * - adapt curriculum or assess learner progress;
 * - create Tasks or Plans;
 * - invoke execution;
 * - establish Observation, Verification, or Outcome;
 * - mutate World Model state;
 * - perform constitutional Learning;
 * - create or persist Memory;
 * - or communicate with Android or another platform API.
 *
 * LANGUAGE_EDUCATION = DOMAIN OF THE ONE DEVIL INTELLIGENCE.
 * LANGUAGE_EDUCATION != ANOTHER INTELLIGENCE.
 * PREPARED != TAUGHT.
 * PREPARED != ASSESSED.
 * PREPARED != VERIFIED_PROGRESS.
 */
class LanguageEducationFoundationCoordinator {

    fun prepare(
        traceId: TraceId,
        educationSession: EducationSessionRecord,
        targetLanguage: String,
    ): LanguageEducationFoundationResult {
        if (targetLanguage.isBlank()) {
            return deferred(
                traceId = traceId,
            )
        }

        val languageSession =
            LanguageEducationSessionRecord.create(
                educationSession = educationSession,
                targetLanguage = targetLanguage,
            )

        return LanguageEducationFoundationResult.create(
            traceId = traceId,
            status = LanguageEducationFoundationStatus.PREPARED,
            languageSession = languageSession,
        )
    }

    private fun deferred(
        traceId: TraceId,
    ): LanguageEducationFoundationResult {
        return LanguageEducationFoundationResult.create(
            traceId = traceId,
            status = LanguageEducationFoundationStatus.DEFERRED,
        )
    }
}
