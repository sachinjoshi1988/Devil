package com.devil.core.model.creative

/**
 * Immutable Stage 169 representation of one bounded Story Creation context.
 *
 * This record preserves:
 *
 * - one exact existing Stage 166 CreativeMediaIntegrationRecord;
 * - one explicitly supplied nonblank story-creation focus;
 * - one explicitly supplied nonblank story-creation context description;
 * - one explicitly supplied nonblank story-creation objective.
 *
 * Stage 169 represents provider-neutral Story Creation preparation only.
 *
 * It does not:
 *
 * - create another Devil intelligence;
 * - replace or reconstruct the preserved Stage 166 integration;
 * - generate actual story prose, screenplay, dialogue, or episode text;
 * - reinterpret StorySource as generated Story Creation output;
 * - decompose stories into scenes;
 * - create shots, framing, camera intentions, or storyboards;
 * - implement Story-to-Animation;
 * - generate image, audio, or video output;
 * - select or invoke text, creative, or multimodal providers or models;
 * - establish authorship, originality, copyright ownership, or licensing;
 * - create files or persistent creative assets;
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
@ConsistentCopyVisibility
data class StoryCreationRecord private constructor(
    val creativeMediaIntegration: CreativeMediaIntegrationRecord,
    val storyCreationFocus: String,
    val suppliedStoryCreationContextDescription: String,
    val storyCreationObjective: String,
) {
    companion object {

        fun create(
            creativeMediaIntegration: CreativeMediaIntegrationRecord,
            storyCreationFocus: String,
            suppliedStoryCreationContextDescription: String,
            storyCreationObjective: String,
        ): StoryCreationRecord {
            val normalizedStoryCreationFocus =
                storyCreationFocus.trim()

            val normalizedSuppliedStoryCreationContextDescription =
                suppliedStoryCreationContextDescription.trim()

            val normalizedStoryCreationObjective =
                storyCreationObjective.trim()

            require(normalizedStoryCreationFocus.isNotEmpty()) {
                "Story Creation focus must not be blank."
            }

            require(normalizedSuppliedStoryCreationContextDescription.isNotEmpty()) {
                "Story Creation context description must not be blank."
            }

            require(normalizedStoryCreationObjective.isNotEmpty()) {
                "Story Creation objective must not be blank."
            }

            return StoryCreationRecord(
                creativeMediaIntegration = creativeMediaIntegration,
                storyCreationFocus = normalizedStoryCreationFocus,
                suppliedStoryCreationContextDescription =
                    normalizedSuppliedStoryCreationContextDescription,
                storyCreationObjective = normalizedStoryCreationObjective,
            )
        }
    }
}
