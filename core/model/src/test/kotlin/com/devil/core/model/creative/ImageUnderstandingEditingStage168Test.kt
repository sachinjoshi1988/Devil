package com.devil.core.model.creative

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class ImageUnderstandingEditingStage168Test {

    @Test
    fun `record preserves exact Stage 167 image creation and normalized editing metadata`() {
        val imageCreation = imageCreation()

        val record =
            ImageUnderstandingEditingRecord.create(
                imageCreation = imageCreation,
                imageUnderstandingEditingFocus =
                    "  Reference-image editing context  ",
                suppliedReferenceImageContextDescription =
                    "  User supplied a reference-image and consistency requirement.  ",
                editingObjective =
                    "  Preserve bounded editing intent without claiming image understanding.  ",
            )

        assertSame(
            imageCreation,
            record.imageCreation,
        )
        assertEquals(
            "Reference-image editing context",
            record.imageUnderstandingEditingFocus,
        )
        assertEquals(
            "User supplied a reference-image and consistency requirement.",
            record.suppliedReferenceImageContextDescription,
        )
        assertEquals(
            "Preserve bounded editing intent without claiming image understanding.",
            record.editingObjective,
        )
    }

    @Test
    fun `record rejects blank image understanding editing focus`() {
        assertFailsWith<IllegalArgumentException> {
            ImageUnderstandingEditingRecord.create(
                imageCreation = imageCreation(),
                imageUnderstandingEditingFocus = "   ",
                suppliedReferenceImageContextDescription =
                    "Reference-image context.",
                editingObjective =
                    "Preserve bounded editing intent.",
            )
        }
    }

    @Test
    fun `record rejects blank reference image context description`() {
        assertFailsWith<IllegalArgumentException> {
            ImageUnderstandingEditingRecord.create(
                imageCreation = imageCreation(),
                imageUnderstandingEditingFocus =
                    "Reference-image editing context",
                suppliedReferenceImageContextDescription = "   ",
                editingObjective =
                    "Preserve bounded editing intent.",
            )
        }
    }

    @Test
    fun `record rejects blank editing objective`() {
        assertFailsWith<IllegalArgumentException> {
            ImageUnderstandingEditingRecord.create(
                imageCreation = imageCreation(),
                imageUnderstandingEditingFocus =
                    "Reference-image editing context",
                suppliedReferenceImageContextDescription =
                    "Reference-image context.",
                editingObjective = "   ",
            )
        }
    }

    private fun imageCreation(): ImageCreationRecord {
        val project =
            CreativeMediaProjectRecord.create(
                projectId =
                    CreativeMediaProjectId.from(
                        "creative-project:stage168",
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

        val integration =
            CreativeMediaIntegrationRecord.create(
                creativeProject = project,
                integrationFocus =
                    "Creative Media integration",
                suppliedCreativeMediaContextDescription =
                    "Existing Creative Media foundation.",
                integrationObjective =
                    "Preserve bounded provider-neutral integration.",
            )

        return ImageCreationRecord.create(
            creativeMediaIntegration = integration,
            imageCreationFocus =
                "Image creation context",
            suppliedImageCreationDescription =
                "Image creation description.",
            imageCreationObjective =
                "Preserve bounded image creation preparation.",
        )
    }
}
