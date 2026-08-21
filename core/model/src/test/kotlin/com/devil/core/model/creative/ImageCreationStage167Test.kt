package com.devil.core.model.creative

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class ImageCreationStage167Test {

    @Test
    fun `record preserves exact Stage 166 integration and normalized image creation metadata`() {
        val integration = creativeMediaIntegration()

        val record =
            ImageCreationRecord.create(
                creativeMediaIntegration = integration,
                imageCreationFocus =
                    "  Provider-neutral image creation context  ",
                suppliedImageCreationDescription =
                    "  User supplied an image creation description.  ",
                imageCreationObjective =
                    "  Preserve bounded image creation preparation without generation.  ",
            )

        assertSame(
            integration,
            record.creativeMediaIntegration,
        )
        assertEquals(
            "Provider-neutral image creation context",
            record.imageCreationFocus,
        )
        assertEquals(
            "User supplied an image creation description.",
            record.suppliedImageCreationDescription,
        )
        assertEquals(
            "Preserve bounded image creation preparation without generation.",
            record.imageCreationObjective,
        )
    }

    @Test
    fun `record rejects blank image creation focus`() {
        assertFailsWith<IllegalArgumentException> {
            ImageCreationRecord.create(
                creativeMediaIntegration = creativeMediaIntegration(),
                imageCreationFocus = "   ",
                suppliedImageCreationDescription =
                    "User supplied an image creation description.",
                imageCreationObjective =
                    "Preserve bounded image creation preparation.",
            )
        }
    }

    @Test
    fun `record rejects blank image creation description`() {
        assertFailsWith<IllegalArgumentException> {
            ImageCreationRecord.create(
                creativeMediaIntegration = creativeMediaIntegration(),
                imageCreationFocus =
                    "Image creation context",
                suppliedImageCreationDescription = "   ",
                imageCreationObjective =
                    "Preserve bounded image creation preparation.",
            )
        }
    }

    @Test
    fun `record rejects blank image creation objective`() {
        assertFailsWith<IllegalArgumentException> {
            ImageCreationRecord.create(
                creativeMediaIntegration = creativeMediaIntegration(),
                imageCreationFocus =
                    "Image creation context",
                suppliedImageCreationDescription =
                    "User supplied an image creation description.",
                imageCreationObjective = "   ",
            )
        }
    }

    private fun creativeMediaIntegration(): CreativeMediaIntegrationRecord {
        val project =
            CreativeMediaProjectRecord.create(
                projectId =
                    CreativeMediaProjectId.from(
                        "creative-project:stage167",
                    ),
                objective =
                    CreativeMediaObjective.create(
                        medium =
                            CreativeMediaMedium.from(
                                "image",
                            ),
                        objective =
                            "Preserve bounded Creative Media project context.",
                    ),
            )

        return CreativeMediaIntegrationRecord.create(
            creativeProject = project,
            integrationFocus =
                "Creative Media integration",
            suppliedCreativeMediaContextDescription =
                "Existing Creative Media foundation.",
            integrationObjective =
                "Preserve bounded provider-neutral integration.",
        )
    }
}
