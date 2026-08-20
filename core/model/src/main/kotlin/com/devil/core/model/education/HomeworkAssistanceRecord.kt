package com.devil.core.model.education

/**
 * Immutable Stage 147 representation of one bounded Homework Assistance context.
 *
 * This record preserves:
 *
 * - one existing Stage 146 Child Privacy Boundary context;
 * - one explicitly supplied nonblank homework subject;
 * - one explicitly supplied nonblank learner-facing assistance objective;
 * - one explicitly supplied nonblank assistance approach.
 *
 * Stage 147 supports learning-oriented homework assistance only.
 *
 * It does not:
 *
 * - complete homework or assignments on behalf of the learner;
 * - submit assignments;
 * - grade learner work;
 * - establish verified correctness;
 * - infer child status;
 * - authenticate a child or guardian;
 * - establish guardian authority or approval;
 * - replace or weaken Stage 44 child/guardian policy;
 * - replace or weaken Stage 146 privacy provenance;
 * - expose protected content;
 * - grant constitutional authorization;
 * - create Tasks or Plans;
 * - invoke Executive or execution;
 * - establish Observation, Verification, or Outcome;
 * - perform constitutional Learning;
 * - create or persist Memory;
 * - invoke external providers;
 * - communicate with Android or platform APIs;
 * - or implement Stage 148 Study Companion.
 *
 * HOMEWORK_ASSISTANCE != HOMEWORK_COMPLETION.
 * HOMEWORK_ASSISTANCE != ASSIGNMENT_SUBMISSION.
 * HOMEWORK_ASSISTANCE != VERIFIED_CORRECTNESS.
 * HOMEWORK_ASSISTANCE != GRADE.
 * HOMEWORK_ASSISTANCE != CONSTITUTIONAL_LEARNING.
 * HOMEWORK_ASSISTANCE != EXECUTION_APPROVED.
 */
@ConsistentCopyVisibility
data class HomeworkAssistanceRecord private constructor(
    val childPrivacyBoundary: ChildPrivacyBoundaryRecord,
    val homeworkSubject: String,
    val assistanceObjective: String,
    val assistanceApproach: String,
) {
    companion object {

        fun create(
            childPrivacyBoundary: ChildPrivacyBoundaryRecord,
            homeworkSubject: String,
            assistanceObjective: String,
            assistanceApproach: String,
        ): HomeworkAssistanceRecord {
            val normalizedHomeworkSubject =
                homeworkSubject.trim()

            val normalizedAssistanceObjective =
                assistanceObjective.trim()

            val normalizedAssistanceApproach =
                assistanceApproach.trim()

            require(normalizedHomeworkSubject.isNotEmpty()) {
                "Homework Assistance subject must not be blank."
            }

            require(normalizedAssistanceObjective.isNotEmpty()) {
                "Homework Assistance objective must not be blank."
            }

            require(normalizedAssistanceApproach.isNotEmpty()) {
                "Homework Assistance approach must not be blank."
            }

            return HomeworkAssistanceRecord(
                childPrivacyBoundary = childPrivacyBoundary,
                homeworkSubject = normalizedHomeworkSubject,
                assistanceObjective = normalizedAssistanceObjective,
                assistanceApproach = normalizedAssistanceApproach,
            )
        }
    }
}
