package com.devil.core.model.creative

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class AudioMusicCreativeAssistanceStage172Test {

    @Test
    fun `record preserves exact Stage 171 pipeline and normalizes metadata`() {
        val pipeline = stage171Pipeline()

        val record =
            AudioMusicCreativeAssistanceRecord.create(
                storyToAnimationPipeline = pipeline,
                audioMusicFocus =
                    "  Voice narration and music direction  ",
                suppliedAudioMusicContextDescription =
                    "  User supplied narration, ambience, and scoring context.  ",
                audioMusicObjective =
                    "  Preserve bounded audio and music creative requirements.  ",
            )

        assertSame(
            pipeline,
            record.storyToAnimationPipeline,
        )
        assertEquals(
            "Voice narration and music direction",
            record.audioMusicFocus,
        )
        assertEquals(
            "User supplied narration, ambience, and scoring context.",
            record.suppliedAudioMusicContextDescription,
        )
        assertEquals(
            "Preserve bounded audio and music creative requirements.",
            record.audioMusicObjective,
        )
    }

    @Test
    fun `record rejects blank audio music focus`() {
        assertFailsWith<IllegalArgumentException> {
            AudioMusicCreativeAssistanceRecord.create(
                storyToAnimationPipeline = stage171Pipeline(),
                audioMusicFocus = "   ",
                suppliedAudioMusicContextDescription =
                    "Supplied audio context.",
                audioMusicObjective =
                    "Prepare bounded assistance.",
            )
        }
    }

    @Test
    fun `record rejects blank audio music context description`() {
        assertFailsWith<IllegalArgumentException> {
            AudioMusicCreativeAssistanceRecord.create(
                storyToAnimationPipeline = stage171Pipeline(),
                audioMusicFocus =
                    "Narration and scoring",
                suppliedAudioMusicContextDescription = "   ",
                audioMusicObjective =
                    "Prepare bounded assistance.",
            )
        }
    }

    @Test
    fun `record rejects blank audio music objective`() {
        assertFailsWith<IllegalArgumentException> {
            AudioMusicCreativeAssistanceRecord.create(
                storyToAnimationPipeline = stage171Pipeline(),
                audioMusicFocus =
                    "Narration and scoring",
                suppliedAudioMusicContextDescription =
                    "Supplied audio context.",
                audioMusicObjective = "   ",
            )
        }
    }

    private fun stage171Pipeline(): StoryToAnimationPipelineRecord {
        val story = "Shared supplied Stage 172 story."

        val project =
            CreativeMediaProjectRecord.create(
                projectId =
                    CreativeMediaProjectId.from(
                        "stage172-project",
                    ),
                objective =
                    CreativeMediaObjective.create(
                        medium =
                            CreativeMediaMedium.from(
                                "Animated narrative",
                            ),
                        objective =
                            "Prepare bounded animated work.",
                    ),
            )

        val integration =
            CreativeMediaIntegrationRecord.create(
                creativeProject = project,
                integrationFocus =
                    "Creative narrative integration",
                suppliedCreativeMediaContextDescription =
                    "Creative Media context.",
                integrationObjective =
                    "Prepare integration.",
            )

        val storyCreation =
            StoryCreationRecord.create(
                creativeMediaIntegration = integration,
                storyCreationFocus =
                    "Narrative development",
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
                            "Prepare audio production requirements.",
                    ),
                ),
            productionObjective =
                "Prepare bounded production pipeline.",
        )
    }
}
