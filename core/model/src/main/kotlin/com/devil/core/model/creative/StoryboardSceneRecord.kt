package com.devil.core.model.creative

/**
 * Immutable Stage 170 representation of one bounded storyboard scene.
 *
 * This record preserves:
 *
 * - one positive storyboard position;
 * - one explicitly supplied nonblank storyboard scene description.
 *
 * It does not:
 *
 * - represent an AnimationSceneRecord;
 * - represent a generated storyboard panel;
 * - contain image bytes, files, assets, frames, or rendered media;
 * - select shots, lenses, camera motion, keyframes, or animation timing;
 * - establish character or location consistency;
 * - invoke providers or capabilities;
 * - execute anything;
 * - establish constitutional Observation, Verification, or Outcome;
 * - create or persist Memory.
 *
 * STORYBOARD_SCENE != ANIMATION_SCENE.
 * STORYBOARD_SCENE != GENERATED_PANEL.
 * STORYBOARD_SCENE != FRAME.
 * STORYBOARD_SCENE != EXECUTION.
 */
@ConsistentCopyVisibility
data class StoryboardSceneRecord private constructor(
    val position: Int,
    val sceneDescription: String,
) {
    companion object {

        fun create(
            position: Int,
            sceneDescription: String,
        ): StoryboardSceneRecord {
            val normalizedSceneDescription =
                sceneDescription.trim()

            require(position > 0) {
                "Storyboard scene position must be positive."
            }

            require(normalizedSceneDescription.isNotEmpty()) {
                "Storyboard scene description must not be blank."
            }

            return StoryboardSceneRecord(
                position = position,
                sceneDescription = normalizedSceneDescription,
            )
        }
    }
}
