package com.devil.core.runtime.creative

import com.devil.core.model.common.TraceId
import com.devil.core.model.creative.AnimationSceneRecord
import com.devil.core.model.creative.AudioMusicCreativeAssistanceRecord
import com.devil.core.model.creative.CreativeMediaIntegrationRecord
import com.devil.core.model.creative.CreativeMediaMedium
import com.devil.core.model.creative.CreativeMediaObjective
import com.devil.core.model.creative.CreativeMediaProjectId
import com.devil.core.model.creative.CreativeMediaProjectRecord
import com.devil.core.model.creative.StoryCreationRecord
import com.devil.core.model.creative.StorySource
import com.devil.core.model.creative.StoryToAnimationPipelineRecord
import com.devil.core.model.creative.StoryToAnimationPipelineStepRecord
import com.devil.core.model.creative.StoryToAnimationRecord
import com.devil.core.model.creative.StoryToStoryboardRecord
import com.devil.core.model.creative.StoryboardSceneRecord
import com.devil.core.model.creative.VideoGenerationMode
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage173VideoCreationAssistanceGovernanceTest {

    @Test
    fun `coordinator prepares text to video context with MP4 requirement`() {
        val pipeline = stage171Pipeline()
        val audioMusic = stage172Assistance(pipeline)

        val result =
            VideoCreationAssistanceCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace:stage173-text-to-video",
                    ),
                storyToAnimationPipeline = pipeline,
                audioMusicAssistance = audioMusic,
                generationMode =
                    VideoGenerationMode.TEXT_TO_VIDEO,
                suppliedPrompt =
                    "Create a cartoon city sequence.",
                suppliedSourceContextDescription =
                    "Text prompt source context.",
                motionCameraDirection =
                    "Slow cinematic push forward.",
                requestedOutputFormat =
                    "MP4",
                videoCreationObjective =
                    "Prepare text-to-video requirements.",
            )

        assertEquals(
            VideoCreationAssistancePreparationStatus.PREPARED,
            result.status,
        )

        val assistance =
            requireNotNull(result.assistance)

        assertSame(
            pipeline,
            assistance.storyToAnimationPipeline,
        )
        assertSame(
            audioMusic,
            assistance.audioMusicAssistance,
        )
        assertEquals(
            VideoGenerationMode.TEXT_TO_VIDEO,
            assistance.generationMode,
        )
        assertEquals(
            "MP4",
            assistance.requestedOutputFormat,
        )
    }

    @Test
    fun `coordinator prepares image to video context with supplied reference context`() {
        val pipeline = stage171Pipeline()
        val audioMusic = stage172Assistance(pipeline)

        val result =
            VideoCreationAssistanceCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace:stage173-image-to-video",
                    ),
                storyToAnimationPipeline = pipeline,
                audioMusicAssistance = audioMusic,
                generationMode =
                    VideoGenerationMode.IMAGE_TO_VIDEO,
                suppliedPrompt =
                    "Make the supplied character walk and smile.",
                suppliedSourceContextDescription =
                    "Supplied reference image is intended as image-to-video source context.",
                motionCameraDirection =
                    "Track the character from the front.",
                requestedOutputFormat =
                    "MP4",
                videoCreationObjective =
                    "Prepare image-to-video requirements.",
            )

        assertEquals(
            VideoCreationAssistancePreparationStatus.PREPARED,
            result.status,
        )

        val assistance =
            requireNotNull(result.assistance)

        assertEquals(
            VideoGenerationMode.IMAGE_TO_VIDEO,
            assistance.generationMode,
        )
        assertEquals(
            "Supplied reference image is intended as image-to-video source context.",
            assistance.suppliedSourceContextDescription,
        )
    }

    @Test
    fun `blank preparation metadata defers`() {
        val pipeline = stage171Pipeline()
        val audioMusic = stage172Assistance(pipeline)

        val result =
            VideoCreationAssistanceCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace:stage173-deferred",
                    ),
                storyToAnimationPipeline = pipeline,
                audioMusicAssistance = audioMusic,
                generationMode =
                    VideoGenerationMode.TEXT_TO_VIDEO,
                suppliedPrompt = "   ",
                suppliedSourceContextDescription =
                    "Text source.",
                motionCameraDirection =
                    "Static camera.",
                requestedOutputFormat =
                    "MP4",
                videoCreationObjective =
                    "Prepare video.",
            )

        assertEquals(
            VideoCreationAssistancePreparationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.assistance)
    }

    @Test
    fun `prepared result requires one assistance context`() {
        assertFailsWith<IllegalArgumentException> {
            VideoCreationAssistancePreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace:stage173-invalid-prepared",
                    ),
                status =
                    VideoCreationAssistancePreparationStatus.PREPARED,
            )
        }
    }

    @Test
    fun `deferred result rejects assistance context`() {
        val pipeline = stage171Pipeline()
        val audioMusic = stage172Assistance(pipeline)

        val prepared =
            VideoCreationAssistanceCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace:stage173-source",
                    ),
                storyToAnimationPipeline = pipeline,
                audioMusicAssistance = audioMusic,
                generationMode =
                    VideoGenerationMode.TEXT_TO_VIDEO,
                suppliedPrompt =
                    "Create a bounded video.",
                suppliedSourceContextDescription =
                    "Text source.",
                motionCameraDirection =
                    "Static camera.",
                requestedOutputFormat =
                    "MP4",
                videoCreationObjective =
                    "Prepare video.",
            )

        val assistance =
            requireNotNull(prepared.assistance)

        assertFailsWith<IllegalArgumentException> {
            VideoCreationAssistancePreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace:stage173-invalid-deferred",
                    ),
                status =
                    VideoCreationAssistancePreparationStatus.DEFERRED,
                assistance = assistance,
            )
        }
    }

    private fun stage172Assistance(
        pipeline: StoryToAnimationPipelineRecord,
    ): AudioMusicCreativeAssistanceRecord {
        return AudioMusicCreativeAssistanceRecord.create(
            storyToAnimationPipeline = pipeline,
            audioMusicFocus =
                "Narration and music",
            suppliedAudioMusicContextDescription =
                "Supplied Stage 172 context.",
            audioMusicObjective =
                "Prepare audio/music requirements.",
        )
    }

    private fun stage171Pipeline(): StoryToAnimationPipelineRecord {
        val story =
            "Shared supplied Stage 173 runtime story."

        val project =
            CreativeMediaProjectRecord.create(
                projectId =
                    CreativeMediaProjectId.from(
                        "stage173-runtime-project",
                    ),
                objective =
                    CreativeMediaObjective.create(
                        medium =
                            CreativeMediaMedium.from(
                                "Animated video",
                            ),
                        objective =
                            "Prepare bounded video work.",
                    ),
            )

        val integration =
            CreativeMediaIntegrationRecord.create(
                creativeProject = project,
                integrationFocus =
                    "Creative video integration",
                suppliedCreativeMediaContextDescription =
                    "Creative Media context.",
                integrationObjective =
                    "Prepare integration.",
            )

        val storyCreation =
            StoryCreationRecord.create(
                creativeMediaIntegration = integration,
                storyCreationFocus =
                    "Narrative preparation",
                suppliedStoryCreationContextDescription =
                    "Supplied Story Creation context.",
                storyCreationObjective =
                    "Prepare Story Creation.",
            )

        val storyToAnimation =
            StoryToAnimationRecord.create(
                creativeProject = project,
                story = StorySource.from(story),
                scenes =
                    listOf(
                        AnimationSceneRecord.create(
                            position = 1,
                            summary =
                                "Supplied animation scene.",
                        ),
                    ),
            )

        val storyboard =
            StoryToStoryboardRecord.create(
                storyCreation = storyCreation,
                story = StorySource.from(story),
                storyboardScenes =
                    listOf(
                        StoryboardSceneRecord.create(
                            position = 1,
                            sceneDescription =
                                "Supplied storyboard scene.",
                        ),
                    ),
                storyboardObjective =
                    "Prepare storyboard.",
            )

        return StoryToAnimationPipelineRecord.create(
            storyToAnimation = storyToAnimation,
            storyboard = storyboard,
            productionSteps =
                listOf(
                    StoryToAnimationPipelineStepRecord.create(
                        position = 1,
                        description =
                            "Prepare video generation requirements.",
                    ),
                ),
            productionObjective =
                "Prepare Story-to-Animation pipeline.",
        )
    }
}
