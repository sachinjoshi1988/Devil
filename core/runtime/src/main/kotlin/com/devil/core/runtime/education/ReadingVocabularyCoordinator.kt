package com.devil.core.runtime.education

import com.devil.core.model.common.TraceId
import com.devil.core.model.education.LanguageEducationSessionRecord
import com.devil.core.model.education.ReadingVocabularyPracticeRecord

/**
 * Stage 126 bounded Reading & Vocabulary Development coordinator.
 *
 * This coordinator prepares one Education Domain reading/vocabulary context
 * directly from an existing Stage 120 Language Education session plus
 * explicitly supplied reading and vocabulary targets.
 *
 * Stages 121–125 are not required predecessors.
 *
 * It does not:
 *
 * - score or verify reading comprehension;
 * - generate or verify vocabulary definitions;
 * - verify vocabulary mastery;
 * - invoke dictionary, search, research, or model providers;
 * - perform grammar or writing assessment;
 * - assess proficiency or learner progress;
 * - create another intelligence, Brain, Executive, Planner, Memory Authority,
 *   or Security Authority;
 * - create Tasks or Plans;
 * - invoke execution;
 * - establish Observation, Verification, or Outcome;
 * - perform constitutional Learning;
 * - create or persist Memory;
 * - or communicate with Android or platform APIs.
 *
 * PREPARED != READ.
 * PREPARED != COMPREHENSION_VERIFIED.
 * PREPARED != VOCABULARY_MASTERED.
 */
class ReadingVocabularyCoordinator {

    fun prepare(
        traceId: TraceId,
        languageEducationSession: LanguageEducationSessionRecord,
        readingTarget: String,
        vocabularyTarget: String,
    ): ReadingVocabularyPreparationResult {
        if (readingTarget.isBlank() || vocabularyTarget.isBlank()) {
            return deferred(
                traceId = traceId,
            )
        }

        val practice =
            ReadingVocabularyPracticeRecord.create(
                languageEducationSession = languageEducationSession,
                readingTarget = readingTarget,
                vocabularyTarget = vocabularyTarget,
            )

        return ReadingVocabularyPreparationResult.create(
            traceId = traceId,
            status = ReadingVocabularyPreparationStatus.PREPARED,
            practice = practice,
        )
    }

    private fun deferred(
        traceId: TraceId,
    ): ReadingVocabularyPreparationResult {
        return ReadingVocabularyPreparationResult.create(
            traceId = traceId,
            status = ReadingVocabularyPreparationStatus.DEFERRED,
        )
    }
}
