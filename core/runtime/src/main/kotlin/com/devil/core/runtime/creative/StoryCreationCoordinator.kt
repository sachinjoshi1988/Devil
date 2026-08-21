package com.devil.core.runtime.creative

import com.devil.core.model.common.TraceId
import com.devil.core.model.creative.CreativeMediaIntegrationRecord
import com.devil.core.model.creative.StoryCreationRecord

/**
 * Stage 169 bounded Story Creation coordinator.
 *
 * This coordinator preserves one exact existing Stage 166 Creative Media
 * Integration record and prepares explicitly supplied Story Creation metadata.
 *
 * Stage 166 remains authoritative for preserved Creative Media integration
 * provenance.
 *
 * This coordinator does not:
 *
 * - create another Devil intelligence;
 * - replace or reconstruct the preserved Stage 166 integration;
 * - generate story prose, screenplay, dialogue, or episode text;
 * - reinterpret StorySource as generated Story Creation output;
 * - decompose stories into scenes;
 * - create shots or storyboards;
 * - select or invoke text, creative, or multimodal providers or models;
 * - generate image, audio, or video output;
 * - establish authorship, originality, copyright ownership, or licensing;
 * - create files or persistent assets;
 * - publish, upload, distribute, or transmit content;
 * - authorize or execute capabilities;
 * - establish constitutional Observation, Verification, or Outcome;
 * - mutate World Model state;
 * - perform constitutional Learning;
 * - create or persist Memory;
 * - or implement Stages 170 through 174.
 *
 * STORY_CREATION != PROVIDER.
 * STORY_CREATION_PREPARED != STORY_GENERATED.
 * SUPPLIED_STORY_CONTEXT != GENERATED_STORY.
 * STORY_SOURCE != STORY_CREATION_RESULT.
 * STORY_CREATION != SCENE_DECOMPOSITION.
 * STORY_CREATION != STORYBOARD.
 * GENERATED != VERIFIED.
 * GENERATION != PUBLISHING_AUTHORIZATION.
 * STORY_CREATION_PREPARED != EXECUTION.
 */
class StoryCreationCoordinator {

    fun prepare(
        traceId: TraceId,
        creativeMediaIntegration: CreativeMediaIntegrationRecord,
        storyCreationFocus: String,
        suppliedStoryCreationContextDescription: String,
        storyCreationObjective: String,
    ): StoryCreationPreparationResult {
        if (
            storyCreationFocus.isBlank() ||
            suppliedStoryCreationContextDescription.isBlank() ||
            storyCreationObjective.isBlank()
        ) {
            return deferred(traceId)
        }

        val storyCreation =
            StoryCreationRecord.create(
                creativeMediaIntegration = creativeMediaIntegration,
                storyCreationFocus = storyCreationFocus,
                suppliedStoryCreationContextDescription =
                    suppliedStoryCreationContextDescription,
                storyCreationObjective = storyCreationObjective,
            )

        return StoryCreationPreparationResult.create(
            traceId = traceId,
            status = StoryCreationPreparationStatus.PREPARED,
            storyCreation = storyCreation,
        )
    }

    private fun deferred(
        traceId: TraceId,
    ): StoryCreationPreparationResult {
        return StoryCreationPreparationResult.create(
            traceId = traceId,
            status = StoryCreationPreparationStatus.DEFERRED,
        )
    }
}
