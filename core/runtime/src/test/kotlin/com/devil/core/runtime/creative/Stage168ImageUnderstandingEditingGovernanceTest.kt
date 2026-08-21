package com.devil.core.runtime.creative

import com.devil.core.model.common.TraceId
import com.devil.core.model.creative.CreativeMediaIntegrationRecord
import com.devil.core.model.creative.CreativeMediaMedium
import com.devil.core.model.creative.CreativeMediaObjective
import com.devil.core.model.creative.CreativeMediaProjectId
import com.devil.core.model.creative.CreativeMediaProjectRecord
import com.devil.core.model.creative.ImageCreationRecord
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage168ImageUnderstandingEditingGovernanceTest {

    @Test
    fun `coordinator prepares bounded image understanding editing context`() {
        val imageCreation = imageCreation()
        val traceId =
            TraceId.from("trace:stage168-prepared")

        val result =
            ImageUnderstandingEditingCoordinator().prepare(
                traceId = traceId,
                imageCreation = imageCreation,
                imageUnderstandingEditingFocus =
                    "Reference-image editing context",
                suppliedReferenceImageContextDescription =
                    "User supplied a reference-image and consistency requirement.",
                editingObjective =
                    "Preserve bounded editing intent without claiming image understanding.",
            )

        assertEquals(
            ImageUnderstandingEditingPreparationStatus.PREPARED,
            result.status,
        )
        assertEquals(
            traceId,
            result.traceId,
        )

        val prepared =
            requireNotNull(result.imageUnderstandingEditing)

        assertSame(
            imageCreation,
            prepared.imageCreation,
        )
        assertEquals(
            "Reference-image editing context",
            prepared.imageUnderstandingEditingFocus,
        )
        assertEquals(
            "User supplied a reference-image and consistency requirement.",
            prepared.suppliedReferenceImageContextDescription,
        )
        assertEquals(
            "Preserve bounded editing intent without claiming image understanding.",
            prepared.editingObjective,
        )
    }

    @Test
    fun `blank image understanding editing focus defers`() {
        val result =
            ImageUnderstandingEditingCoordinator().prepare(
                traceId = TraceId.from("trace:stage168-focus"),
                imageCreation = imageCreation(),
                imageUnderstandingEditingFocus = "   ",
                suppliedReferenceImageContextDescription =
                    "Reference-image context.",
                editingObjective =
                    "Preserve bounded editing intent.",
            )

        assertEquals(
            ImageUnderstandingEditingPreparationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.imageUnderstandingEditing)
    }

    @Test
    fun `blank reference image context description defers`() {
        val result =
            ImageUnderstandingEditingCoordinator().prepare(
                traceId = TraceId.from("trace:stage168-description"),
                imageCreation = imageCreation(),
                imageUnderstandingEditingFocus =
                    "Reference-image editing context",
                suppliedReferenceImageContextDescription = "   ",
                editingObjective =
                    "Preserve bounded editing intent.",
            )

        assertEquals(
            ImageUnderstandingEditingPreparationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.imageUnderstandingEditing)
    }

    @Test
    fun `blank editing objective defers`() {
        val result =
            ImageUnderstandingEditingCoordinator().prepare(
                traceId = TraceId.from("trace:stage168-objective"),
                imageCreation = imageCreation(),
                imageUnderstandingEditingFocus =
                    "Reference-image editing context",
                suppliedReferenceImageContextDescription =
                    "Reference-image context.",
                editingObjective = "   ",
            )

        assertEquals(
            ImageUnderstandingEditingPreparationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.imageUnderstandingEditing)
    }

    @Test
    fun `prepared result requires image understanding editing context`() {
        assertFailsWith<IllegalArgumentException> {
            ImageUnderstandingEditingPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace:stage168-invalid-prepared",
                    ),
                status =
                    ImageUnderstandingEditingPreparationStatus.PREPARED,
            )
        }
    }

    @Test
    fun `deferred result cannot contain image understanding editing context`() {
        val prepared =
            ImageUnderstandingEditingCoordinator().prepare(
                traceId = TraceId.from("trace:stage168-source"),
                imageCreation = imageCreation(),
                imageUnderstandingEditingFocus =
                    "Reference-image editing context",
                suppliedReferenceImageContextDescription =
                    "Reference-image context.",
                editingObjective =
                    "Preserve bounded editing intent.",
            )

        val context =
            requireNotNull(prepared.imageUnderstandingEditing)

        assertFailsWith<IllegalArgumentException> {
            ImageUnderstandingEditingPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace:stage168-invalid-deferred",
                    ),
                status =
                    ImageUnderstandingEditingPreparationStatus.DEFERRED,
                imageUnderstandingEditing = context,
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
