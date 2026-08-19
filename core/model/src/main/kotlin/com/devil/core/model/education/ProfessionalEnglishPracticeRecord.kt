package com.devil.core.model.education

/**
 * Immutable Stage 130 representation of one bounded Professional English
 * educational practice context.
 *
 * This record preserves:
 *
 * - one existing Stage 120 Language Education session;
 * - one explicitly supplied nonblank professional target;
 * - one explicitly supplied nonblank professional objective.
 *
 * This record does not send professional communication, execute workplace tasks,
 * apply for jobs, submit resumes, make employment decisions, verify professional
 * proficiency, perform constitutional Learning, create Memory, or persist
 * learner progress.
 *
 * PROFESSIONAL_ENGLISH != WORKPLACE_EXECUTION.
 * PROFESSIONAL_TARGET != PROFESSIONAL_PROFICIENCY_VERIFIED.
 * PROFESSIONAL_COMMUNICATION_CONTEXT != MESSAGE_SENT.
 */
@ConsistentCopyVisibility
data class ProfessionalEnglishPracticeRecord private constructor(
    val languageEducationSession: LanguageEducationSessionRecord,
    val professionalTarget: String,
    val professionalObjective: String,
) {
    companion object {

        fun create(
            languageEducationSession: LanguageEducationSessionRecord,
            professionalTarget: String,
            professionalObjective: String,
        ): ProfessionalEnglishPracticeRecord {
            val normalizedProfessionalTarget = professionalTarget.trim()
            val normalizedProfessionalObjective = professionalObjective.trim()

            require(normalizedProfessionalTarget.isNotEmpty()) {
                "Professional English target must not be blank."
            }

            require(normalizedProfessionalObjective.isNotEmpty()) {
                "Professional English objective must not be blank."
            }

            return ProfessionalEnglishPracticeRecord(
                languageEducationSession = languageEducationSession,
                professionalTarget = normalizedProfessionalTarget,
                professionalObjective = normalizedProfessionalObjective,
            )
        }
    }
}
