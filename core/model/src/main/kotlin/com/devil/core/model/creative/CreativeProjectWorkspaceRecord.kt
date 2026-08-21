package com.devil.core.model.creative

/**
 * Immutable Stage 174 representation of one bounded Creative Project Workspace.
 *
 * This record preserves:
 *
 * - one exact existing Stage 173 VideoCreationAssistanceRecord;
 * - one ordered collection of explicitly supplied workspace asset contexts;
 * - one explicitly supplied nonblank shot-state description;
 * - one explicitly supplied nonblank episode-continuity description;
 * - one explicitly supplied nonblank workspace objective.
 *
 * The workspace provides structured creative-production continuity for recurring
 * characters, locations, landmarks, environments, props, shots, and episodes.
 *
 * Stage 174 does not:
 *
 * - create another intelligence, Brain, Executive, Planner, or Memory Authority;
 * - inspect image, audio, or video bytes;
 * - generate images, animation, audio, music, voices, or video;
 * - create frames, keyframes, rendered media, files, or generated assets;
 * - invoke providers, models, generators, renderers, codecs, or capabilities;
 * - verify character, location, asset, shot, or episode continuity;
 * - establish that supplied shot state was actually observed;
 * - create an ExecutionRequest;
 * - authorize or execute capabilities;
 * - perform editing, compositing, rendering, or encoding;
 * - establish constitutional Observation, Verification, or Outcome;
 * - mutate World Model state;
 * - perform constitutional Learning;
 * - create a Memory Proposal;
 * - invoke Memory Authority;
 * - commit or persist Memory;
 * - write workspace state to disk, database, cloud, or another storage system;
 * - publish, upload, distribute, or transmit media.
 *
 * CREATIVE_PROJECT_WORKSPACE != ANOTHER_INTELLIGENCE.
 * CREATIVE_WORKSPACE != MEMORY.
 * CREATIVE_WORKSPACE != MEMORY_AUTHORITY.
 * WORKSPACE_ASSET_RECORD != GENERATED_ASSET.
 * WORKSPACE_ASSET_RECORD != FILE.
 * CHARACTER_RECORD != VERIFIED_CHARACTER_IDENTITY.
 * LOCATION_RECORD != VERIFIED_LOCATION_IDENTITY.
 * SUPPLIED_CONTINUITY != VERIFIED_VISUAL_CONSISTENCY.
 * SUPPLIED_SHOT_STATE != OBSERVED_SHOT_STATE.
 * EPISODE_CONTINUITY != CONSTITUTIONAL_MEMORY.
 * WORKSPACE_PREPARED != WORKSPACE_PERSISTED.
 * WORKSPACE_RECORD != STORAGE_SUCCESS.
 * VIDEO_CREATION_ASSISTANCE != VIDEO_GENERATOR.
 * REQUESTED_MP4 != MP4_FILE_EXISTS.
 * CAPABILITY != PROVIDER.
 * PREPARED != EXECUTION.
 * GENERATED != VERIFIED.
 * GENERATION != PUBLISHING_AUTHORIZATION.
 */
@ConsistentCopyVisibility
data class CreativeProjectWorkspaceRecord private constructor(
    val videoCreationAssistance: VideoCreationAssistanceRecord,
    val assets: List<CreativeWorkspaceAssetRecord>,
    val suppliedShotStateDescription: String,
    val suppliedEpisodeContinuityDescription: String,
    val workspaceObjective: String,
) {
    companion object {

        fun create(
            videoCreationAssistance: VideoCreationAssistanceRecord,
            assets: List<CreativeWorkspaceAssetRecord>,
            suppliedShotStateDescription: String,
            suppliedEpisodeContinuityDescription: String,
            workspaceObjective: String,
        ): CreativeProjectWorkspaceRecord {
            val normalizedShotStateDescription =
                suppliedShotStateDescription.trim()

            val normalizedEpisodeContinuityDescription =
                suppliedEpisodeContinuityDescription.trim()

            val normalizedWorkspaceObjective =
                workspaceObjective.trim()

            require(normalizedShotStateDescription.isNotEmpty()) {
                "Creative Project Workspace shot-state description must not be blank."
            }

            require(normalizedEpisodeContinuityDescription.isNotEmpty()) {
                "Creative Project Workspace episode-continuity description must not be blank."
            }

            require(normalizedWorkspaceObjective.isNotEmpty()) {
                "Creative Project Workspace objective must not be blank."
            }

            require(
                assets.map { it.position } ==
                    assets.indices.toList(),
            ) {
                "Creative Project Workspace assets must use contiguous zero-based ordered positions."
            }

            return CreativeProjectWorkspaceRecord(
                videoCreationAssistance = videoCreationAssistance,
                assets = assets.toList(),
                suppliedShotStateDescription =
                    normalizedShotStateDescription,
                suppliedEpisodeContinuityDescription =
                    normalizedEpisodeContinuityDescription,
                workspaceObjective =
                    normalizedWorkspaceObjective,
            )
        }
    }
}
