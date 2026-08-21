package com.devil.core.runtime.creative

import com.devil.core.model.common.TraceId
import com.devil.core.model.creative.StoryToAnimationPipelineRecord
import com.devil.core.model.creative.StoryToAnimationPipelineStepRecord
import com.devil.core.model.creative.StoryToAnimationRecord
import com.devil.core.model.creative.StoryToStoryboardRecord

/**
 * Stage 171 bounded Story-to-Animation Pipeline coordinator.
 *
 * This coordinator preserves one exact Stage 88 Story-to-Animation record and
 * one exact Stage 170 Story-to-Storyboard record, then prepares explicitly
 * supplied production-pipeline structure around them.
 *
 * It does not:
 *
 * - replace Stage 88 or Stage 170 architecture;
 * - generate stories, storyboard panels, shots, frames, or keyframes;
 * - create or approve character, location, or prop assets;
 * - generate images, animation, audio, music, voices, or video;
 * - perform editing or rendering;
 * - select or invoke providers, models, generators, renderers, or capabilities;
 * - authorize or execute anything;
 * - create an ExecutionRequest;
 * - establish constitutional Observation, Verification, or Outcome;
 * - mutate World Model state;
 * - perform constitutional Learning;
 * - create or persist Memory;
 * - authorize publishing;
 * - or implement Stages 172 through 174.
 *
 * PIPELINE_STEP != EXECUTION.
 * PIPELINE_PREPARED != GENERATION.
 * PIPELINE_PREPARED != EXECUTION.
 * SUPPLIED_ASSET_REQUIREMENT != APPROVED_ASSET.
 * PLANNED_OBSERVATION != CONSTITUTIONAL_OBSERVATION.
 * PLANNED_VERIFICATION != CONSTITUTIONAL_VERIFICATION.
 * GENERATED != VERIFIED.
 * GENERATION != PUBLISHING_AUTHORIZATION.
 */
class StoryToAnimationPipelineCoordinator {

    fun prepare(
        traceId: TraceId,
        storyToAnimation: StoryToAnimationRecord,
        storyboard: StoryToStoryboardRecord,
        productionStepDescriptions: List<String>,
        productionObjective: String,
    ): StoryToAnimationPipelinePreparationResult {
        if (
            productionStepDescriptions.isEmpty() ||
            productionStepDescriptions.any { it.isBlank() } ||
            productionObjective.isBlank()
        ) {
            return deferred(traceId)
        }

        val productionSteps =
            productionStepDescriptions.mapIndexed { index, description ->
                StoryToAnimationPipelineStepRecord.create(
                    position = index + 1,
                    description = description,
                )
            }

        val pipeline =
            StoryToAnimationPipelineRecord.create(
                storyToAnimation = storyToAnimation,
                storyboard = storyboard,
                productionSteps = productionSteps,
                productionObjective = productionObjective,
            )

        return StoryToAnimationPipelinePreparationResult.create(
            traceId = traceId,
            status =
                StoryToAnimationPipelinePreparationStatus.PREPARED,
            pipeline = pipeline,
        )
    }

    private fun deferred(
        traceId: TraceId,
    ): StoryToAnimationPipelinePreparationResult {
        return StoryToAnimationPipelinePreparationResult.create(
            traceId = traceId,
            status =
                StoryToAnimationPipelinePreparationStatus.DEFERRED,
        )
    }
}
