package com.devil.core.model.education

/**
 * Immutable Stage 128 representation of one bounded English Confidence Coach
 * educational practice context.
 *
 * This record preserves:
 *
 * - one existing Stage 120 Language Education session;
 * - one explicitly supplied nonblank confidence target or situation;
 * - one explicitly supplied nonblank coaching objective.
 *
 * This record does not diagnose mental health, infer emotional state,
 * calculate a confidence score, claim confidence improvement, verify learner
 * progress, execute actions, perform constitutional Learning, create Memory,
 * or persist learner progress.
 *
 * CONFIDENCE_COACHING != PSYCHOLOGICAL_DIAGNOSIS.
 * CONFIDENCE_TARGET != CONFIDENCE_IMPROVED.
 * COACHING_CONTEXT != VERIFIED_PROGRESS.
 */
@ConsistentCopyVisibility
data class EnglishConfidenceCoachPracticeRecord private constructor(
    val languageEducationSession: LanguageEducationSessionRecord,
    val confidenceTarget: String,
    val coachingObjective: String,
) {
    companion object {

        fun create(
            languageEducationSession: LanguageEducationSessionRecord,
            confidenceTarget: String,
            coachingObjective: String,
        ): EnglishConfidenceCoachPracticeRecord {
            val normalizedConfidenceTarget = confidenceTarget.trim()
            val normalizedCoachingObjective = coachingObjective.trim()

            require(normalizedConfidenceTarget.isNotEmpty()) {
                "English Confidence Coach target must not be blank."
            }

            require(normalizedCoachingObjective.isNotEmpty()) {
                "English Confidence Coach objective must not be blank."
            }

            return EnglishConfidenceCoachPracticeRecord(
                languageEducationSession = languageEducationSession,
                confidenceTarget = normalizedConfidenceTarget,
                coachingObjective = normalizedCoachingObjective,
            )
        }
    }
}
