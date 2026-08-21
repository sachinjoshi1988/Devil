package com.devil.core.model.creative

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotSame
import kotlin.test.assertSame

class CreativeProjectWorkspaceStage174Test {

    @Test
    fun `workspace preserves Stage 173 provenance and supplied creative continuity`() {
        val videoCreation =
            stage173VideoCreation()

        val suppliedAssets =
            mutableListOf(
                CreativeWorkspaceAssetRecord.create(
                    position = 0,
                    type = CreativeWorkspaceAssetType.CHARACTER,
                    name = "  Recurring Devil Character  ",
                    suppliedContinuityDescription =
                        "  Same horns, clothing, proportions, and cartoon appearance.  ",
                ),
                CreativeWorkspaceAssetRecord.create(
                    position = 1,
                    type = CreativeWorkspaceAssetType.LOCATION,
                    name = "  YouTube City Street  ",
                    suppliedContinuityDescription =
                        "  Preserve the supplied recurring street and landmark layout.  ",
                ),
                CreativeWorkspaceAssetRecord.create(
                    position = 2,
                    type = CreativeWorkspaceAssetType.PROP,
                    name = "  Golden Microphone  ",
                    suppliedContinuityDescription =
                        "  Preserve the same supplied microphone design across shots.  ",
                ),
            )

        val workspace =
            CreativeProjectWorkspaceRecord.create(
                videoCreationAssistance = videoCreation,
                assets = suppliedAssets,
                suppliedShotStateDescription =
                    "  Previous shot ends with the character beside the doorway facing camera-left.  ",
                suppliedEpisodeContinuityDescription =
                    "  Preserve the supplied recurring character, location, and prop continuity.  ",
                workspaceObjective =
                    "  Prepare bounded Creative Project Workspace continuity.  ",
            )

        assertSame(
            videoCreation,
            workspace.videoCreationAssistance,
        )

        assertEquals(
            listOf(0, 1, 2),
            workspace.assets.map { it.position },
        )

        assertEquals(
            CreativeWorkspaceAssetType.CHARACTER,
            workspace.assets[0].type,
        )

        assertEquals(
            "Recurring Devil Character",
            workspace.assets[0].name,
        )

        assertEquals(
            "Same horns, clothing, proportions, and cartoon appearance.",
            workspace.assets[0].suppliedContinuityDescription,
        )

        assertEquals(
            "Previous shot ends with the character beside the doorway facing camera-left.",
            workspace.suppliedShotStateDescription,
        )

        assertEquals(
            "Preserve the supplied recurring character, location, and prop continuity.",
            workspace.suppliedEpisodeContinuityDescription,
        )

        assertEquals(
            "Prepare bounded Creative Project Workspace continuity.",
            workspace.workspaceObjective,
        )

        assertNotSame(
            suppliedAssets,
            workspace.assets,
        )
    }

    @Test
    fun `asset record rejects negative position or blank supplied metadata`() {
        assertFailsWith<IllegalArgumentException> {
            CreativeWorkspaceAssetRecord.create(
                position = -1,
                type = CreativeWorkspaceAssetType.CHARACTER,
                name = "Character",
                suppliedContinuityDescription =
                    "Supplied character continuity.",
            )
        }

        assertFailsWith<IllegalArgumentException> {
            CreativeWorkspaceAssetRecord.create(
                position = 0,
                type = CreativeWorkspaceAssetType.LOCATION,
                name = "   ",
                suppliedContinuityDescription =
                    "Supplied location continuity.",
            )
        }

        assertFailsWith<IllegalArgumentException> {
            CreativeWorkspaceAssetRecord.create(
                position = 0,
                type = CreativeWorkspaceAssetType.PROP,
                name = "Recurring prop",
                suppliedContinuityDescription = "   ",
            )
        }
    }

