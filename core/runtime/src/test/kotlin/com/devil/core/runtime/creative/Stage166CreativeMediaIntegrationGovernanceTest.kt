package com.devil.core.runtime.creative

import com.devil.core.model.common.TraceId
import com.devil.core.model.creative.CreativeMediaMedium
import com.devil.core.model.creative.CreativeMediaObjective
import com.devil.core.model.creative.CreativeMediaProjectId
import com.devil.core.model.creative.CreativeMediaProjectRecord
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage166CreativeMediaIntegrationGovernanceTest {

    @Test
    fun `coordinator prepares bounded Creative Media integration context`() {
        val project = creativeProject()
        val traceId =
            TraceId.from("trace:stage166-prepared")

        val result =
            CreativeMediaIntegrationCoordinator().prepare(
                traceId = traceId,
                creativeProject = project,
                integrationFocus =
                    "Provider-neutral Creative Media integration",
                suppliedCreativeMediaContextDescription =
                    "Existing Creative Media foundation is supplied for integration.",
                integrationObjective =
                    "Preserve one Devil intelligence and bounded Creative Media integration.",
            )

        assertEquals(
            CreativeMediaIntegrationPreparationStatus.PREPARED,
            result.status,
        )
        assertEquals(
            traceId,
            result.traceId,
        )

        val integration =
            requireNotNull(result.integration)

        assertSame(
            project,
            integration.creativeProject,
        )
        assertEquals(
            "Provider-neutral Creative Media integration",
            integration.integrationFocus,
        )
        assertEquals(
            "Existing Creative Media foundation is supplied for integration.",
            integration.suppliedCreativeMediaContextDescription,
        )
        assertEquals(
            "Preserve one Devil intelligence and bounded Creative Media integration.",
            integration.integrationObjective,
        )
    }

    @Test
    fun `blank integration focus defers`() {
        val result =
            CreativeMediaIntegrationCoordinator().prepare(
                traceId = TraceId.from("trace:stage166-focus"),
                creativeProject = creativeProject(),
                integrationFocus = "   ",
                suppliedCreativeMediaContextDescription =
                    "Existing Creative Media foundation.",
                integrationObjective =
                    "Preserve bounded integration.",
            )

        assertEquals(
            CreativeMediaIntegrationPreparationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.integration)
    }

    @Test
    fun `blank creative media context description defers`() {
        val result =
            CreativeMediaIntegrationCoordinator().prepare(
                traceId = TraceId.from("trace:stage166-description"),
                creativeProject = creativeProject(),
                integrationFocus =
                    "Creative Media integration",
                suppliedCreativeMediaContextDescription = "   ",
                integrationObjective =
                    "Preserve bounded integration.",
            )

        assertEquals(
            CreativeMediaIntegrationPreparationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.integration)
    }

    @Test
    fun `blank integration objective defers`() {
        val result =
            CreativeMediaIntegrationCoordinator().prepare(
                traceId = TraceId.from("trace:stage166-objective"),
                creativeProject = creativeProject(),
                integrationFocus =
                    "Creative Media integration",
                suppliedCreativeMediaContextDescription =
                    "Existing Creative Media foundation.",
                integrationObjective = "   ",
            )

        assertEquals(
            CreativeMediaIntegrationPreparationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.integration)
    }

    @Test
    fun `prepared result requires integration context`() {
        assertFailsWith<IllegalArgumentException> {
            CreativeMediaIntegrationPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace:stage166-invalid-prepared",
                    ),
                status =
                    CreativeMediaIntegrationPreparationStatus.PREPARED,
            )
        }
    }

    @Test
    fun `deferred result cannot contain integration context`() {
        val prepared =
            CreativeMediaIntegrationCoordinator().prepare(
                traceId = TraceId.from("trace:stage166-source"),
                creativeProject = creativeProject(),
                integrationFocus =
                    "Creative Media integration",
                suppliedCreativeMediaContextDescription =
                    "Existing Creative Media foundation.",
                integrationObjective =
                    "Preserve bounded integration.",
            )

        val integration =
            requireNotNull(prepared.integration)

        assertFailsWith<IllegalArgumentException> {
            CreativeMediaIntegrationPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace:stage166-invalid-deferred",
                    ),
                status =
                    CreativeMediaIntegrationPreparationStatus.DEFERRED,
                integration = integration,
            )
        }
    }

    private fun creativeProject(): CreativeMediaProjectRecord {
        return CreativeMediaProjectRecord.create(
            projectId =
                CreativeMediaProjectId.from(
                    "creative-project:stage166",
                ),
            objective =
                CreativeMediaObjective.create(
                    medium =
                        CreativeMediaMedium.from(
                            "multimodal",
                        ),
                    objective =
                        "Preserve bounded Creative Media project context.",
                ),
        )
    }
}
