package com.devil.core.runtime.creative

import com.devil.core.model.common.TraceId
import com.devil.core.model.creative.StoryToAnimationRecord

/**
 * Stable Stage 88 result of bounded Story-to-Animation preparation.
 *
 * PREPARED requires exactly one StoryToAnimationRecord.
 *
 * DEFERRED must not contain a Story-to-Animation record.
 *
 * This result creates no Brain, Constitution, runtime, identity authority,
 * trust, authentication, authorization, security session, Decision, Task,
 * Plan, capability, model, renderer, generation request, execution,
 * storyboard, frame sequence, animation timeline, generated media, file,
 * Observation, Verification, Outcome, World Model mutation, constitutional
 * Learning, Memory, or persistence authority.
 */
@ConsistentCopyVisibility
data class StoryToAnimationPreparationResult private constructor(
    val traceId: TraceId,
    val status: StoryToAnimationPreparationStatus,
    val record: StoryToAnimationRecord?,
) {
    companion object {

        fun create(
            traceId: TraceId,
            status: StoryToAnimationPreparationStatus,
            record: StoryToAnimationRecord? = null,
        ): StoryToAnimationPreparationResult {
            when (status) {
                StoryToAnimationPreparationStatus.PREPARED -> {
                    require(record != null) {
                        "Prepared Story-to-Animation results require one record."
                    }
                }

                StoryToAnimationPreparationStatus.DEFERRED -> {
                    require(record == null) {
                        "Deferred Story-to-Animation results must not contain a record."
                    }
                }
            }

            return StoryToAnimationPreparationResult(
                traceId = traceId,
                status = status,
                record = record,
            )
        }
    }
}
