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
import com.devil.core.model.creative.StoryToAnimationRecord
import com.devil.core.model.creative.StoryToStoryboardRecord
import com.devil.core.model.creative.StoryboardSceneRecord
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage171StoryToAnimationPipelineGovernanceTest {

    private val coordinator =
        StoryToAnimationPipelineCoordinator()

    @Test
    fun `coordinator prepares bounded production pipeline`() {
        val storyToAnimation =
            stage88StoryToAnimation(
                "Shared supplied story.",
            )
        val storyboard =
            stage170Storyboard(
                "Shared supplied story.",
            )
        val traceId =
            TraceId.from(
                "trace:stage171-prepared",
            )

        val result =
            coordinator.prepare(
                traceId = traceId,
                storyToAnimation = storyToAnimation,
                storyboard = storyboard,
                productionStepDescriptions =
                    listOf(
                        "Prepare shot planning context.",
                        "Prepare approved asset requirements.",
                        "Prepare future generation context.",
                        "Prepare planned observation.",
                        "Prepare planned verification.",
                    ),
                productionObjective =
                    "Preserve bounded Story-to-Animation production structure.",
            )

        assertEquals(
            StoryToAnimationPipelinePreparationStatus.PREPARED,
            result.status,
        )
        assertEquals(traceId, result.traceId)

        val pipeline =
            requireNotNull(result.pipeline)

        assertSame(
            storyToAnimation,
            pipeline.storyToAnimation,
        )
        assertSame(
            storyboard,
            pipeline.storyboard,
        )
        assertEquals(
            listOf(1, 2, 3, 4, 5),
            pipeline.productionSteps.map { it.position },
        )
        assertEquals(
            "Prepare planned observation.",
            pipeline.productionSteps[3].description,
        )
        assertEquals(
            "Prepare planned verification.",
            pipeline.productionSteps[4].description,
        )
    }

    @Test
    fun `empty production steps defer`() {
        val result =
            coordinator.prepare(
                traceId =
                    TraceId.from(
                        "trace:stage171-empty",
                    ),
                storyToAnimation =
                    stage88StoryToAnimation(
                        "Shared supplied story.",
                    ),
                storyboard =
                    stage170Storyboard(
                        "Shared supplied story.",
                    ),
                productionStepDescriptions =
                    emptyList(),
                productionObjective =
                    "Prepare pipeline.",
            )

        assertEquals(
            StoryToAnimationPipelinePreparationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.pipeline)
    }

    @Test
    fun `blank production step defers`() {
        val result =
            coordinator.prepare(
                traceId =
                    TraceId.from(
                        "trace:stage171-step",
                    ),
                storyToAnimation =
                    stage88StoryToAnimation(
                        "Shared supplied story.",
                    ),
                storyboard =
                    stage170Storyboard(
                        "Shared supplied story.",
                    ),
                productionStepDescriptions =
                    listOf(
                        "Prepare shot planning.",
                        "   ",
                    ),
                productionObjective =
                    "Prepare pipeline.",
            )

        assertEquals(
            StoryToAnimationPipelinePreparationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.pipeline)
    }

    @Test
    fun `blank production objective defers`() {
        val result =
            coordinator.prepare(
                traceId =
                    TraceId.from(
                        "trace:stage171-objective",
                    ),
                storyToAnimation =
                    stage88StoryToAnimation(
                        "Shared supplied story.",
                    ),
                storyboard =
                    stage170Storyboard(
                        "Shared supplied story.",
                    ),
                productionStepDescriptions =
                    listOf(
                        "Prepare production context.",
                    ),
                productionObjective = "   ",
            )

        assertEquals(
            StoryToAnimationPipelinePreparationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.pipeline)
    }

    @Test
    fun `mismatched supplied stories are rejected`() {
        assertFailsWith<IllegalArgumentException> {
            coordinator.prepare(
                traceId =
                    TraceId.from(
                        "trace:stage171-mismatch",
                    ),
                storyToAnimation =
                    stage88StoryToAnimation(
                        "Story A.",
                    ),
                storyboard =
                    stage170Storyboard(
                        "Story B.",
                    ),
                productionStepDescriptions =
                    listOf(
                        "Prepare production context.",
                    ),
                productionObjective =
                    "Prepare pipeline.",
            )
        }
    }

    @Test
    fun `prepared result requires pipeline context`() {
        assertFailsWith<IllegalArgumentException> {
            StoryToAnimationPipelinePreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace:stage171-invalid-prepared",
                    ),
                status =
                    StoryToAnimationPipelinePreparationStatus.PREPARED,
            )
        }
    }

    @Test
    fun `deferred result cannot contain pipeline context`() {
        val prepared =
            coordinator.prepare(
                traceId =
                    TraceId.from(
                        "trace:stage171-source",
                    ),
                storyToAnimation =
                    stage88StoryToAnimation(
                        "Shared supplied story.",
                    ),
                storyboard =
                    stage170Storyboard(
                        "Shared supplied story.",
                    ),
                productionStepDescriptions =
                    listOf(
                        "Prepare production context.",
                    ),
                productionObjective =
                    "Prepare pipeline.",
            )

        val pipeline =
            requireNotNull(prepared.pipeline)

        assertFailsWith<IllegalArgumentException> {
            StoryToAnimationPipelinePreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace:stage171-invalid-deferred",
                    ),
                status =
                    StoryToAnimationPipelinePreparationStatus.DEFERRED,
                pipeline = pipeline,
            )
        }
    }

    private fun stage88StoryToAnimation(
        storyContent: String,
    ): StoryToAnimationRecord {
        return StoryToAnimationRecord.create(
            creativeProject = project(),
            story =
                StorySource.from(
                    storyContent,
                ),
            scenes =
                listOf(
                    AnimationSceneRecord.create(
                        position = 1,
                        summary =
                            "Supplied animation-scene intention.",
                    ),
                ),
        )
    }

    private fun stage170Storyboard(
        storyContent: String,
    ): StoryToStoryboardRecord {
        return StoryToStoryboardRecord.create(
            storyCreation =
                StoryCreationRecord.create(
                    creativeMediaIntegration = integration(),
                    storyCreationFocus =
                        "Story development",
                    suppliedStoryCreationContextDescription =
                        "User supplied Story Creation context.",
                    storyCreationObjective =
                        "Prepare bounded Story Creation.",
                ),
            story =
                StorySource.from(
                    storyContent,
                ),
            storyboardScenes =
                listOf(
                    StoryboardSceneRecord.create(
                        position = 1,
                        sceneDescription =
                            "Supplied storyboard scene.",
                    ),
                ),
            storyboardObjective =
                "Prepare bounded storyboard structure.",
        )
    }

    private fun integration(): CreativeMediaIntegrationRecord {
        return CreativeMediaIntegrationRecord.create(
            creativeProject = project(),
            integrationFocus =
                "Creative narrative integration",
            suppliedCreativeMediaContextDescription =
                "Creative Media context for Stage 171.",
            integrationObjective =
                "Preserve bounded Creative Media integration.",
        )
    }

    private fun project(): CreativeMediaProjectRecord {
        return CreativeMediaProjectRecord.create(
            projectId =
                CreativeMediaProjectId.from(
                    "stage171-runtime-project",
                ),
            objective =
                CreativeMediaObjective.create(
                    medium =
                        CreativeMediaMedium.from(
                            "Animated narrative",
                        ),
                    objective =
                        "Prepare bounded animated narrative work.",
                ),
        )
    }
}
