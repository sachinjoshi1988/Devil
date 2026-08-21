package com.devil.core.model.creative

/**
 * Immutable Stage 171 representation of one bounded Story-to-Animation
 * production-pipeline context.
 *
 * This record preserves:
 *
 * - one exact existing Stage 88 StoryToAnimationRecord;
 * - one exact existing Stage 170 StoryToStoryboardRecord;
 * - one ordered nonempty explicitly supplied pipeline-step sequence;
 * - one explicitly supplied nonblank production objective.
 *
 * The Stage 88 and Stage 170 records must preserve the same supplied
 * StorySource content before they may be combined into one pipeline context.
 *
 * This structural equality requirement does not establish story understanding,
 * semantic equivalence, authorship, generation provenance, or Verification.
 *
 * This record does not:
 *
 * - replace Stage 88 Story-to-Animation architecture;
 * - replace Stage 170 Story-to-Storyboard architecture;
 * - reinterpret StoryboardSceneRecord as AnimationSceneRecord;
 * - generate storyboard panels;
 * - create detailed shots, lenses, camera motion, frames, or keyframes;
 * - create or approve character, location, or prop assets;
 * - generate images, animation, audio, music, voices, or video;
 * - select or invoke providers, models, generators, or renderers;
 * - authorize or execute capabilities;
 * - create an ExecutionRequest;
 * - perform editing or rendering;
 * - establish constitutional Observation, Verification, or Outcome;
 * - mutate World Model state;
 * - perform constitutional Learning;
 * - create or persist Memory;
 * - persist Stage 174 Creative Project Workspace state;
 * - publish, upload, distribute, or transmit media;
 * - or implement Stages 172 through 174.
 *
 * STORYBOARD_SCENE != ANIMATION_SCENE.
 * PIPELINE_STEP != EXECUTION.
 * PIPELINE_PREPARED != GENERATION.
 * PIPELINE_PREPARED != EXECUTION.
 * SUPPLIED_ASSET_REQUIREMENT != APPROVED_ASSET.
 * PLANNED_OBSERVATION != CONSTITUTIONAL_OBSERVATION.
 * PLANNED_VERIFICATION != CONSTITUTIONAL_VERIFICATION.
 * GENERATED != VERIFIED.
 * GENERATION != PUBLISHING_AUTHORIZATION.
 */
@ConsistentCopyVisibility
data class StoryToAnimationPipelineRecord private constructor(
    val storyToAnimation: StoryToAnimationRecord,
    val storyboard: StoryToStoryboardRecord,
    val productionSteps: List<StoryToAnimationPipelineStepRecord>,
    val productionObjective: String,
) {
    companion object {

        fun create(
            storyToAnimation: StoryToAnimationRecord,
            storyboard: StoryToStoryboardRecord,
            productionSteps: List<StoryToAnimationPipelineStepRecord>,
            productionObjective: String,
        ): StoryToAnimationPipelineRecord {
            val normalizedProductionObjective =
                productionObjective.trim()

            require(
                storyToAnimation.story.content ==
                    storyboard.story.content,
            ) {
                "Story-to-Animation and Story-to-Storyboard contexts must preserve the same supplied story content."
            }

            require(productionSteps.isNotEmpty()) {
                "Story-to-Animation pipeline requires at least one supplied production step."
            }

            productionSteps.forEachIndexed { index, step ->
                val expectedPosition =
                    index + 1

                require(step.position == expectedPosition) {
                    "Story-to-Animation pipeline steps must use contiguous ordered positions beginning at one."
                }
            }

            require(normalizedProductionObjective.isNotEmpty()) {
                "Story-to-Animation pipeline production objective must not be blank."
            }

            return StoryToAnimationPipelineRecord(
                storyToAnimation = storyToAnimation,
                storyboard = storyboard,
                productionSteps = productionSteps.toList(),
                productionObjective = normalizedProductionObjective,
            )
        }
    }
}
