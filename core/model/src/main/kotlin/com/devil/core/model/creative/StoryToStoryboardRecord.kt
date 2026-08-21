package com.devil.core.model.creative

/**
 * Immutable Stage 170 representation of one bounded Story-to-Storyboard context.
 *
 * This record preserves:
 *
 * - one exact existing Stage 169 StoryCreationRecord;
 * - one explicitly supplied StorySource;
 * - one ordered nonempty storyboard-scene sequence;
 * - one explicitly supplied nonblank storyboard objective.
 *
 * The supplied StorySource is not treated as generated Stage 169 output.
 *
 * It does not:
 *
 * - reinterpret StorySource as a Story Creation result;
 * - reinterpret Stage 88 AnimationSceneRecord as storyboard state;
 * - generate or render storyboard panels;
 * - create image bytes, files, assets, frames, or rendered media;
 * - select detailed shots, lenses, camera motion, keyframes, or animation timing;
 * - invoke text, image, video, or multimodal providers or models;
 * - authorize or execute capabilities;
 * - establish character or location consistency as verified;
 * - implement the Stage 171 Story-to-Animation pipeline;
 * - create audio or music;
 * - implement video creation;
 * - persist Creative Project Workspace state;
 * - publish, upload, distribute, or transmit media;
 * - establish constitutional Observation, Verification, or Outcome;
 * - mutate World Model state;
 * - perform constitutional Learning;
 * - create or persist Memory;
 * - or implement Stages 171 through 174.
 *
 * STORY_SOURCE != GENERATED_STORY.
 * STORYBOARD_SCENE != ANIMATION_SCENE.
 * STORYBOARD_SCENE != GENERATED_PANEL.
 * STORYBOARD != RENDERED_MEDIA.
 * STORY_TO_STORYBOARD_PREPARED != GENERATION.
 * STORY_TO_STORYBOARD_PREPARED != EXECUTION.
 * STORYBOARD != STORY_TO_ANIMATION_PIPELINE.
 * GENERATED != VERIFIED.
 * GENERATION != PUBLISHING_AUTHORIZATION.
 */
@ConsistentCopyVisibility
data class StoryToStoryboardRecord private constructor(
    val storyCreation: StoryCreationRecord,
    val story: StorySource,
    val storyboardScenes: List<StoryboardSceneRecord>,
    val storyboardObjective: String,
) {
    companion object {

        fun create(
            storyCreation: StoryCreationRecord,
            story: StorySource,
            storyboardScenes: List<StoryboardSceneRecord>,
            storyboardObjective: String,
        ): StoryToStoryboardRecord {
            val normalizedStoryboardObjective =
                storyboardObjective.trim()

            require(storyboardScenes.isNotEmpty()) {
                "Story-to-Storyboard preparation requires at least one supplied storyboard scene."
            }

            storyboardScenes.forEachIndexed { index, scene ->
                val expectedPosition =
                    index + 1

                require(scene.position == expectedPosition) {
                    "Storyboard scenes must use contiguous ordered positions beginning at one."
                }
            }

            require(normalizedStoryboardObjective.isNotEmpty()) {
                "Story-to-Storyboard objective must not be blank."
            }

            return StoryToStoryboardRecord(
                storyCreation = storyCreation,
                story = story,
                storyboardScenes = storyboardScenes.toList(),
                storyboardObjective = normalizedStoryboardObjective,
            )
        }
    }
}
