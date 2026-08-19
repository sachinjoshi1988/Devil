package com.devil.core.runtime.education

import com.devil.core.model.common.TraceId
import com.devil.core.model.education.LanguageEducationSessionRecord
import com.devil.core.model.education.LanguageProgressAssessmentRecord

/**
 * Stage 132 bounded Language Progress & Assessment coordinator.
 *
 * This coordinator prepares one Education Domain assessment context directly
 * from an existing Stage 120 Language Education session plus explicitly supplied
 * assessment focus, learner-evidence description, and assessment interpretation.
 *
 * Stage 121–131 preparation records are not automatically treated as learner
 * performance evidence.
 *
 * It does not:
 *
 * - infer learner performance from prior preparation records;
 * - capture speech, audio, or other assessment input;
 * - run examinations or standardized tests;
 * - calculate CEFR or another standardized proficiency level;
 * - establish constitutional Observation;
 * - establish constitutional Verification;
 * - establish Outcome;
 * - claim global proficiency or mastery;
 * - automatically adapt curriculum;
 * - invoke Stage 94 Strategy Adaptation;
 * - perform constitutional Learning;
 * - create or persist Memory;
 * - create Tasks or Plans;
 * - invoke execution;
 * - invoke model, assessment, or education providers;
 * - or communicate with Android or platform APIs.
 *
 * ASSESSMENT_EVIDENCE_DESCRIPTION != CONSTITUTIONAL_OBSERVATION.
 * ASSESSMENT_INTERPRETATION != CONSTITUTIONAL_VERIFICATION.
 * PREPARED != VERIFIED_GLOBAL_PROFICIENCY.
 * PREPARED != MASTERY_ESTABLISHED.
 */
class LanguageProgressAssessmentCoordinator {

    fun prepare(
        traceId: TraceId,
        languageEducationSession: LanguageEducationSessionRecord,
        assessmentFocus: String,
        learnerEvidence: String,
        assessmentInterpretation: String,
    ): LanguageProgressAssessmentPreparationResult {
        if (
            assessmentFocus.isBlank() ||
            learnerEvidence.isBlank() ||
            assessmentInterpretation.isBlank()
        ) {
            return deferred(
                traceId = traceId,
            )
        }

        val assessment =
            LanguageProgressAssessmentRecord.create(
                languageEducationSession = languageEducationSession,
                assessmentFocus = assessmentFocus,
                learnerEvidence = learnerEvidence,
                assessmentInterpretation = assessmentInterpretation,
            )

        return LanguageProgressAssessmentPreparationResult.create(
            traceId = traceId,
            status = LanguageProgressAssessmentPreparationStatus.PREPARED,
            assessment = assessment,
        )
    }

    private fun deferred(
        traceId: TraceId,
    ): LanguageProgressAssessmentPreparationResult {
        return LanguageProgressAssessmentPreparationResult.create(
            traceId = traceId,
            status = LanguageProgressAssessmentPreparationStatus.DEFERRED,
        )
    }
}
