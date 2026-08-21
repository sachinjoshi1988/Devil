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

class Stage169StoryCreationGovernanceTest {

    private val coordinator =
        StoryCreationCoordinator()

    @Test
    fun `prepare preserves exact Stage 166 provenance and prepares bounded Story Creation`() {
        val traceId =
            TraceId.from("stage169-prepared")

        val integration =
            stage166Integration()

        val result =
            coordinator.prepare(
                traceId = traceId,
                creativeMediaIntegration = integration,
                storyCreationFocus =
                    "  Character and narrative development  ",
                suppliedStoryCreationContextDescription =
                    "  User supplied a bounded story context.  ",
                storyCreationObjective =
                    "  Prepare Story Creation without generating story output.  ",
            )

        assertEquals(
            StoryCreationPreparationStatus.PREPARED,
            result.status,
        )
        assertEquals(
            traceId,
            result.traceId,
        )

        val storyCreation =
            requireNotNull(result.storyCreation)

        assertSame(
            integration,
            storyCreation.creativeMediaIntegration,
        )
        assertEquals(
            "Character and narrative development",
            storyCreation.storyCreationFocus,
        )
        assertEquals(
            "User supplied a bounded story context.",
            storyCreation.suppliedStoryCreationContextDescription,
        )
        assertEquals(
            "Prepare Story Creation without generating story output.",
            storyCreation.storyCreationObjective,
        )
    }

    @Test
    fun `prepare defers when story creation focus is blank`() {
        val result =
            coordinator.prepare(
                traceId = TraceId.from("stage169-blank-focus"),
                creativeMediaIntegration = stage166Integration(),
                storyCreationFocus = "   ",
                suppliedStoryCreationContextDescription =
                    "User supplied story context.",
                storyCreationObjective =
                    "Prepare bounded Story Creation.",
            )

        assertEquals(
            StoryCreationPreparationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.storyCreation)
    }

    @Test
    fun `prepare defers when story creation context is blank`() {
        val result =
            coordinator.prepare(
                traceId = TraceId.from("stage169-blank-context"),
                creativeMediaIntegration = stage166Integration(),
                storyCreationFocus = "Story development",
                suppliedStoryCreationContextDescription = "   ",
                storyCreationObjective =
                    "Prepare bounded Story Creation.",
            )

        assertEquals(
            StoryCreationPreparationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.storyCreation)
    }

    @Test
    fun `prepare defers when story creation objective is blank`() {
        val result =
            coordinator.prepare(
                traceId = TraceId.from("stage169-blank-objective"),
                creativeMediaIntegration = stage166Integration(),
                storyCreationFocus = "Story development",
                suppliedStoryCreationContextDescription =
                    "User supplied story context.",
                storyCreationObjective = "   ",
            )

        assertEquals(
            StoryCreationPreparationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.storyCreation)
    }

    @Test
    fun `prepared result requires one Story Creation context`() {
        assertFailsWith<IllegalArgumentException> {
            StoryCreationPreparationResult.create(
                traceId =
                    TraceId.from(
                        "stage169-invalid-prepared",
                    ),
                status =
                    StoryCreationPreparationStatus.PREPARED,
            )
        }
    }

    @Test
    fun `deferred result rejects Story Creation context`() {
        val prepared =
            coordinator.prepare(
                traceId =
                    TraceId.from(
                        "stage169-source",
                    ),
                creativeMediaIntegration =
                    stage166Integration(),
                storyCreationFocus =
                    "Story development",
                suppliedStoryCreationContextDescription =
                    "User supplied story context.",
                storyCreationObjective =
                    "Prepare bounded Story Creation.",
            )

        val storyCreation =
            requireNotNull(prepared.storyCreation)

        assertFailsWith<IllegalArgumentException> {
            StoryCreationPreparationResult.create(
                traceId =
                    TraceId.from(
                        "stage169-invalid-deferred",
                    ),
                status =
                    StoryCreationPreparationStatus.DEFERRED,
                storyCreation = storyCreation,
            )
        }
    }

    private fun stage166Integration(): CreativeMediaIntegrationRecord {
        return CreativeMediaIntegrationRecord.create(
            creativeProject =
                CreativeMediaProjectRecord.create(
                    projectId =
                        CreativeMediaProjectId.from(
                            "stage169-runtime-project",
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
