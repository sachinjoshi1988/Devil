package com.devil.core.model.creative

/**
 * Immutable Stage 88 representation of one explicitly supplied ordered
 * animation-scene intention.
 *
 * position expresses ordering only.
 *
 * summary preserves one bounded supplied description of what the scene is
 * intended to represent.
 *
 * An AnimationSceneRecord does not:
 *
 * - infer a scene from StorySource;
 * - prove that the supplied scene accurately represents a story;
 * - create another Brain;
 * - create another Planner;
 * - create a Task or Plan;
 * - select a capability;
 * - select a model or renderer;
 * - create a shot list;
 * - create a storyboard;
 * - create frames;
 * - create keyframes;
 * - create audio;
 * - create dialogue;
 * - create animation timing;
 * - render media;
 * - execute actions;
 * - create files;
 * - establish Observation, Verification, or Outcome;
 * - perform constitutional Learning;
 * - or create or commit Memory.
 *
 * SCENE_RECORD != FRAME.
 * SCENE_RECORD != SHOT.
 * SCENE_RECORD != ANIMATION_TIMELINE.
 * SCENE_RECORD != GENERATED_MEDIA.
 */
@ConsistentCopyVisibility
data class AnimationSceneRecord private constructor(
    val position: Int,
    val summary: String,
) {
    companion object {

        fun create(
            position: Int,
            summary: String,
        ): AnimationSceneRecord {
            val normalizedSummary =
                summary.trim()

            require(position > 0) {
                "Animation scene position must be positive."
            }

            require(normalizedSummary.isNotEmpty()) {
                "Animation scene summary must not be blank."
            }

            return AnimationSceneRecord(
                position = position,
                summary = normalizedSummary,
            )
        }
    }
}