    @Test
    fun `workspace rejects noncontiguous asset positions`() {
        val videoCreation =
            stage173VideoCreation()

        assertFailsWith<IllegalArgumentException> {
            CreativeProjectWorkspaceRecord.create(
                videoCreationAssistance = videoCreation,
                assets =
                    listOf(
                        CreativeWorkspaceAssetRecord.create(
                            position = 0,
                            type = CreativeWorkspaceAssetType.CHARACTER,
                            name = "Character",
                            suppliedContinuityDescription =
                                "Supplied character continuity.",
                        ),
                        CreativeWorkspaceAssetRecord.create(
                            position = 2,
                            type = CreativeWorkspaceAssetType.LOCATION,
                            name = "Location",
                            suppliedContinuityDescription =
                                "Supplied location continuity.",
                        ),
                    ),
                suppliedShotStateDescription =
                    "Supplied shot state.",
                suppliedEpisodeContinuityDescription =
                    "Supplied episode continuity.",
                workspaceObjective =
                    "Prepare workspace.",
            )
        }
    }

    @Test
    fun `workspace permits an empty supplied asset collection`() {
        val videoCreation =
            stage173VideoCreation()

        val workspace =
            CreativeProjectWorkspaceRecord.create(
                videoCreationAssistance = videoCreation,
                assets = emptyList(),
                suppliedShotStateDescription =
                    "Supplied shot state.",
                suppliedEpisodeContinuityDescription =
                    "Supplied episode continuity.",
                workspaceObjective =
                    "Prepare continuity before assets are supplied.",
            )

        assertEquals(
            emptyList(),
            workspace.assets,
        )
    }

    @Test
    fun `workspace rejects blank shot episode or objective metadata`() {
        val videoCreation =
            stage173VideoCreation()

        assertFailsWith<IllegalArgumentException> {
            CreativeProjectWorkspaceRecord.create(
                videoCreationAssistance = videoCreation,
                assets = emptyList(),
                suppliedShotStateDescription = "   ",
                suppliedEpisodeContinuityDescription =
                    "Supplied episode continuity.",
                workspaceObjective =
                    "Prepare workspace.",
            )
        }

        assertFailsWith<IllegalArgumentException> {
            CreativeProjectWorkspaceRecord.create(
                videoCreationAssistance = videoCreation,
                assets = emptyList(),
                suppliedShotStateDescription =
                    "Supplied shot state.",
                suppliedEpisodeContinuityDescription = "   ",
                workspaceObjective =
                    "Prepare workspace.",
            )
        }

        assertFailsWith<IllegalArgumentException> {
            CreativeProjectWorkspaceRecord.create(
                videoCreationAssistance = videoCreation,
                assets = emptyList(),
                suppliedShotStateDescription =
                    "Supplied shot state.",
                suppliedEpisodeContinuityDescription =
                    "Supplied episode continuity.",
                workspaceObjective = "   ",
            )
        }
    }

    private fun stage173VideoCreation(): VideoCreationAssistanceRecord {
        val story =
            "Shared supplied Stage 174 story."

        val project =
            CreativeMediaProjectRecord.create(
                projectId =
                    CreativeMediaProjectId.from(
                        "stage174-project",
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

        val pipeline =
            StoryToAnimationPipelineRecord.create(
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

        val audioMusic =
            AudioMusicCreativeAssistanceRecord.create(
                storyToAnimationPipeline = pipeline,
                audioMusicFocus =
                    "Narration and music",
                suppliedAudioMusicContextDescription =
                    "Supplied Stage 172 audio/music context.",
                audioMusicObjective =
                    "Prepare audio/music requirements.",
            )

        return VideoCreationAssistanceRecord.create(
            storyToAnimationPipeline = pipeline,
            audioMusicAssistance = audioMusic,
            generationMode =
                VideoGenerationMode.IMAGE_TO_VIDEO,
            suppliedPrompt =
                "Animate the supplied recurring character.",
            suppliedSourceContextDescription =
                "Supplied reference-image context.",
            motionCameraDirection =
                "Track the character smoothly.",
            requestedOutputFormat =
                "MP4",
            videoCreationObjective =
                "Prepare image-to-video requirements.",
        )
    }
}
