package com.devil.core.runtime.education

import com.devil.core.model.common.TraceId
import com.devil.core.model.education.EnglishConfidenceCoachPracticeRecord
import com.devil.core.model.education.LanguageEducationSessionRecord

/**
 * Stage 128 bounded English Confidence Coach coordinator.
 *
 * This coordinator prepares one Education Domain confidence-coaching context
 * directly from an existing Stage 120 Language Education session plus one
 * explicitly supplied confidence target and coaching objective.
 *
 * Stages 121–127 are not required predecessors.
 *
 * It does not:
 *
 * - diagnose or treat mental-health conditions;
 * - infer emotional or psychological state;
 * - calculate a confidence score;
 * - claim confidence improvement;
 * - verify proficiency or learner progress;
 * - create academic or professional-English capability;
 * - create adaptive curriculum or assessment;
 * - create another intelligence, Brain, Executive, Planner, Memory Authority,
 *   or Security Authority;
 * - create Tasks or Plans;
 * - invoke execution;
 * - establish Observation, Verification, or Outcome;
 * - perform constitutional Learning;
 * - create or persist Memory;
 * - or communicate with Android or platform APIs.
 *
 * PREPARED != COACHED.
 * PREPARED != CONFIDENCE_IMPROVED.
 * PREPARED != VERIFIED_PROGRESS.
 */
class EnglishConfidenceCoachCoordinator {

    fun prepare(
        traceId: TraceId,
        languageEducationSession: LanguageEducationSessionRecord,
        confidenceTarget: String,
        coachingObjective: String,
    ): EnglishConfidenceCoachPreparationResult {
        if (confidenceTarget.isBlank() || coachingObjective.isBlank()) {
            return deferred(
                traceId = traceId,
            )
        }

        val practice =
            EnglishConfidenceCoachPracticeRecord.create(
                languageEducationSession = languageEducationSession,
                confidenceTarget = confidenceTarget,
                coachingObjective = coachingObjective,
            )

        return EnglishConfidenceCoachPreparationResult.create(
            traceId = traceId,
            status = EnglishConfidenceCoachPreparationStatus.PREPARED,
            practice = practice,
        )
    }

    private fun deferred(
        traceId: TraceId,
    ): EnglishConfidenceCoachPreparationResult {
        return EnglishConfidenceCoachPreparationResult.create(
            traceId = traceId,
            status = EnglishConfidenceCoachPreparationStatus.DEFERRED,
        )
    }
}
