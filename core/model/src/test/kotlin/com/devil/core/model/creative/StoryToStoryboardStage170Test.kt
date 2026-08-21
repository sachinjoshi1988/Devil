package com.devil.core.model.creative

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class StoryToStoryboardStage170Test {

    @Test
    fun `record preserves Stage 169 story source ordered storyboard scenes and objective`() {
        val storyCreation = storyCreation()
        val story =
            StorySource.from(
                "  A traveler enters an abandoned city.  ",
            )
        val scenes =
            listOf(
                StoryboardSceneRecord.create(
                    position = 1,
                    sceneDescription =
                        "  Wide establishing view of the abandoned city.  ",
                ),
                StoryboardSceneRecord.create(
                    position = 2,
                    sceneDescription =
                        "  The traveler approaches the silent central square.  ",
                ),
            )

        val record =
            StoryToStoryboardRecord.create(
                storyCreation = storyCreation,
                story = story,
                storyboardScenes = scenes,
                storyboardObjective =
                    "  Preserve supplied storyboard structure without rendering panels.  ",
            )

        assertSame(
            storyCreation,
            record.storyCreation,
        )
        assertSame(
            story,
            record.story,
        )
        assertEquals(
            listOf(
                "Wide establishing view of the abandoned city.",
                "The traveler approaches the silent central square.",
            ),
            record.storyboardScenes.map { it.sceneDescription },
        )
        assertEquals(
            "Preserve supplied storyboard structure without rendering panels.",
            record.storyboardObjective,
        )
    }

    @Test
    fun `storyboard scene normalizes description and requires positive position`() {
        val scene =
            StoryboardSceneRecord.create(
                position = 1,
                sceneDescription =
                    "  Opening storyboard scene.  ",
            )

        assertEquals(1, scene.position)
        assertEquals(
            "Opening storyboard scene.",
            scene.sceneDescription,
        )

        assertFailsWith<IllegalArgumentException> {
            StoryboardSceneRecord.create(
                position = 0,
                sceneDescription =
                    "Valid description.",
            )
        }
    }

    @Test
    fun `storyboard scene rejects blank description`() {
        assertFailsWith<IllegalArgumentException> {
            StoryboardSceneRecord.create(
                position = 1,
                sceneDescription = "   ",
            )
        }
    }

    @Test
    fun `record rejects empty storyboard scene sequence`() {
        assertFailsWith<IllegalArgumentException> {
            StoryToStoryboardRecord.create(
                storyCreation = storyCreation(),
                story =
                    StorySource.from(
                        "Supplied story.",
                    ),
                storyboardScenes = emptyList(),
                storyboardObjective =
                    "Prepare storyboard context.",
            )
        }
    }

    @Test
    fun `record rejects noncontiguous storyboard positions`() {
        assertFailsWith<IllegalArgumentException> {
            StoryToStoryboardRecord.create(
                storyCreation = storyCreation(),
                story =
                    StorySource.from(
                        "Supplied story.",
                    ),
                storyboardScenes =
                    listOf(
                        StoryboardSceneRecord.create(
                            position = 1,
                            sceneDescription =
                                "First scene.",
                        ),
                        StoryboardSceneRecord.create(
                            position = 3,
                            sceneDescription =
                                "Third-position scene.",
                        ),
                    ),
                storyboardObjective =
                    "Prepare storyboard context.",
            )
        }
    }

    @Test
    fun `record rejects blank storyboard objective`() {
        assertFailsWith<IllegalArgumentException> {
            StoryToStoryboardRecord.create(
                storyCreation = storyCreation(),
                story =
                    StorySource.from(
                        "Supplied story.",
                    ),
                storyboardScenes =
                    listOf(
                        StoryboardSceneRecord.create(
                            position = 1,
                            sceneDescription =
                                "First scene.",
                        ),
                    ),
                storyboardObjective = "   ",
            )
        }
    }

    private fun storyCreation(): StoryCreationRecord {
        val project =
            CreativeMediaProjectRecord.create(
                projectId =
                    CreativeMediaProjectId.from(
                        "stage170-project",
                    ),
                objective =
                    CreativeMediaObjective.create(
                        medium =
                            CreativeMediaMedium.from(
                                "Narrative media",
                            ),
                        objective =
                            "Prepare bounded creative work.",
                    ),
            )

        val integration =
            CreativeMediaIntegrationRecord.create(
                creativeProject = project,
                integrationFocus =
                    "Narrative integration",
                suppliedCreativeMediaContextDescription =
                    "Creative Media context for Stage 170.",
                integrationObjective =
                    "Preserve bounded Creative Media integration.",
            )

        return StoryCreationRecord.create(
            creativeMediaIntegration = integration,
            storyCreationFocus =
                "Story development",
            suppliedStoryCreationContextDescription =
                "User supplied Story Creation context.",
            storyCreationObjective =
                "Prepare bounded Story Creation.",
        )
    }
}
