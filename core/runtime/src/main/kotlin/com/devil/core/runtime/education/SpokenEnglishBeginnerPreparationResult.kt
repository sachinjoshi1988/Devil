package com.devil.core.runtime.education

import com.devil.core.model.common.TraceId
import com.devil.core.model.education.SpokenEnglishBeginnerSessionRecord

/**
 * Stable Stage 121 result of bounded Spoken English Beginner preparation.
 *
 * PREPARED requires one SpokenEnglishBeginnerSessionRecord.
 * DEFERRED must not contain one.
 *
 * This result creates no Brain, Decision, Task, Plan, capability authority,
 * execution, Observation, Verification, Outcome, constitutional Learning,
 * Memory commitment, curriculum, lesson, conversation result, proficiency
 * assessment, or verified learner progress.
 */
@ConsistentCopyVisibility
data class SpokenEnglishBeginnerPreparationResult private constructor(
    val traceId: TraceId,
    val status: SpokenEnglishBeginnerPreparationStatus,
    val beginnerSession: SpokenEnglishBeginnerSessionRecord?,
) {
    companion object {

        fun create(
            traceId: TraceId,
            status: SpokenEnglishBeginnerPreparationStatus,
            beginnerSession: SpokenEnglishBeginnerSessionRecord? = null,
        ): SpokenEnglishBeginnerPreparationResult {
            when (status) {
                SpokenEnglishBeginnerPreparationStatus.PREPARED -> {
                    require(beginnerSession != null) {
                        "Prepared Spoken English Beginner results require one beginner session."
                    }
                }

                SpokenEnglishBeginnerPreparationStatus.DEFERRED -> {
                    require(beginnerSession == null) {
                        "Deferred Spoken English Beginner results must not contain a beginner session."
                    }
                }
            }

            return SpokenEnglishBeginnerPreparationResult(
                traceId = traceId,
                status = status,
                beginnerSession = beginnerSession,
            )
        }
    }
}
