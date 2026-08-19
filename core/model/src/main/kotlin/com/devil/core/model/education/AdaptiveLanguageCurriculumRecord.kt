package com.devil.core.model.education

/**
 * Immutable Stage 131 representation of one bounded Adaptive Language Curriculum
 * preparation context.
 *
 * This record preserves:
 *
 * - one existing Stage 120 Language Education session;
 * - one explicitly supplied nonblank curriculum focus;
 * - one explicitly supplied nonblank adaptation rationale.
 *
 * The adaptation rationale is supplied input only. It is not a verified learner
 * assessment, proficiency score, mastery claim, or evidence of learner progress.
 *
 * This record does not generate lessons, execute curriculum, schedule study,
 * invoke constitutional Strategy Adaptation, perform constitutional Learning,
 * create Memory, or persist learner progress.
 *
 * ADAPTIVE_CURRICULUM != STRATEGY_ADAPTATION.
 * CURRICULUM_PREPARATION != LESSON_GENERATION.
 * ADAPTATION_RATIONALE != VERIFIED_LEARNER_ASSESSMENT.
 */
@ConsistentCopyVisibility
data class AdaptiveLanguageCurriculumRecord private constructor(
    val languageEducationSession: LanguageEducationSessionRecord,
    val curriculumFocus: String,
    val adaptationRationale: String,
) {
    companion object {

        fun create(
            languageEducationSession: LanguageEducationSessionRecord,
            curriculumFocus: String,
            adaptationRationale: String,
        ): AdaptiveLanguageCurriculumRecord {
            val normalizedCurriculumFocus = curriculumFocus.trim()
            val normalizedAdaptationRationale = adaptationRationale.trim()

            require(normalizedCurriculumFocus.isNotEmpty()) {
                "Adaptive Language Curriculum focus must not be blank."
            }

            require(normalizedAdaptationRationale.isNotEmpty()) {
                "Adaptive Language Curriculum rationale must not be blank."
            }

            return AdaptiveLanguageCurriculumRecord(
                languageEducationSession = languageEducationSession,
                curriculumFocus = normalizedCurriculumFocus,
                adaptationRationale = normalizedAdaptationRationale,
            )
        }
    }
}
