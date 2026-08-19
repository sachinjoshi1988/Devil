package com.devil.core.runtime.education

import com.devil.core.model.common.TraceId
import com.devil.core.model.education.LanguageEducationSessionRecord
import com.devil.core.model.education.SpokenEnglishBeginnerSessionRecord

/**
 * Stage 121 bounded Spoken English Beginner coordinator.
 *
 * This coordinator accepts an existing Stage 120 Language Education session.
 * It may prepare beginner Spoken English only when that session explicitly
 * targets English.
 *
 * "Beginner" is the selected educational context. This coordinator does not
 * infer or verify learner proficiency.
 *
 * It does not:
 *
 * - create another intelligence, Brain, Constitution, Executive, Planner,
 *   Unified Devil Runtime, Memory Authority, or Security Authority;
 * - authenticate or authorize a learner;
 * - classify age or apply guardian policy;
 * - conduct Spoken English conversation;
 * - evaluate pronunciation or listening comprehension;
 * - teach grammar, reading, vocabulary, or writing;
 * - create confidence, academic, or professional coaching;
 * - create adaptive curriculum;
 * - assess learner progress or proficiency;
 * - create Tasks or Plans;
 * - invoke execution;
 * - establish Observation, Verification, or Outcome;
 * - perform constitutional Learning;
 * - create or persist Memory;
 * - or communicate with Android or voice APIs.
 *
 * PREPARED != TAUGHT.
 * PREPARED != CONVERSATION_COMPLETED.
 * BEGINNER_CONTEXT != VERIFIED_PROFICIENCY.
 */
class SpokenEnglishBeginnerCoordinator {

    fun prepare(
        traceId: TraceId,
        languageEducationSession: LanguageEducationSessionRecord,
    ): SpokenEnglishBeginnerPreparationResult {
        if (
            !languageEducationSession.targetLanguage.equals(
                other = "English",
                ignoreCase = true,
            )
        ) {
            return deferred(
                traceId = traceId,
            )
        }

        val beginnerSession =
            SpokenEnglishBeginnerSessionRecord.create(
                languageEducationSession = languageEducationSession,
            )

        return SpokenEnglishBeginnerPreparationResult.create(
            traceId = traceId,
            status = SpokenEnglishBeginnerPreparationStatus.PREPARED,
            beginnerSession = beginnerSession,
        )
    }

    private fun deferred(
        traceId: TraceId,
    ): SpokenEnglishBeginnerPreparationResult {
        return SpokenEnglishBeginnerPreparationResult.create(
            traceId = traceId,
            status = SpokenEnglishBeginnerPreparationStatus.DEFERRED,
        )
    }
}
