package com.devil.core.runtime.education

import com.devil.core.model.common.TraceId
import com.devil.core.model.education.HomeworkAssistanceRecord
import com.devil.core.model.education.StudyCompanionRecord

/**
 * Stage 148 bounded Study Companion coordinator.
 *
 * This coordinator prepares one Study Companion context from an existing
 * Stage 147 Homework Assistance context and explicitly supplied study-support
 * metadata.
 *
 * Stage 147 remains authoritative for the preserved Homework Assistance
 * provenance.
 *
 * This coordinator does not:
 *
 * - schedule study activity;
 * - create Tasks or Plans;
 * - establish that a study session occurred or completed;
 * - complete or submit homework;
 * - grade learner work;
 * - establish verified correctness or mastery;
 * - infer child status;
 * - authenticate a child or guardian;
 * - establish guardian authority or approval;
 * - evaluate or replace child/guardian policy;
 * - evaluate or replace privacy policy;
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
 */
class StudyCompanionCoordinator {

    fun prepare(
        traceId: TraceId,
        homeworkAssistance: HomeworkAssistanceRecord,
        studyFocus: String,
        studyApproach: String,
        learnerSupportObjective: String,
    ): StudyCompanionPreparationResult {
        if (
            studyFocus.isBlank() ||
            studyApproach.isBlank() ||
            learnerSupportObjective.isBlank()
        ) {
            return deferred(
                traceId = traceId,
            )
        }

        val studyCompanion =
            StudyCompanionRecord.create(
                homeworkAssistance = homeworkAssistance,
                studyFocus = studyFocus,
                studyApproach = studyApproach,
                learnerSupportObjective = learnerSupportObjective,
            )

        return StudyCompanionPreparationResult.create(
            traceId = traceId,
            status = StudyCompanionPreparationStatus.PREPARED,
            studyCompanion = studyCompanion,
        )
    }

    private fun deferred(
        traceId: TraceId,
    ): StudyCompanionPreparationResult {
        return StudyCompanionPreparationResult.create(
            traceId = traceId,
            status = StudyCompanionPreparationStatus.DEFERRED,
        )
    }
}
