package com.devil.core.model.creative

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class VideoCreationAssistanceStage173Test {

    @Test
    fun `text to video record preserves exact provenance and normalizes metadata`() {
        val pipeline = stage171Pipeline()
        val audioMusic = stage172Assistance(pipeline)

        val record =
            VideoCreationAssistanceRecord.create(
                storyToAnimationPipeline = pipeline,
                audioMusicAssistance = audioMusic,
                generationMode = VideoGenerationMode.TEXT_TO_VIDEO,
                suppliedPrompt =
                    "  A cartoon devil walks through a futuristic city.  ",
                suppliedSourceContextDescription =
                    "  Text prompt is the primary source context.  ",
                motionCameraDirection =
                    "  Slow forward camera movement with character walking toward frame center.  ",
                requestedOutputFormat = "  MP4  ",
                videoCreationObjective =
                    "  Prepare bounded text-to-video generation requirements.  ",
            )

        assertSame(pipeline, record.storyToAnimationPipeline)
        assertSame(audioMusic, record.audioMusicAssistance)
        assertEquals(
            VideoGenerationMode.TEXT_TO_VIDEO,
            record.generationMode,
        )
        assertEquals(
            "A cartoon devil walks through a futuristic city.",
            record.suppliedPrompt,
        )
        assertEquals(
            "Text prompt is the primary source context.",
            record.suppliedSourceContextDescription,
        )
        assertEquals(
            "Slow forward camera movement with character walking toward frame center.",
            record.motionCameraDirection,
        )
        assertEquals(
            "MP4",
            record.requestedOutputFormat,
        )
        assertEquals(
            "Prepare bounded text-to-video generation requirements.",
            record.videoCreationObjective,
        )
    }

    @Test
    fun `image to video record preserves requested image to video mode`() {
        val pipeline = stage171Pipeline()
        val audioMusic = stage172Assistance(pipeline)

        val record =
            VideoCreationAssistanceRecord.create(
                storyToAnimationPipeline = pipeline,
                audioMusicAssistance = audioMusic,
                generationMode = VideoGenerationMode.IMAGE_TO_VIDEO,
                suppliedPrompt =
                    "Animate the supplied character walking forward and smiling.",
                suppliedSourceContextDescription =
                    "Use the supplied reference image as future image-to-video source context.",
                motionCameraDirection =
                    "Character walks forward while camera tracks smoothly.",
                requestedOutputFormat =
                    "MP4",
                videoCreationObjective =
                    "Prepare bounded image-to-video requirements.",
            )

        assertEquals(
            VideoGenerationMode.IMAGE_TO_VIDEO,
            record.generationMode,
        )
        assertEquals(
            "MP4",
            record.requestedOutputFormat,
        )
    }

    @Test
    fun `record rejects Stage 172 provenance from another pipeline`() {
        val pipeline = stage171Pipeline(
            projectId = "stage173-primary",
            story = "Primary Stage 173 story.",
        )

        val otherPipeline =
            stage171Pipeline(
                projectId = "stage173-other",
                story = "Other Stage 173 story.",
            )

        val audioMusic =
            stage172Assistance(otherPipeline)

        assertFailsWith<IllegalArgumentException> {
            VideoCreationAssistanceRecord.create(
                storyToAnimationPipeline = pipeline,
                audioMusicAssistance = audioMusic,
                generationMode = VideoGenerationMode.IMAGE_TO_VIDEO,
                suppliedPrompt =
                    "Animate supplied image.",
                suppliedSourceContextDescription =
                    "Supplied image context.",
                motionCameraDirection =
                    "Static camera.",
                requestedOutputFormat =
                    "MP4",
                videoCreationObjective =
                    "Prepare video requirements.",
            )
        }
    }

    @Test
    fun `record rejects blank bounded metadata`() {
        val pipeline = stage171Pipeline()
        val audioMusic = stage172Assistance(pipeline)

        assertFailsWith<IllegalArgumentException> {
            VideoCreationAssistanceRecord.create(
                storyToAnimationPipeline = pipeline,
                audioMusicAssistance = audioMusic,
                generationMode = VideoGenerationMode.TEXT_TO_VIDEO,
                suppliedPrompt = "   ",
                suppliedSourceContextDescription =
                    "Text source context.",
                motionCameraDirection =
                    "Static camera.",
                requestedOutputFormat =
                    "MP4",
                videoCreationObjective =
                    "Prepare video.",
            )
        }

        assertFailsWith<IllegalArgumentException> {
            VideoCreationAssistanceRecord.create(
                storyToAnimationPipeline = pipeline,
                audioMusicAssistance = audioMusic,
                generationMode = VideoGenerationMode.TEXT_TO_VIDEO,
                suppliedPrompt =
                    "Create a video.",
                suppliedSourceContextDescription = "   ",
                motionCameraDirection =
                    "Static camera.",
                requestedOutputFormat =
                    "MP4",
                videoCreationObjective =
                    "Prepare video.",
            )
        }

        assertFailsWith<IllegalArgumentException> {
            VideoCreationAssistanceRecord.create(
                storyToAnimationPipeline = pipeline,
                audioMusicAssistance = audioMusic,
                generationMode = VideoGenerationMode.TEXT_TO_VIDEO,
                suppliedPrompt =
                    "Create a video.",
                suppliedSourceContextDescription =
                    "Text source context.",
                motionCameraDirection = "   ",
                requestedOutputFormat =
                    "MP4",
                videoCreationObjective =
                    "Prepare video.",
            )
        }

        assertFailsWith<IllegalArgumentException> {
            VideoCreationAssistanceRecord.create(
                storyToAnimationPipeline = pipeline,
                audioMusicAssistance = audioMusic,
                generationMode = VideoGenerationMode.TEXT_TO_VIDEO,
                suppliedPrompt =
                    "Create a video.",
                suppliedSourceContextDescription =
                    "Text source context.",
                motionCameraDirection =
                    "Static camera.",
                requestedOutputFormat = "   ",
                videoCreationObjective =
                    "Prepare video.",
            )
        }

        assertFailsWith<IllegalArgumentException> {
            VideoCreationAssistanceRecord.create(
                storyToAnimationPipeline = pipeline,
                audioMusicAssistance = audioMusic,
                generationMode = VideoGenerationMode.TEXT_TO_VIDEO,
                suppliedPrompt =
                    "Create a video.",
                suppliedSourceContextDescription =
                    "Text source context.",
                motionCameraDirection =
                    "Static camera.",
                requestedOutputFormat =
                    "MP4",
                videoCreationObjective = "   ",
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
                "Supplied Stage 172 audio/music context.",
            audioMusicObjective =
                "Prepare audio/music requirements.",
        )
    }

    private fun stage171Pipeline(
        projectId: String = "stage173-project",
        story: String = "Shared supplied Stage 173 story.",
    ): StoryToAnimationPipelineRecord {
        val project =
            CreativeMediaProjectRecord.create(
                projectId =
                    CreativeMediaProjectId.from(
                        projectId,
                    ),
                objective =
                    CreativeMediaObjective.create(
                        medium =
                            CreativeMediaMedium.from(
                                "Animated video",
                            ),
                        objective =
                            "Prepare bounded creative video work.",
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
                    "Prepare Creative Media integration.",
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
                            "Prepare bounded video production requirements.",
                    ),
                ),
            productionObjective =
                "Prepare Story-to-Animation pipeline.",
        )
    }
}
