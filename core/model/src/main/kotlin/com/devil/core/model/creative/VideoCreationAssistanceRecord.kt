package com.devil.core.model.creative

/**
 * Immutable Stage 173 representation of one bounded Video Creation Assistance context.
 *
 * This record preserves:
 *
 * - one exact existing Stage 171 StoryToAnimationPipelineRecord;
 * - one exact existing Stage 172 AudioMusicCreativeAssistanceRecord;
 * - one explicit TEXT_TO_VIDEO or IMAGE_TO_VIDEO generation mode;
 * - one explicitly supplied nonblank prompt;
 * - one explicitly supplied nonblank source-context description;
 * - one explicitly supplied nonblank motion/camera direction;
 * - one explicitly supplied nonblank requested output format;
 * - one explicitly supplied nonblank video-creation objective.
 *
 * Stage 173 establishes provider-neutral video-creation intent for future governed
 * text-to-video and image-to-video generation.
 *
 * It does not:
 *
 * - inspect, decode, or verify supplied image bytes;
 * - establish reference-image authenticity or character identity;
 * - generate frames or video;
 * - invoke a video-generation provider, model, generator, renderer, or codec;
 * - encode MP4, WebM, MOV, GIF, or another media format;
 * - create video bytes, files, rendered output, or persistent assets;
 * - execute motion, camera movement, facial expression, or lip synchronization;
 * - perform real editing, compositing, caption rendering, or regeneration;
 * - establish that any requested output file exists;
 * - authorize or execute capabilities;
 * - create an ExecutionRequest;
 * - establish constitutional Observation, Verification, or Outcome;
 * - mutate World Model state;
 * - perform constitutional Learning;
 * - create or persist Memory;
 * - persist Stage 174 Creative Project Workspace state;
 * - publish, upload, distribute, or transmit media.
 *
 * VIDEO_CREATION_ASSISTANCE != VIDEO_GENERATOR.
 * TEXT_TO_VIDEO_PREPARED != VIDEO_GENERATED.
 * IMAGE_TO_VIDEO_PREPARED != VIDEO_GENERATED.
 * SUPPLIED_IMAGE_CONTEXT != IMAGE_BYTES_INSPECTED.
 * REFERENCE_IMAGE != VERIFIED_CHARACTER_IDENTITY.
 * MOTION_DIRECTION != MOTION_EXECUTED.
 * LIP_SYNC_REQUIREMENT != LIP_SYNC_COMPLETED.
 * REQUESTED_OUTPUT_FORMAT != OUTPUT_FILE_EXISTS.
 * REQUESTED_MP4 != MP4_FILE_EXISTS.
 * CAPABILITY != PROVIDER.
 * PREPARED != EXECUTION.
 * GENERATED != VERIFIED.
 * GENERATION != PUBLISHING_AUTHORIZATION.
 */
@ConsistentCopyVisibility
data class VideoCreationAssistanceRecord private constructor(
    val storyToAnimationPipeline: StoryToAnimationPipelineRecord,
    val audioMusicAssistance: AudioMusicCreativeAssistanceRecord,
    val generationMode: VideoGenerationMode,
    val suppliedPrompt: String,
    val suppliedSourceContextDescription: String,
    val motionCameraDirection: String,
    val requestedOutputFormat: String,
    val videoCreationObjective: String,
) {
    companion object {

        fun create(
            storyToAnimationPipeline: StoryToAnimationPipelineRecord,
            audioMusicAssistance: AudioMusicCreativeAssistanceRecord,
            generationMode: VideoGenerationMode,
            suppliedPrompt: String,
            suppliedSourceContextDescription: String,
            motionCameraDirection: String,
            requestedOutputFormat: String,
            videoCreationObjective: String,
        ): VideoCreationAssistanceRecord {
            require(
                audioMusicAssistance.storyToAnimationPipeline ===
                    storyToAnimationPipeline,
            ) {
                "Video Creation Assistance requires Stage 172 audio/music provenance from the exact supplied Stage 171 pipeline."
            }

            val normalizedPrompt =
                suppliedPrompt.trim()

            val normalizedSourceContextDescription =
                suppliedSourceContextDescription.trim()

            val normalizedMotionCameraDirection =
                motionCameraDirection.trim()

            val normalizedRequestedOutputFormat =
                requestedOutputFormat.trim()

            val normalizedVideoCreationObjective =
                videoCreationObjective.trim()

            require(normalizedPrompt.isNotEmpty()) {
                "Video Creation Assistance prompt must not be blank."
            }

            require(normalizedSourceContextDescription.isNotEmpty()) {
                "Video Creation Assistance source context description must not be blank."
            }

            require(normalizedMotionCameraDirection.isNotEmpty()) {
                "Video Creation Assistance motion/camera direction must not be blank."
            }

            require(normalizedRequestedOutputFormat.isNotEmpty()) {
                "Video Creation Assistance requested output format must not be blank."
            }

            require(normalizedVideoCreationObjective.isNotEmpty()) {
                "Video Creation Assistance objective must not be blank."
            }

            return VideoCreationAssistanceRecord(
                storyToAnimationPipeline = storyToAnimationPipeline,
                audioMusicAssistance = audioMusicAssistance,
                generationMode = generationMode,
                suppliedPrompt = normalizedPrompt,
                suppliedSourceContextDescription =
                    normalizedSourceContextDescription,
                motionCameraDirection =
                    normalizedMotionCameraDirection,
                requestedOutputFormat =
                    normalizedRequestedOutputFormat,
                videoCreationObjective =
                    normalizedVideoCreationObjective,
            )
        }
    }
}
