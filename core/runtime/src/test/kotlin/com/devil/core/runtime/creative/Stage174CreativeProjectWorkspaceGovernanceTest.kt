package com.devil.core.runtime.creative

import com.devil.core.model.common.TraceId
import com.devil.core.model.creative.AnimationSceneRecord
import com.devil.core.model.creative.AudioMusicCreativeAssistanceRecord
import com.devil.core.model.creative.CreativeMediaIntegrationRecord
import com.devil.core.model.creative.CreativeMediaMedium
import com.devil.core.model.creative.CreativeMediaObjective
import com.devil.core.model.creative.CreativeMediaProjectId
import com.devil.core.model.creative.CreativeMediaProjectRecord
import com.devil.core.model.creative.CreativeWorkspaceAssetType
import com.devil.core.model.creative.StoryCreationRecord
import com.devil.core.model.creative.StorySource
import com.devil.core.model.creative.StoryToAnimationPipelineRecord
import com.devil.core.model.creative.StoryToAnimationPipelineStepRecord
import com.devil.core.model.creative.StoryToAnimationRecord
import com.devil.core.model.creative.StoryToStoryboardRecord
import com.devil.core.model.creative.StoryboardSceneRecord
import com.devil.core.model.creative.VideoCreationAssistanceRecord
import com.devil.core.model.creative.VideoGenerationMode
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage174CreativeProjectWorkspaceGovernanceTest {

    @Test
    fun `coordinator prepares ordered recurring creative workspace continuity`() {
        val videoCreation =
            stage173VideoCreation()

        val result =
            CreativeProjectWorkspaceCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace:stage174-workspace",
                    ),
                videoCreationAssistance =
                    videoCreation,
                suppliedAssets =
                    listOf(
                        SuppliedCreativeWorkspaceAsset(
                            type =
                                CreativeWorkspaceAssetType.CHARACTER,
                            name =
                                "Recurring Devil Character",
                            suppliedContinuityDescription =
                                "Preserve the supplied horns, clothing, proportions, and cartoon appearance.",
                        ),
                        SuppliedCreativeWorkspaceAsset(
                            type =
                                CreativeWorkspaceAssetType.LOCATION,
                            name =
                                "YouTube City Street",
                            suppliedContinuityDescription =
                                "Preserve the supplied recurring street and landmark layout.",
                        ),
                        SuppliedCreativeWorkspaceAsset(
                            type =
                                CreativeWorkspaceAssetType.PROP,
                            name =
                                "Golden Microphone",
                            suppliedContinuityDescription =
                                "Preserve the supplied microphone design across shots.",
                        ),
                    ),
                suppliedShotStateDescription =
                    "Previous shot ends with the character beside the doorway facing camera-left.",
                suppliedEpisodeContinuityDescription =
                    "Continue the supplied recurring character, location, and prop context.",
                workspaceObjective =
                    "Prepare bounded creative-project continuity.",
            )

        assertEquals(
            CreativeProjectWorkspacePreparationStatus.PREPARED,
            result.status,
        )

        val workspace =
            requireNotNull(result.workspace)

        assertSame(
            videoCreation,
            workspace.videoCreationAssistance,
        )

        assertEquals(
            listOf(0, 1, 2),
            workspace.assets.map { it.position },
        )

        assertEquals(
            listOf(
                CreativeWorkspaceAssetType.CHARACTER,
                CreativeWorkspaceAssetType.LOCATION,
                CreativeWorkspaceAssetType.PROP,
            ),
            workspace.assets.map { it.type },
        )

        assertEquals(
            "Previous shot ends with the character beside the doorway facing camera-left.",
            workspace.suppliedShotStateDescription,
        )
    }

    @Test
    fun `coordinator can prepare workspace before recurring assets are supplied`() {
        val result =
            CreativeProjectWorkspaceCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace:stage174-empty-assets",
                    ),
                videoCreationAssistance =
                    stage173VideoCreation(),
                suppliedAssets =
                    emptyList(),
                suppliedShotStateDescription =
                    "Initial supplied shot state.",
                suppliedEpisodeContinuityDescription =
                    "Initial supplied episode continuity.",
                workspaceObjective =
                    "Prepare workspace foundation.",
            )

        assertEquals(
            CreativeProjectWorkspacePreparationStatus.PREPARED,
            result.status,
        )

        assertEquals(
            emptyList(),
            requireNotNull(result.workspace).assets,
        )
    }

    @Test
    fun `blank supplied workspace metadata defers without manufacturing state`() {
        val result =
            CreativeProjectWorkspaceCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace:stage174-deferred",
                    ),
                videoCreationAssistance =
                    stage173VideoCreation(),
                suppliedAssets =
                    listOf(
                        SuppliedCreativeWorkspaceAsset(
                            type =
                                CreativeWorkspaceAssetType.CHARACTER,
                            name =
                                "   ",
                            suppliedContinuityDescription =
                                "Supplied character continuity.",
                        ),
                    ),
                suppliedShotStateDescription =
                    "Supplied shot state.",
                suppliedEpisodeContinuityDescription =
                    "Supplied episode continuity.",
                workspaceObjective =
                    "Prepare workspace.",
            )

        assertEquals(
            CreativeProjectWorkspacePreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(result.workspace)
    }

    @Test
    fun `prepared result requires one workspace`() {
        assertFailsWith<IllegalArgumentException> {
            CreativeProjectWorkspacePreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace:stage174-invalid-prepared",
                    ),
                status =
                    CreativeProjectWorkspacePreparationStatus.PREPARED,
            )
        }
    }

    @Test
    fun `deferred result rejects a workspace`() {
        val prepared =
            CreativeProjectWorkspaceCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace:stage174-source",
                    ),
                videoCreationAssistance =
                    stage173VideoCreation(),
                suppliedAssets =
                    emptyList(),
                suppliedShotStateDescription =
                    "Supplied shot state.",
                suppliedEpisodeContinuityDescription =
                    "Supplied episode continuity.",
                workspaceObjective =
                    "Prepare workspace.",
            )

        val workspace =
            requireNotNull(prepared.workspace)

        assertFailsWith<IllegalArgumentException> {
            CreativeProjectWorkspacePreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace:stage174-invalid-deferred",
                    ),
                status =
                    CreativeProjectWorkspacePreparationStatus.DEFERRED,
                workspace = workspace,
            )
        }
    }

    private fun stage173VideoCreation(): VideoCreationAssistanceRecord {
        val story =
            "Shared supplied Stage 174 runtime story."

        val project =
            CreativeMediaProjectRecord.create(
                projectId =
                    CreativeMediaProjectId.from(
                        "stage174-runtime-project",
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
                    "Creative Media runtime context.",
                integrationObjective =
                    "Prepare Creative Media integration.",
            )

        val storyCreation =
            StoryCreationRecord.create(
                creativeMediaIntegration = integration,
                storyCreationFocus =
                    "Narrative preparation",
                suppliedStoryCreationContextDescription =
                    "Supplied Story Creation runtime context.",
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
                    "Supplied Stage 172 runtime audio/music context.",
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
                "Supplied Stage 173 reference-image context.",
            motionCameraDirection =
                "Track the character smoothly.",
            requestedOutputFormat =
                "MP4",
            videoCreationObjective =
                "Prepare image-to-video requirements.",
        )
    }
}
