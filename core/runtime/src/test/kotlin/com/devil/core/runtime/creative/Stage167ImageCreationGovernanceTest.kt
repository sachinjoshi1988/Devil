package com.devil.core.runtime.creative

import com.devil.core.model.common.TraceId
import com.devil.core.model.creative.CreativeMediaIntegrationRecord
import com.devil.core.model.creative.CreativeMediaMedium
import com.devil.core.model.creative.CreativeMediaObjective
import com.devil.core.model.creative.CreativeMediaProjectId
import com.devil.core.model.creative.CreativeMediaProjectRecord
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage167ImageCreationGovernanceTest {

    @Test
    fun `coordinator prepares bounded image creation context`() {
        val integration = creativeMediaIntegration()
        val traceId =
            TraceId.from("trace:stage167-prepared")

        val result =
            ImageCreationCoordinator().prepare(
                traceId = traceId,
                creativeMediaIntegration = integration,
                imageCreationFocus =
                    "Provider-neutral image creation context",
                suppliedImageCreationDescription =
                    "User supplied an image creation description.",
                imageCreationObjective =
                    "Preserve bounded image creation preparation without generation.",
            )

        assertEquals(
            ImageCreationPreparationStatus.PREPARED,
            result.status,
        )
        assertEquals(
            traceId,
            result.traceId,
        )

        val imageCreation =
            requireNotNull(result.imageCreation)

        assertSame(
            integration,
            imageCreation.creativeMediaIntegration,
        )
        assertEquals(
            "Provider-neutral image creation context",
            imageCreation.imageCreationFocus,
        )
        assertEquals(
            "User supplied an image creation description.",
            imageCreation.suppliedImageCreationDescription,
        )
        assertEquals(
            "Preserve bounded image creation preparation without generation.",
            imageCreation.imageCreationObjective,
        )
    }

    @Test
    fun `blank image creation focus defers`() {
        val result =
            ImageCreationCoordinator().prepare(
                traceId = TraceId.from("trace:stage167-focus"),
                creativeMediaIntegration = creativeMediaIntegration(),
                imageCreationFocus = "   ",
                suppliedImageCreationDescription =
                    "User supplied an image creation description.",
                imageCreationObjective =
                    "Preserve bounded image creation preparation.",
            )

        assertEquals(
            ImageCreationPreparationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.imageCreation)
    }

    @Test
    fun `blank image creation description defers`() {
        val result =
            ImageCreationCoordinator().prepare(
                traceId = TraceId.from("trace:stage167-description"),
                creativeMediaIntegration = creativeMediaIntegration(),
                imageCreationFocus =
                    "Image creation context",
                suppliedImageCreationDescription = "   ",
                imageCreationObjective =
                    "Preserve bounded image creation preparation.",
            )

        assertEquals(
            ImageCreationPreparationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.imageCreation)
    }

    @Test
    fun `blank image creation objective defers`() {
        val result =
            ImageCreationCoordinator().prepare(
                traceId = TraceId.from("trace:stage167-objective"),
                creativeMediaIntegration = creativeMediaIntegration(),
                imageCreationFocus =
                    "Image creation context",
                suppliedImageCreationDescription =
                    "User supplied an image creation description.",
                imageCreationObjective = "   ",
            )

        assertEquals(
            ImageCreationPreparationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.imageCreation)
    }

    @Test
    fun `prepared result requires image creation context`() {
        assertFailsWith<IllegalArgumentException> {
            ImageCreationPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace:stage167-invalid-prepared",
                    ),
                status =
                    ImageCreationPreparationStatus.PREPARED,
            )
        }
    }

    @Test
    fun `deferred result cannot contain image creation context`() {
        val prepared =
            ImageCreationCoordinator().prepare(
                traceId = TraceId.from("trace:stage167-source"),
                creativeMediaIntegration = creativeMediaIntegration(),
                imageCreationFocus =
                    "Image creation context",
                suppliedImageCreationDescription =
                    "User supplied an image creation description.",
                imageCreationObjective =
                    "Preserve bounded image creation preparation.",
            )

        val imageCreation =
            requireNotNull(prepared.imageCreation)

        assertFailsWith<IllegalArgumentException> {
            ImageCreationPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace:stage167-invalid-deferred",
                    ),
                status =
                    ImageCreationPreparationStatus.DEFERRED,
                imageCreation = imageCreation,
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
