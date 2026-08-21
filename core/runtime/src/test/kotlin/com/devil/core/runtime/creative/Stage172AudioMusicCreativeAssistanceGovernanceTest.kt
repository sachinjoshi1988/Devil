package com.devil.core.runtime.creative

import com.devil.core.model.common.TraceId
import com.devil.core.model.creative.AnimationSceneRecord
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
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage172AudioMusicCreativeAssistanceGovernanceTest {

    @Test
    fun `coordinator prepares bounded audio music assistance context`() {
        val pipeline = stage171Pipeline()
        val traceId =
            TraceId.from(
                "trace:stage172-prepared",
            )

        val result =
            AudioMusicCreativeAssistanceCoordinator().prepare(
                traceId = traceId,
                storyToAnimationPipeline = pipeline,
                audioMusicFocus =
                    "Narration ambience and music direction",
                suppliedAudioMusicContextDescription =
                    "User supplied voice, sound, and music requirements.",
                audioMusicObjective =
                    "Preserve bounded audio and music creative assistance.",
            )

        assertEquals(
            AudioMusicCreativeAssistancePreparationStatus.PREPARED,
            result.status,
        )
        assertEquals(
            traceId,
            result.traceId,
        )

        val assistance =
            requireNotNull(result.assistance)

        assertSame(
            pipeline,
            assistance.storyToAnimationPipeline,
        )
        assertEquals(
            "Narration ambience and music direction",
            assistance.audioMusicFocus,
        )
    }

    @Test
    fun `blank audio music focus defers`() {
        val result =
            AudioMusicCreativeAssistanceCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace:stage172-focus",
                    ),
                storyToAnimationPipeline = stage171Pipeline(),
                audioMusicFocus = "   ",
                suppliedAudioMusicContextDescription =
                    "Supplied audio context.",
                audioMusicObjective =
                    "Prepare bounded assistance.",
            )

        assertEquals(
            AudioMusicCreativeAssistancePreparationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.assistance)
    }

    @Test
    fun `blank audio music context description defers`() {
        val result =
            AudioMusicCreativeAssistanceCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace:stage172-context",
                    ),
                storyToAnimationPipeline = stage171Pipeline(),
                audioMusicFocus =
                    "Narration and music",
                suppliedAudioMusicContextDescription = "   ",
                audioMusicObjective =
                    "Prepare bounded assistance.",
            )

        assertEquals(
            AudioMusicCreativeAssistancePreparationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.assistance)
    }

    @Test
    fun `blank audio music objective defers`() {
        val result =
            AudioMusicCreativeAssistanceCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace:stage172-objective",
                    ),
                storyToAnimationPipeline = stage171Pipeline(),
                audioMusicFocus =
                    "Narration and music",
                suppliedAudioMusicContextDescription =
                    "Supplied audio context.",
                audioMusicObjective = "   ",
            )

        assertEquals(
            AudioMusicCreativeAssistancePreparationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.assistance)
    }

    @Test
    fun `prepared result requires assistance context`() {
        assertFailsWith<IllegalArgumentException> {
            AudioMusicCreativeAssistancePreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace:stage172-invalid-prepared",
                    ),
                status =
                    AudioMusicCreativeAssistancePreparationStatus.PREPARED,
            )
        }
    }

    @Test
    fun `deferred result cannot contain assistance context`() {
        val prepared =
            AudioMusicCreativeAssistanceCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace:stage172-source",
                    ),
                storyToAnimationPipeline = stage171Pipeline(),
                audioMusicFocus =
                    "Narration and music",
                suppliedAudioMusicContextDescription =
                    "Supplied audio context.",
                audioMusicObjective =
                    "Prepare bounded assistance.",
            )

        val assistance =
            requireNotNull(prepared.assistance)

        assertFailsWith<IllegalArgumentException> {
            AudioMusicCreativeAssistancePreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace:stage172-invalid-deferred",
                    ),
                status =
                    AudioMusicCreativeAssistancePreparationStatus.DEFERRED,
                assistance = assistance,
            )
        }
    }

    private fun stage171Pipeline(): StoryToAnimationPipelineRecord {
        val story = "Shared supplied Stage 172 runtime story."

        val project =
            CreativeMediaProjectRecord.create(
                projectId =
                    CreativeMediaProjectId.from(
                        "stage172-runtime-project",
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
