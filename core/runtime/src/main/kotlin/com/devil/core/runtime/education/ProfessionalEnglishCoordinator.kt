package com.devil.core.runtime.education

import com.devil.core.model.common.TraceId
import com.devil.core.model.education.LanguageEducationSessionRecord
import com.devil.core.model.education.ProfessionalEnglishPracticeRecord

/**
 * Stage 130 bounded Professional English coordinator.
 *
 * This coordinator prepares one Education Domain Professional English context
 * directly from an existing Stage 120 Language Education session plus one
 * explicitly supplied professional target and professional objective.
 *
 * Stages 121–129 are not required predecessors.
 *
 * It does not:
 *
 * - send email, chat, or other professional communication;
 * - create or execute workplace tasks;
 * - apply for jobs or submit CVs/resumes;
 * - complete interviews or claim interview success;
 * - make HR or employment decisions;
 * - verify professional proficiency or learner progress;
 * - create Adaptive Language Curriculum or progress assessment;
 * - invoke workplace, business, employment, or communication providers;
 * - create another intelligence, Brain, Executive, Planner, Memory Authority,
 *   or Security Authority;
 * - create Tasks or Plans;
 * - invoke execution;
 * - establish Observation, Verification, or Outcome;
 * - perform constitutional Learning;
 * - create or persist Memory;
 * - or communicate with Android or platform APIs.
 *
 * PREPARED != SENT.
 * PREPARED != WORKPLACE_TASK_COMPLETED.
 * PREPARED != PROFESSIONAL_PROFICIENCY_VERIFIED.
 */
class ProfessionalEnglishCoordinator {

    fun prepare(
        traceId: TraceId,
        languageEducationSession: LanguageEducationSessionRecord,
        professionalTarget: String,
        professionalObjective: String,
    ): ProfessionalEnglishPreparationResult {
        if (professionalTarget.isBlank() || professionalObjective.isBlank()) {
            return deferred(
                traceId = traceId,
            )
        }

        val practice =
            ProfessionalEnglishPracticeRecord.create(
                languageEducationSession = languageEducationSession,
                professionalTarget = professionalTarget,
                professionalObjective = professionalObjective,
            )

        return ProfessionalEnglishPreparationResult.create(
            traceId = traceId,
            status = ProfessionalEnglishPreparationStatus.PREPARED,
            practice = practice,
        )
    }

    private fun deferred(
        traceId: TraceId,
    ): ProfessionalEnglishPreparationResult {
        return ProfessionalEnglishPreparationResult.create(
            traceId = traceId,
            status = ProfessionalEnglishPreparationStatus.DEFERRED,
        )
    }
}
