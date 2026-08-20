package com.devil.core.model.education

/**
 * Immutable Stage 148 representation of one bounded Study Companion context.
 *
 * This record preserves:
 *
 * - one existing Stage 147 Homework Assistance context;
 * - one explicitly supplied nonblank study focus;
 * - one explicitly supplied nonblank study approach;
 * - one explicitly supplied nonblank learner-support objective.
 *
 * Stage 148 represents bounded study-oriented educational support only.
 *
 * It does not:
 *
 * - schedule study activity;
 * - create Tasks or Plans;
 * - establish that a study session occurred or completed;
 * - complete homework or assignments;
 * - submit assignments;
 * - grade learner work;
 * - establish verified correctness or mastery;
 * - infer child status;
 * - authenticate a child or guardian;
 * - establish guardian authority or approval;
 * - replace or weaken child/guardian policy;
 * - replace or weaken privacy provenance;
 * - expose protected content;
 * - grant constitutional authorization;
 * - invoke Executive or execution;
 * - establish Observation, Verification, or Outcome;
 * - perform constitutional Learning;
 * - create or persist Memory;
 * - invoke external providers;
 * - communicate with Android or platform APIs;
 * - or implement Stage 149 Learning Progress.
 *
 * STUDY_COMPANION != STUDY_SCHEDULER.
 * STUDY_COMPANION != TASK_CREATION.
 * STUDY_COMPANION != STUDY_SESSION_COMPLETED.
 * STUDY_COMPANION != VERIFIED_MASTERY.
 * STUDY_COMPANION != LEARNING_PROGRESS.
 * STUDY_COMPANION != CONSTITUTIONAL_LEARNING.
 */
@ConsistentCopyVisibility
data class StudyCompanionRecord private constructor(
    val homeworkAssistance: HomeworkAssistanceRecord,
    val studyFocus: String,
    val studyApproach: String,
    val learnerSupportObjective: String,
) {
    companion object {

        fun create(
            homeworkAssistance: HomeworkAssistanceRecord,
            studyFocus: String,
            studyApproach: String,
            learnerSupportObjective: String,
        ): StudyCompanionRecord {
            val normalizedStudyFocus =
                studyFocus.trim()

            val normalizedStudyApproach =
                studyApproach.trim()

            val normalizedLearnerSupportObjective =
                learnerSupportObjective.trim()

            require(normalizedStudyFocus.isNotEmpty()) {
                "Study Companion focus must not be blank."
            }

            require(normalizedStudyApproach.isNotEmpty()) {
                "Study Companion approach must not be blank."
            }

            require(normalizedLearnerSupportObjective.isNotEmpty()) {
                "Study Companion learner-support objective must not be blank."
            }

            return StudyCompanionRecord(
                homeworkAssistance = homeworkAssistance,
                studyFocus = normalizedStudyFocus,
                studyApproach = normalizedStudyApproach,
                learnerSupportObjective =
                    normalizedLearnerSupportObjective,
            )
        }
    }
}
