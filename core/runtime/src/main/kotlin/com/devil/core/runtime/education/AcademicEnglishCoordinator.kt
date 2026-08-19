package com.devil.core.runtime.education

import com.devil.core.model.common.TraceId
import com.devil.core.model.education.AcademicEnglishPracticeRecord
import com.devil.core.model.education.LanguageEducationSessionRecord

/**
 * Stage 129 bounded Academic English coordinator.
 *
 * This coordinator prepares one Education Domain Academic English context
 * directly from an existing Stage 120 Language Education session plus one
 * explicitly supplied academic target and academic objective.
 *
 * Stages 121–128 are not required predecessors.
 *
 * It does not:
 *
 * - complete homework or assignments;
 * - grade essays, reports, or other academic work;
 * - verify or fabricate citations;
 * - perform plagiarism checking;
 * - claim academic proficiency or verified learner progress;
 * - create Professional English capability;
 * - create adaptive curriculum or progress assessment;
 * - invoke academic, research, or model providers;
 * - create another intelligence, Brain, Executive, Planner, Memory Authority,
 *   or Security Authority;
 * - create Tasks or Plans;
 * - invoke execution;
 * - establish Observation, Verification, or Outcome;
 * - perform constitutional Learning;
 * - create or persist Memory;
 * - or communicate with Android or platform APIs.
 *
 * PREPARED != TAUGHT.
 * PREPARED != ASSIGNMENT_COMPLETED.
 * PREPARED != ACADEMIC_PROFICIENCY_VERIFIED.
 */
class AcademicEnglishCoordinator {

    fun prepare(
        traceId: TraceId,
        languageEducationSession: LanguageEducationSessionRecord,
        academicTarget: String,
        academicObjective: String,
    ): AcademicEnglishPreparationResult {
        if (academicTarget.isBlank() || academicObjective.isBlank()) {
            return deferred(
                traceId = traceId,
            )
        }

        val practice =
            AcademicEnglishPracticeRecord.create(
                languageEducationSession = languageEducationSession,
                academicTarget = academicTarget,
                academicObjective = academicObjective,
            )

        return AcademicEnglishPreparationResult.create(
            traceId = traceId,
            status = AcademicEnglishPreparationStatus.PREPARED,
            practice = practice,
        )
    }

    private fun deferred(
        traceId: TraceId,
    ): AcademicEnglishPreparationResult {
        return AcademicEnglishPreparationResult.create(
            traceId = traceId,
            status = AcademicEnglishPreparationStatus.DEFERRED,
        )
    }
}
