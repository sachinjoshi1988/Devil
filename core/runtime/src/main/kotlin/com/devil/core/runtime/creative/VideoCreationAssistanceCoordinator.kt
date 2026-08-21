package com.devil.core.runtime.creative

import com.devil.core.model.common.TraceId
import com.devil.core.model.creative.AudioMusicCreativeAssistanceRecord
import com.devil.core.model.creative.StoryToAnimationPipelineRecord
import com.devil.core.model.creative.VideoCreationAssistanceRecord
import com.devil.core.model.creative.VideoGenerationMode

/**
 * Stage 173 bounded Video Creation Assistance coordinator.
 *
 * This coordinator prepares provider-neutral text-to-video or image-to-video intent while
 * preserving the exact supplied Stage 171 pipeline and Stage 172 audio/music assistance.
 *
 * It does not inspect media, invoke models/providers, generate frames/video, encode files,
 * execute capabilities, render media, perform constitutional Verification, persist workspace
 * state, or authorize publishing.
 *
 * VIDEO_CREATION_ASSISTANCE != VIDEO_GENERATOR.
 * TEXT_TO_VIDEO_PREPARED != VIDEO_GENERATED.
 * IMAGE_TO_VIDEO_PREPARED != VIDEO_GENERATED.
 * REQUESTED_MP4 != MP4_FILE_EXISTS.
 * CAPABILITY != PROVIDER.
 * PREPARED != EXECUTION.
 * GENERATED != VERIFIED.
 * GENERATION != PUBLISHING_AUTHORIZATION.
 */
class VideoCreationAssistanceCoordinator {

    fun prepare(
        traceId: TraceId,
        storyToAnimationPipeline: StoryToAnimationPipelineRecord,
        audioMusicAssistance: AudioMusicCreativeAssistanceRecord,
        generationMode: VideoGenerationMode,
        suppliedPrompt: String,
        suppliedSourceContextDescription: String,
        motionCameraDirection: String,
        requestedOutputFormat: String,
        videoCreationObjective: String,
    ): VideoCreationAssistancePreparationResult {
        if (
            suppliedPrompt.isBlank() ||
            suppliedSourceContextDescription.isBlank() ||
            motionCameraDirection.isBlank() ||
            requestedOutputFormat.isBlank() ||
            videoCreationObjective.isBlank()
        ) {
            return deferred(traceId)
        }

        val assistance =
            VideoCreationAssistanceRecord.create(
                storyToAnimationPipeline = storyToAnimationPipeline,
                audioMusicAssistance = audioMusicAssistance,
                generationMode = generationMode,
                suppliedPrompt = suppliedPrompt,
                suppliedSourceContextDescription =
                    suppliedSourceContextDescription,
                motionCameraDirection =
                    motionCameraDirection,
                requestedOutputFormat =
                    requestedOutputFormat,
                videoCreationObjective =
                    videoCreationObjective,
            )

        return VideoCreationAssistancePreparationResult.create(
            traceId = traceId,
            status =
                VideoCreationAssistancePreparationStatus.PREPARED,
            assistance = assistance,
        )
    }

    private fun deferred(
        traceId: TraceId,
    ): VideoCreationAssistancePreparationResult {
        return VideoCreationAssistancePreparationResult.create(
            traceId = traceId,
            status =
                VideoCreationAssistancePreparationStatus.DEFERRED,
        )
    }
}
