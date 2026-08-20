package com.devil.core.runtime.education

/**
 * Stage 148 bounded Study Companion preparation status.
 *
 * PREPARED means one structurally valid StudyCompanionRecord was prepared
 * from an existing Stage 147 Homework Assistance context and explicit
 * study-support metadata.
 *
 * PREPARED does not mean:
 *
 * - study activity was scheduled;
 * - a Task or Plan was created;
 * - a study session occurred or completed;
 * - homework was completed;
 * - learner work was graded;
 * - mastery was verified;
 * - Learning Progress was established;
 * - constitutional Learning occurred;
 * - or execution was approved.
 *
 * DEFERRED means no truthful Study Companion context was produced.
 */
enum class StudyCompanionPreparationStatus {
    PREPARED,
    DEFERRED,
}
