package com.devil.core.model.education

/**
 * Immutable Stage 129 representation of one bounded Academic English
 * educational practice context.
 *
 * This record preserves:
 *
 * - one existing Stage 120 Language Education session;
 * - one explicitly supplied nonblank academic target;
 * - one explicitly supplied nonblank academic objective.
 *
 * This record does not complete assignments, grade academic work, verify
 * citations, claim academic proficiency, execute actions, perform
 * constitutional Learning, create Memory, or persist learner progress.
 *
 * ACADEMIC_ENGLISH != HOMEWORK_COMPLETION.
 * ACADEMIC_TARGET != ACADEMIC_PROFICIENCY_VERIFIED.
 * ACADEMIC_CONTEXT != VERIFIED_ASSIGNMENT.
 */
@ConsistentCopyVisibility
data class AcademicEnglishPracticeRecord private constructor(
    val languageEducationSession: LanguageEducationSessionRecord,
    val academicTarget: String,
    val academicObjective: String,
) {
    companion object {

        fun create(
            languageEducationSession: LanguageEducationSessionRecord,
            academicTarget: String,
            academicObjective: String,
        ): AcademicEnglishPracticeRecord {
            val normalizedAcademicTarget = academicTarget.trim()
            val normalizedAcademicObjective = academicObjective.trim()

            require(normalizedAcademicTarget.isNotEmpty()) {
                "Academic English target must not be blank."
            }

            require(normalizedAcademicObjective.isNotEmpty()) {
                "Academic English objective must not be blank."
            }

            return AcademicEnglishPracticeRecord(
                languageEducationSession = languageEducationSession,
                academicTarget = normalizedAcademicTarget,
                academicObjective = normalizedAcademicObjective,
            )
        }
    }
}
