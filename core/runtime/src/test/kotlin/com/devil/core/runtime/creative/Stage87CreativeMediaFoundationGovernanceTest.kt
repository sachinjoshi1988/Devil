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

class Stage87CreativeMediaFoundationGovernanceTest {

    @Test
    fun `bounded creative media project may be prepared without generating media`() {
        val traceId =
            TraceId.from(
                "trace-stage87-creative-001",
            )

        val projectId =
            CreativeMediaProjectId.from(
                "creative-project:001",
            )

        val result =
            CreativeMediaProjectCoordinator().prepare(
                traceId = traceId,
                projectId = projectId,
                medium = "image",
                objective =
                    "Create a bounded visual concept for later governed production.",
            )

        assertEquals(
            traceId,
            result.traceId,
        )

        assertEquals(
            CreativeMediaProjectPreparationStatus.PREPARED,
            result.status,
        )

        val project =
            requireNotNull(result.project)

        assertSame(
            projectId,
            project.projectId,
        )

        assertEquals(
            "image",
            project.objective.medium.value,
        )

        assertEquals(
            "Create a bounded visual concept for later governed production.",
            project.objective.objective,
        )
    }

    @Test
    fun `creative media project identity is normalized and required`() {
        assertEquals(
            "creative-project:001",
            CreativeMediaProjectId.from(
                "  creative-project:001  ",
            ).value,
        )

        assertFailsWith<IllegalArgumentException> {
            CreativeMediaProjectId.from("   ")
        }
    }

    @Test
    fun `creative media medium is extensible normalized descriptive metadata`() {
        assertEquals(
            "future-creative-medium",
            CreativeMediaMedium.from(
                "  future-creative-medium  ",
            ).value,
        )
    }

    @Test
    fun `creative media medium rejects blank value`() {
        assertFailsWith<IllegalArgumentException> {
            CreativeMediaMedium.from("   ")
        }
    }

    @Test
    fun `creative media objective normalizes supplied objective`() {
        val medium =
            CreativeMediaMedium.from(
                "audio",
            )

        val objective =
            CreativeMediaObjective.create(
                medium = medium,
                objective =
                    "  Prepare a musical concept for later governed production.  ",
            )

        assertSame(
            medium,
            objective.medium,
        )

        assertEquals(
            "Prepare a musical concept for later governed production.",
            objective.objective,
        )
    }

    @Test
    fun `creative media objective rejects blank objective`() {
        assertFailsWith<IllegalArgumentException> {
            CreativeMediaObjective.create(
                medium =
                    CreativeMediaMedium.from(
                        "text",
                    ),
                objective = "   ",
            )
        }
    }

    @Test
    fun `blank medium remains deferred`() {
        val result =
            CreativeMediaProjectCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage87-creative-002",
                    ),
                projectId =
                    CreativeMediaProjectId.from(
                        "creative-project:002",
                    ),
                medium = "   ",
                objective =
                    "Prepare bounded creative content.",
            )

        assertEquals(
            CreativeMediaProjectPreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(result.project)
    }

    @Test
    fun `blank creative objective remains deferred`() {
        val result =
            CreativeMediaProjectCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage87-creative-003",
                    ),
                projectId =
                    CreativeMediaProjectId.from(
                        "creative-project:003",
                    ),
                medium = "video",
                objective = "   ",
            )

        assertEquals(
            CreativeMediaProjectPreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(result.project)
    }

    @Test
    fun `creative project record preserves supplied project and objective only`() {
        val projectId =
            CreativeMediaProjectId.from(
                "creative-project:004",
            )

        val objective =
            CreativeMediaObjective.create(
                medium =
                    CreativeMediaMedium.from(
                        "image",
                    ),
                objective =
                    "Prepare a bounded visual design objective.",
            )

        val record =
            CreativeMediaProjectRecord.create(
                projectId = projectId,
                objective = objective,
            )

        assertSame(
            projectId,
            record.projectId,
        )

        assertSame(
            objective,
            record.objective,
        )
    }

    @Test
    fun `prepared result requires one creative media project`() {
        assertFailsWith<IllegalArgumentException> {
            CreativeMediaProjectPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage87-result-001",
                    ),
                status =
                    CreativeMediaProjectPreparationStatus.PREPARED,
            )
        }
    }

    @Test
    fun `deferred result cannot smuggle creative media project`() {
        val project =
            CreativeMediaProjectRecord.create(
                projectId =
                    CreativeMediaProjectId.from(
                        "creative-project:005",
                    ),
                objective =
                    CreativeMediaObjective.create(
                        medium =
                            CreativeMediaMedium.from(
                                "audio",
                            ),
                        objective =
                            "Prepare bounded creative audio.",
                    ),
            )

        assertFailsWith<IllegalArgumentException> {
            CreativeMediaProjectPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage87-result-002",
                    ),
                status =
                    CreativeMediaProjectPreparationStatus.DEFERRED,
                project = project,
            )
        }
    }

    @Test
    fun `deferred result contains no creative project`() {
        val result =
            CreativeMediaProjectPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage87-result-003",
                    ),
                status =
                    CreativeMediaProjectPreparationStatus.DEFERRED,
            )

        assertNull(result.project)
    }
}
