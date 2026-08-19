package com.devil.core.model.education

/**
 * Immutable Stage 132 representation of one bounded Language Progress &
 * Assessment educational context.
 *
 * This record preserves:
 *
 * - one existing Stage 120 Language Education session;
 * - one explicitly supplied nonblank assessment focus;
 * - one explicitly supplied nonblank learner-evidence description;
 * - one explicitly supplied nonblank assessment interpretation.
 *
 * Learner-evidence description is bounded Education Domain input only. It is not
 * constitutional Observation evidence.
 *
 * Assessment interpretation is bounded Education Domain interpretation only. It
 * is not constitutional Verification, Outcome, global proficiency, or mastery.
 *
 * This record does not capture audio, run examinations, calculate standardized
 * proficiency levels, establish constitutional Observation/Verification/Outcome,
 * perform constitutional Learning, create Memory, or persist learner progress.
 *
 * ASSESSMENT_EVIDENCE_DESCRIPTION != CONSTITUTIONAL_OBSERVATION.
 * ASSESSMENT_INTERPRETATION != CONSTITUTIONAL_VERIFICATION.
 * BOUNDED_PROGRESS_ASSESSMENT != VERIFIED_GLOBAL_PROFICIENCY.
 * ASSESSMENT_CONTEXT != MASTERY_ESTABLISHED.
 */
@ConsistentCopyVisibility
data class LanguageProgressAssessmentRecord private constructor(
    val languageEducationSession: LanguageEducationSessionRecord,
    val assessmentFocus: String,
    val learnerEvidence: String,
    val assessmentInterpretation: String,
) {
    companion object {

        fun create(
            languageEducationSession: LanguageEducationSessionRecord,
            assessmentFocus: String,
            learnerEvidence: String,
            assessmentInterpretation: String,
        ): LanguageProgressAssessmentRecord {
            val normalizedAssessmentFocus = assessmentFocus.trim()
            val normalizedLearnerEvidence = learnerEvidence.trim()
            val normalizedAssessmentInterpretation =
                assessmentInterpretation.trim()

            require(normalizedAssessmentFocus.isNotEmpty()) {
                "Language Progress & Assessment focus must not be blank."
            }

            require(normalizedLearnerEvidence.isNotEmpty()) {
                "Language Progress & Assessment learner evidence must not be blank."
            }

            require(normalizedAssessmentInterpretation.isNotEmpty()) {
                "Language Progress & Assessment interpretation must not be blank."
            }

            return LanguageProgressAssessmentRecord(
                languageEducationSession = languageEducationSession,
                assessmentFocus = normalizedAssessmentFocus,
                learnerEvidence = normalizedLearnerEvidence,
                assessmentInterpretation = normalizedAssessmentInterpretation,
            )
        }
    }
}
