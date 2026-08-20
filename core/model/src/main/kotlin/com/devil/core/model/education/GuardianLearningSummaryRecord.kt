package com.devil.core.model.education

/**
 * Immutable Stage 150 representation of one bounded Guardian Learning Summary.
 *
 * This record preserves:
 *
 * - one existing Stage 149 Learning Progress context;
 * - one explicitly supplied nonblank guardian-summary focus;
 * - one explicitly supplied nonblank learner-progress summary;
 * - one explicitly supplied nonblank guardian-facing interpretation.
 *
 * Stage 150 represents bounded guardian-facing Education Domain summary
 * context only.
 *
 * It does not:
 *
 * - invent learner evidence;
 * - calculate or infer a score;
 * - establish verified mastery or global proficiency;
 * - authenticate a child or guardian;
 * - establish guardian authority or approval;
 * - replace child/guardian policy;
 * - replace or weaken privacy provenance;
 * - authorize disclosure or establish that disclosure occurred;
 * - send, publish, or transmit a summary;
 * - create Tasks or Plans;
 * - invoke Executive or execution;
 * - perform constitutional Observation or Verification;
 * - establish a verified Outcome;
 * - perform constitutional Learning;
 * - create or persist Memory;
 * - invoke external providers;
 * - communicate with Android or platform APIs;
 * - or implement Stage 151 Financial Intelligence Integration.
 *
 * GUARDIAN_LEARNING_SUMMARY != GUARDIAN_AUTHENTICATION.
 * GUARDIAN_LEARNING_SUMMARY != GUARDIAN_AUTHORITY.
 * GUARDIAN_LEARNING_SUMMARY != PRIVACY_AUTHORIZATION.
 * GUARDIAN_LEARNING_SUMMARY != VERIFIED_MASTERY.
 * GUARDIAN_LEARNING_SUMMARY != MEMORY_PERSISTENCE.
 * SUMMARY_PRESENTATION_CONTEXT != DISCLOSURE_OCCURRED.
 */
@ConsistentCopyVisibility
data class GuardianLearningSummaryRecord private constructor(
    val learningProgress: LearningProgressRecord,
    val guardianSummaryFocus: String,
    val learnerProgressSummary: String,
    val guardianFacingInterpretation: String,
) {
    companion object {

        fun create(
            learningProgress: LearningProgressRecord,
            guardianSummaryFocus: String,
            learnerProgressSummary: String,
            guardianFacingInterpretation: String,
        ): GuardianLearningSummaryRecord {
            val normalizedGuardianSummaryFocus =
                guardianSummaryFocus.trim()

            val normalizedLearnerProgressSummary =
                learnerProgressSummary.trim()

            val normalizedGuardianFacingInterpretation =
                guardianFacingInterpretation.trim()

            require(normalizedGuardianSummaryFocus.isNotEmpty()) {
                "Guardian Learning Summary focus must not be blank."
            }

            require(normalizedLearnerProgressSummary.isNotEmpty()) {
                "Guardian Learning Summary learner progress must not be blank."
            }

            require(normalizedGuardianFacingInterpretation.isNotEmpty()) {
                "Guardian Learning Summary interpretation must not be blank."
            }

            return GuardianLearningSummaryRecord(
                learningProgress = learningProgress,
                guardianSummaryFocus = normalizedGuardianSummaryFocus,
                learnerProgressSummary = normalizedLearnerProgressSummary,
                guardianFacingInterpretation =
                    normalizedGuardianFacingInterpretation,
            )
        }
    }
}
