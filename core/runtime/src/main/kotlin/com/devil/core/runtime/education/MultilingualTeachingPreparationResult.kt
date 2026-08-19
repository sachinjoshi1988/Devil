package com.devil.core.runtime.education

import com.devil.core.model.common.TraceId
import com.devil.core.model.education.MultilingualTeachingRecord

/**
 * Stable Stage 133 result of bounded Multilingual Teaching Architecture
 * preparation.
 *
 * PREPARED requires one MultilingualTeachingRecord.
 * DEFERRED must not contain one.
 *
 * This result creates no language-specific curriculum, translation result,
 * speech-recognition result, proficiency claim, Brain decision, Task, Plan,
 * execution, Observation, Verification, Outcome, constitutional Learning,
 * Memory commitment, or verified learner progress.
 */
@ConsistentCopyVisibility
data class MultilingualTeachingPreparationResult private constructor(
    val traceId: TraceId,
    val status: MultilingualTeachingPreparationStatus,
    val teaching: MultilingualTeachingRecord?,
) {
    companion object {

        fun create(
            traceId: TraceId,
            status: MultilingualTeachingPreparationStatus,
            teaching: MultilingualTeachingRecord? = null,
        ): MultilingualTeachingPreparationResult {
            when (status) {
                MultilingualTeachingPreparationStatus.PREPARED -> {
                    require(teaching != null) {
                        "Prepared Multilingual Teaching results require one teaching context."
                    }
                }

                MultilingualTeachingPreparationStatus.DEFERRED -> {
                    require(teaching == null) {
                        "Deferred Multilingual Teaching results must not contain a teaching context."
                    }
                }
            }

            return MultilingualTeachingPreparationResult(
                traceId = traceId,
                status = status,
                teaching = teaching,
            )
        }
    }
}
