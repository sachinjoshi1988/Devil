package com.devil.core.runtime.education

import com.devil.core.model.common.TraceId
import com.devil.core.model.education.StudyCompanionRecord

/**
 * Stable Stage 148 result of bounded Study Companion preparation.
 *
 * PREPARED requires exactly one StudyCompanionRecord.
 * DEFERRED must not contain one.
 *
 * This result establishes no scheduling, Task, Plan, completed study session,
 * verified mastery, Learning Progress, constitutional authorization, execution,
 * Observation, Verification, Outcome, Learning, or Memory commitment.
 */
@ConsistentCopyVisibility
data class StudyCompanionPreparationResult private constructor(
    val traceId: TraceId,
    val status: StudyCompanionPreparationStatus,
    val studyCompanion: StudyCompanionRecord?,
) {
    companion object {

        fun create(
            traceId: TraceId,
            status: StudyCompanionPreparationStatus,
            studyCompanion: StudyCompanionRecord? = null,
        ): StudyCompanionPreparationResult {
            when (status) {
                StudyCompanionPreparationStatus.PREPARED -> {
                    require(studyCompanion != null) {
                        "Prepared Study Companion results require one study context."
                    }
                }

                StudyCompanionPreparationStatus.DEFERRED -> {
                    require(studyCompanion == null) {
                        "Deferred Study Companion results must not contain a study context."
                    }
                }
            }

            return StudyCompanionPreparationResult(
                traceId = traceId,
                status = status,
                studyCompanion = studyCompanion,
            )
        }
    }
}
