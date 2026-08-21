package com.devil.core.runtime.creative

import com.devil.core.model.common.TraceId
import com.devil.core.model.creative.StoryCreationRecord
import com.devil.core.model.creative.StorySource
import com.devil.core.model.creative.StoryToStoryboardRecord
import com.devil.core.model.creative.StoryboardSceneRecord

/**
 * Stage 170 bounded Story-to-Storyboard coordinator.
 *
 * This coordinator preserves one exact existing Stage 169 Story Creation record
 * and prepares explicitly supplied story and storyboard-scene structure.
 *
 * Stage 169 remains authoritative for preserved Story Creation provenance.
 *
 * The supplied story is not treated as generated Stage 169 output.
 *
 * This coordinator does not:
 *
 * - generate story prose;
 * - reinterpret StorySource as a Story Creation result;
 * - reinterpret AnimationSceneRecord as storyboard state;
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
class StoryToStoryboardCoordinator {

    fun prepare(
        traceId: TraceId,
        storyCreation: StoryCreationRecord,
        story: String,
        storyboardSceneDescriptions: List<String>,
        storyboardObjective: String,
    ): StoryToStoryboardPreparationResult {
        if (
            story.isBlank() ||
            storyboardSceneDescriptions.isEmpty() ||
            storyboardSceneDescriptions.any { it.isBlank() } ||
            storyboardObjective.isBlank()
        ) {
            return deferred(traceId)
        }

        val storySource =
            StorySource.from(
                rawContent = story,
            )

        val storyboardScenes =
            storyboardSceneDescriptions.mapIndexed { index, description ->
                StoryboardSceneRecord.create(
                    position = index + 1,
                    sceneDescription = description,
                )
            }

        val storyboard =
            StoryToStoryboardRecord.create(
                storyCreation = storyCreation,
                story = storySource,
                storyboardScenes = storyboardScenes,
                storyboardObjective = storyboardObjective,
            )

        return StoryToStoryboardPreparationResult.create(
            traceId = traceId,
            status =
                StoryToStoryboardPreparationStatus.PREPARED,
            storyboard = storyboard,
        )
    }

    private fun deferred(
        traceId: TraceId,
    ): StoryToStoryboardPreparationResult {
        return StoryToStoryboardPreparationResult.create(
            traceId = traceId,
            status =
                StoryToStoryboardPreparationStatus.DEFERRED,
        )
    }
}
