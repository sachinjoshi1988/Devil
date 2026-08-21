package com.devil.core.model.creative

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class StoryCreationStage169Test {

    @Test
    fun `record preserves exact Stage 166 integration and normalized story creation metadata`() {
        val integration = stage166Integration()

        val record =
            StoryCreationRecord.create(
                creativeMediaIntegration = integration,
                storyCreationFocus = "  Character-driven story development  ",
                suppliedStoryCreationContextDescription =
                    "  User supplied a bounded story creation context.  ",
                storyCreationObjective =
                    "  Prepare a provider-neutral story creation context.  ",
            )

        assertSame(
            integration,
            record.creativeMediaIntegration,
        )
        assertEquals(
            "Character-driven story development",
            record.storyCreationFocus,
        )
        assertEquals(
            "User supplied a bounded story creation context.",
            record.suppliedStoryCreationContextDescription,
        )
        assertEquals(
            "Prepare a provider-neutral story creation context.",
            record.storyCreationObjective,
        )
    }

    @Test
    fun `record rejects blank story creation focus`() {
        assertFailsWith<IllegalArgumentException> {
            StoryCreationRecord.create(
                creativeMediaIntegration = stage166Integration(),
                storyCreationFocus = "   ",
                suppliedStoryCreationContextDescription =
                    "User supplied story context.",
                storyCreationObjective =
                    "Prepare bounded Story Creation.",
            )
        }
    }

    @Test
    fun `record rejects blank story creation context description`() {
        assertFailsWith<IllegalArgumentException> {
            StoryCreationRecord.create(
                creativeMediaIntegration = stage166Integration(),
                storyCreationFocus = "Story development",
                suppliedStoryCreationContextDescription = "   ",
                storyCreationObjective =
                    "Prepare bounded Story Creation.",
            )
        }
    }

    @Test
    fun `record rejects blank story creation objective`() {
        assertFailsWith<IllegalArgumentException> {
            StoryCreationRecord.create(
                creativeMediaIntegration = stage166Integration(),
                storyCreationFocus = "Story development",
                suppliedStoryCreationContextDescription =
                    "User supplied story context.",
                storyCreationObjective = "   ",
            )
        }
    }

    private fun stage166Integration(): CreativeMediaIntegrationRecord {
        return CreativeMediaIntegrationRecord.create(
            creativeProject =
                CreativeMediaProjectRecord.create(
                    projectId =
                        CreativeMediaProjectId.from(
                            "stage169-project",
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
                ),
            integrationFocus =
                "Creative narrative integration",
            suppliedCreativeMediaContextDescription =
                "Creative Media context for Stage 169.",
            integrationObjective =
                "Prepare Creative Media integration.",
        )
    }
}
