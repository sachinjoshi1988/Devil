package com.devil.core.runtime.creative

import com.devil.core.model.common.TraceId
import com.devil.core.model.creative.AnimationSceneRecord
import com.devil.core.model.creative.CreativeMediaProjectRecord
import com.devil.core.model.creative.StorySource
import com.devil.core.model.creative.StoryToAnimationRecord

/**
 * Stage 88 bounded Story-to-Animation Foundation coordinator.
 *
 * This coordinator extends one already-existing Stage 87 Creative Media
 * project with explicitly supplied Story-to-Animation structure.
 *
 * Inputs are:
 *
 * - constitutional TraceId;
 * - existing CreativeMediaProjectRecord;
 * - explicit story content;
 * - explicit ordered scene summaries.
 *
 * The coordinator does not split story prose into scenes. Scene decomposition
 * must already have been supplied by the caller.
 *
 * It does not:
 *
 * - infer creative intent from raw conversation;
 * - interpret story meaning;
 * - infer characters;
 * - infer chronology;
 * - infer scenes;
 * - invent scenes;
 * - create another Devil intelligence;
 * - create another Brain;
 * - create another Constitution;
 * - create another Executive;
 * - create another Planner;
 * - create another Unified Devil Runtime;
 * - create Story-to-Animation-specific Memory or Security authorities;
 * - resolve or infer identity;
 * - authenticate a subject;
 * - establish trust;
 * - grant authorization;
 * - establish or validate a security session;
 * - enter Owner Mode;
 * - create a constitutional Decision;
 * - create Tasks or Plans;
 * - register, select, authorize, or activate capabilities;
 * - establish capability availability, health, or readiness;
 * - select or invoke an image, audio, video, text, or multimodal model;
 * - select or invoke a renderer or generator;
 * - invoke UnifiedDevilRuntime;
 * - create execution requests;
 * - execute actions;
 * - create shots;
 * - create storyboards;
 * - create frame sequences;
 * - create keyframes;
 * - create animation timelines;
 * - generate images;
 * - generate audio;
 * - generate video;
 * - render media;
 * - create assets;
 * - create or write files;
 * - communicate with platform APIs;
 * - establish Observation, Verification, or Outcome;
 * - mutate World Model state;
 * - perform constitutional Learning;
 * - propose, commit, or persist Memory;
 * - or persist Story-to-Animation state.
 *
 * STORY_SOURCE != STORY_UNDERSTANDING.
 * STORY != SCENE_DECOMPOSITION.
 * SCENE != FRAME.
 * SCENE_SEQUENCE != FRAME_SEQUENCE.
 * SCENE_SEQUENCE != ANIMATION_TIMELINE.
 * STORY_TO_ANIMATION_PREPARED != GENERATED.
 * STORY_TO_ANIMATION_PREPARED != EXECUTION.
 * GENERATED != VERIFIED_OUTCOME.
 */
class StoryToAnimationCoordinator {

    fun prepare(
        traceId: TraceId,
        creativeProject: CreativeMediaProjectRecord,
        story: String,
        sceneSummaries: List<String>,
    ): StoryToAnimationPreparationResult {
        if (
            story.isBlank() ||
            sceneSummaries.isEmpty() ||
            sceneSummaries.any { it.isBlank() }
        ) {
            return deferred(
                traceId = traceId,
            )
        }

        val storySource =
            StorySource.from(
                rawContent = story,
            )

        val scenes =
            sceneSummaries.mapIndexed { index, summary ->
                AnimationSceneRecord.create(
                    position = index + 1,
                    summary = summary,
                )
            }

        val record =
            StoryToAnimationRecord.create(
                creativeProject = creativeProject,
                story = storySource,
                scenes = scenes,
            )

        return StoryToAnimationPreparationResult.create(
            traceId = traceId,
            status =
                StoryToAnimationPreparationStatus.PREPARED,
            record = record,
        )
    }

    private fun deferred(
        traceId: TraceId,
    ): StoryToAnimationPreparationResult {
        return StoryToAnimationPreparationResult.create(
            traceId = traceId,
            status =
                StoryToAnimationPreparationStatus.DEFERRED,
        )
    }
}
