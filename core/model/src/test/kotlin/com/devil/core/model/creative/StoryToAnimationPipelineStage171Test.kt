package com.devil.core.model.creative

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class StoryToAnimationPipelineStage171Test {

    @Test
    fun `pipeline preserves Stage 88 Stage 170 ordered steps and objective`() {
        val storyToAnimation = stage88StoryToAnimation("Shared supplied story.")
        val storyboard = stage170Storyboard("Shared supplied story.")

        val steps =
            listOf(
                StoryToAnimationPipelineStepRecord.create(
                    position = 1,
                    description = "  Prepare shot planning context.  ",
                ),
                StoryToAnimationPipelineStepRecord.create(
                    position = 2,
                    description = "  Prepare approved asset requirements.  ",
                ),
            )

        val record =
            StoryToAnimationPipelineRecord.create(
                storyToAnimation = storyToAnimation,
                storyboard = storyboard,
                productionSteps = steps,
                productionObjective =
                    "  Preserve bounded production-pipeline structure.  ",
            )

        assertSame(storyToAnimation, record.storyToAnimation)
        assertSame(storyboard, record.storyboard)
        assertEquals(
            listOf(
                "Prepare shot planning context.",
                "Prepare approved asset requirements.",
            ),
            record.productionSteps.map { it.description },
        )
        assertEquals(
            "Preserve bounded production-pipeline structure.",
            record.productionObjective,
        )
    }

    @Test
    fun `pipeline step requires positive position and nonblank description`() {
        val step =
            StoryToAnimationPipelineStepRecord.create(
                position = 1,
                description = "  Prepare scene production context.  ",
            )

        assertEquals(1, step.position)
        assertEquals(
            "Prepare scene production context.",
            step.description,
        )

        assertFailsWith<IllegalArgumentException> {
            StoryToAnimationPipelineStepRecord.create(
                position = 0,
                description = "Valid description.",
            )
        }

        assertFailsWith<IllegalArgumentException> {
            StoryToAnimationPipelineStepRecord.create(
                position = 1,
                description = "   ",
            )
        }
    }

    @Test
    fun `pipeline rejects mismatched supplied story provenance`() {
        assertFailsWith<IllegalArgumentException> {
            StoryToAnimationPipelineRecord.create(
                storyToAnimation =
                    stage88StoryToAnimation(
                        "Story A.",
                    ),
                storyboard =
                    stage170Storyboard(
                        "Story B.",
                    ),
                productionSteps =
                    listOf(
                        StoryToAnimationPipelineStepRecord.create(
                            position = 1,
                            description =
                                "Prepare production context.",
                        ),
                    ),
                productionObjective =
                    "Prepare pipeline.",
            )
        }
    }

    @Test
    fun `pipeline rejects empty production steps`() {
        assertFailsWith<IllegalArgumentException> {
            StoryToAnimationPipelineRecord.create(
                storyToAnimation =
                    stage88StoryToAnimation(
                        "Shared supplied story.",
                    ),
                storyboard =
                    stage170Storyboard(
                        "Shared supplied story.",
                    ),
                productionSteps = emptyList(),
                productionObjective =
                    "Prepare pipeline.",
            )
        }
    }

    @Test
    fun `pipeline rejects noncontiguous production step positions`() {
        assertFailsWith<IllegalArgumentException> {
            StoryToAnimationPipelineRecord.create(
                storyToAnimation =
                    stage88StoryToAnimation(
                        "Shared supplied story.",
                    ),
                storyboard =
                    stage170Storyboard(
                        "Shared supplied story.",
                    ),
                productionSteps =
                    listOf(
                        StoryToAnimationPipelineStepRecord.create(
                            position = 1,
                            description = "First step.",
                        ),
                        StoryToAnimationPipelineStepRecord.create(
                            position = 3,
                            description = "Third-position step.",
                        ),
                    ),
                productionObjective =
                    "Prepare pipeline.",
            )
        }
    }

    @Test
    fun `pipeline rejects blank production objective`() {
        assertFailsWith<IllegalArgumentException> {
            StoryToAnimationPipelineRecord.create(
                storyToAnimation =
                    stage88StoryToAnimation(
                        "Shared supplied story.",
                    ),
                storyboard =
                    stage170Storyboard(
                        "Shared supplied story.",
                    ),
                productionSteps =
                    listOf(
                        StoryToAnimationPipelineStepRecord.create(
                            position = 1,
                            description =
                                "Prepare production context.",
                        ),
                    ),
                productionObjective = "   ",
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
            storyCreation = storyCreation(),
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

    private fun storyCreation(): StoryCreationRecord {
        return StoryCreationRecord.create(
            creativeMediaIntegration = integration(),
            storyCreationFocus =
                "Story development",
            suppliedStoryCreationContextDescription =
                "User supplied Story Creation context.",
            storyCreationObjective =
                "Prepare bounded Story Creation.",
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
                    "stage171-project",
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
