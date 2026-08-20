package com.devil.core.model.education

/**
 * Immutable Stage 149 representation of one bounded Learning Progress context.
 *
 * This record preserves:
 *
 * - one existing Stage 148 Study Companion context;
 * - one explicitly supplied nonblank progress focus;
 * - one explicitly supplied nonblank learner-evidence description;
 * - one explicitly supplied nonblank bounded progress interpretation.
 *
 * Stage 149 represents Education Domain progress evidence only.
 *
 * It does not:
 *
 * - invent learner evidence;
 * - calculate or infer a score;
 * - establish verified mastery;
 * - establish global proficiency;
 * - perform constitutional Observation or Verification;
 * - establish a verified Outcome;
 * - authenticate a child or guardian;
 * - establish guardian authority or approval;
 * - replace child/guardian policy;
 * - weaken privacy provenance;
 * - create Tasks or Plans;
 * - invoke Executive or execution;
 * - perform constitutional Learning;
 * - create or persist Memory;
 * - invoke external providers;
 * - communicate with Android or platform APIs;
 * - or implement Stage 150 Guardian Learning Summary.
 *
 * LEARNING_PROGRESS != VERIFIED_MASTERY.
 * LEARNING_PROGRESS != CONSTITUTIONAL_VERIFICATION.
 * LEARNING_PROGRESS != GLOBAL_PROFICIENCY.
 * LEARNING_PROGRESS != MEMORY_PERSISTENCE.
 * PROGRESS_EVIDENCE_DESCRIPTION != CONSTITUTIONAL_OBSERVATION.
 * PROGRESS_INTERPRETATION != VERIFIED_OUTCOME.
 */
@ConsistentCopyVisibility
data class LearningProgressRecord private constructor(
    val studyCompanion: StudyCompanionRecord,
    val progressFocus: String,
    val learnerEvidenceDescription: String,
    val progressInterpretation: String,
) {
    companion object {

        fun create(
            studyCompanion: StudyCompanionRecord,
            progressFocus: String,
            learnerEvidenceDescription: String,
            progressInterpretation: String,
        ): LearningProgressRecord {
            val normalizedProgressFocus =
                progressFocus.trim()

            val normalizedLearnerEvidenceDescription =
                learnerEvidenceDescription.trim()

            val normalizedProgressInterpretation =
                progressInterpretation.trim()

            require(normalizedProgressFocus.isNotEmpty()) {
                "Learning Progress focus must not be blank."
            }

            require(normalizedLearnerEvidenceDescription.isNotEmpty()) {
                "Learning Progress learner evidence must not be blank."
            }

            require(normalizedProgressInterpretation.isNotEmpty()) {
                "Learning Progress interpretation must not be blank."
            }

            return LearningProgressRecord(
                studyCompanion = studyCompanion,
                progressFocus = normalizedProgressFocus,
                learnerEvidenceDescription =
                    normalizedLearnerEvidenceDescription,
                progressInterpretation =
                    normalizedProgressInterpretation,
            )
        }
    }
}
