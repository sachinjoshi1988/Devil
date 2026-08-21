package com.devil.core.model.creative

/**
 * Immutable Stage 171 representation of one explicitly supplied
 * Story-to-Animation production-pipeline step.
 *
 * This record preserves:
 *
 * - one positive pipeline position;
 * - one explicitly supplied nonblank pipeline-step description.
 *
 * A pipeline step expresses intended production structure only.
 *
 * It does not:
 *
 * - represent an AnimationSceneRecord;
 * - represent a StoryboardSceneRecord;
 * - represent a shot, frame, keyframe, asset, or rendered medium;
 * - prove that any described production operation occurred;
 * - authorize or execute a capability;
 * - select or invoke a provider, model, generator, or renderer;
 * - establish constitutional Observation, Verification, or Outcome;
 * - mutate World Model state;
 * - perform constitutional Learning;
 * - create or persist Memory;
 * - or authorize publishing.
 *
 * PIPELINE_STEP != ANIMATION_SCENE.
 * PIPELINE_STEP != STORYBOARD_SCENE.
 * PIPELINE_STEP != EXECUTION.
 * PLANNED_OBSERVATION != CONSTITUTIONAL_OBSERVATION.
 * PLANNED_VERIFICATION != CONSTITUTIONAL_VERIFICATION.
 */
@ConsistentCopyVisibility
data class StoryToAnimationPipelineStepRecord private constructor(
    val position: Int,
    val description: String,
) {
    companion object {

        fun create(
            position: Int,
            description: String,
        ): StoryToAnimationPipelineStepRecord {
            val normalizedDescription =
                description.trim()

            require(position > 0) {
                "Story-to-Animation pipeline step position must be positive."
            }

            require(normalizedDescription.isNotEmpty()) {
                "Story-to-Animation pipeline step description must not be blank."
            }

            return StoryToAnimationPipelineStepRecord(
                position = position,
                description = normalizedDescription,
            )
        }
    }
}
