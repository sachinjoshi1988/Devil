package com.devil.core.runtime.creative

import com.devil.core.model.common.TraceId
import com.devil.core.model.creative.StoryToAnimationPipelineRecord

/**
 * Stable Stage 171 result of bounded Story-to-Animation Pipeline preparation.
 *
 * PREPARED requires exactly one StoryToAnimationPipelineRecord.
 * DEFERRED must contain none.
 *
 * This result establishes no generation, execution, provider selection,
 * approved asset state, rendered media, constitutional Observation,
 * constitutional Verification, Outcome, publishing authorization,
 * Stage 172–174 behavior, World Model mutation, Learning, or Memory persistence.
 */
@ConsistentCopyVisibility
data class StoryToAnimationPipelinePreparationResult private constructor(
    val traceId: TraceId,
    val status: StoryToAnimationPipelinePreparationStatus,
    val pipeline: StoryToAnimationPipelineRecord?,
) {
    companion object {

        fun create(
            traceId: TraceId,
            status: StoryToAnimationPipelinePreparationStatus,
            pipeline: StoryToAnimationPipelineRecord? = null,
        ): StoryToAnimationPipelinePreparationResult {
            when (status) {
                StoryToAnimationPipelinePreparationStatus.PREPARED -> {
                    require(pipeline != null) {
                        "Prepared Story-to-Animation Pipeline results require one pipeline context."
                    }
                }

                StoryToAnimationPipelinePreparationStatus.DEFERRED -> {
                    require(pipeline == null) {
                        "Deferred Story-to-Animation Pipeline results must not contain a pipeline context."
                    }
                }
            }

            return StoryToAnimationPipelinePreparationResult(
                traceId = traceId,
                status = status,
                pipeline = pipeline,
            )
        }
    }
}
