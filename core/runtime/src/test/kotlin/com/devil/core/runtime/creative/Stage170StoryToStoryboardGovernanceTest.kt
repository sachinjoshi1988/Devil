package com.devil.core.runtime.creative

import com.devil.core.model.common.TraceId
import com.devil.core.model.creative.CreativeMediaIntegrationRecord
import com.devil.core.model.creative.CreativeMediaMedium
import com.devil.core.model.creative.CreativeMediaObjective
import com.devil.core.model.creative.CreativeMediaProjectId
import com.devil.core.model.creative.CreativeMediaProjectRecord
import com.devil.core.model.creative.StoryCreationRecord
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage170StoryToStoryboardGovernanceTest {

    private val coordinator =
        StoryToStoryboardCoordinator()

    @Test
    fun `coordinator prepares bounded Story-to-Storyboard context`() {
        val storyCreation = storyCreation()
        val traceId =
            TraceId.from(
                "trace:stage170-prepared",
            )

        val result =
            coordinator.prepare(
                traceId = traceId,
                storyCreation = storyCreation,
                story =
                    "A traveler enters an abandoned city.",
                storyboardSceneDescriptions =
                    listOf(
                        "Wide establishing view of the abandoned city.",
                        "The traveler approaches the silent central square.",
                    ),
                storyboardObjective =
                    "Preserve supplied storyboard structure without rendering panels.",
            )

        assertEquals(
            StoryToStoryboardPreparationStatus.PREPARED,
            result.status,
        )
        assertEquals(traceId, result.traceId)

        val storyboard =
            requireNotNull(result.storyboard)

        assertSame(
            storyCreation,
            storyboard.storyCreation,
        )
        assertEquals(
            "A traveler enters an abandoned city.",
            storyboard.story.content,
        )
        assertEquals(
            listOf(1, 2),
            storyboard.storyboardScenes.map { it.position },
        )
        assertEquals(
            listOf(
                "Wide establishing view of the abandoned city.",
                "The traveler approaches the silent central square.",
            ),
            storyboard.storyboardScenes.map { it.sceneDescription },
        )
        assertEquals(
            "Preserve supplied storyboard structure without rendering panels.",
            storyboard.storyboardObjective,
        )
    }

    @Test
    fun `blank story defers`() {
        val result =
            coordinator.prepare(
                traceId =
                    TraceId.from(
                        "trace:stage170-story",
                    ),
                storyCreation = storyCreation(),
                story = "   ",
                storyboardSceneDescriptions =
                    listOf(
                        "Supplied storyboard scene.",
                    ),
                storyboardObjective =
                    "Prepare storyboard context.",
            )

        assertEquals(
            StoryToStoryboardPreparationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.storyboard)
    }

    @Test
    fun `empty storyboard sequence defers`() {
        val result =
            coordinator.prepare(
                traceId =
                    TraceId.from(
                        "trace:stage170-empty-scenes",
                    ),
                storyCreation = storyCreation(),
                story =
                    "Supplied story.",
                storyboardSceneDescriptions =
                    emptyList(),
                storyboardObjective =
                    "Prepare storyboard context.",
            )

        assertEquals(
            StoryToStoryboardPreparationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.storyboard)
    }

    @Test
    fun `blank storyboard scene description defers`() {
        val result =
            coordinator.prepare(
                traceId =
                    TraceId.from(
                        "trace:stage170-scene",
                    ),
                storyCreation = storyCreation(),
                story =
                    "Supplied story.",
                storyboardSceneDescriptions =
                    listOf(
                        "First scene.",
                        "   ",
                    ),
                storyboardObjective =
                    "Prepare storyboard context.",
            )

        assertEquals(
            StoryToStoryboardPreparationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.storyboard)
    }

    @Test
    fun `blank storyboard objective defers`() {
        val result =
            coordinator.prepare(
                traceId =
                    TraceId.from(
                        "trace:stage170-objective",
                    ),
                storyCreation = storyCreation(),
                story =
                    "Supplied story.",
                storyboardSceneDescriptions =
                    listOf(
                        "First scene.",
                    ),
                storyboardObjective = "   ",
            )

        assertEquals(
            StoryToStoryboardPreparationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.storyboard)
    }

    @Test
    fun `prepared result requires storyboard context`() {
        assertFailsWith<IllegalArgumentException> {
            StoryToStoryboardPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace:stage170-invalid-prepared",
                    ),
                status =
                    StoryToStoryboardPreparationStatus.PREPARED,
            )
        }
    }

    @Test
    fun `deferred result cannot contain storyboard context`() {
        val prepared =
            coordinator.prepare(
                traceId =
                    TraceId.from(
                        "trace:stage170-source",
                    ),
                storyCreation = storyCreation(),
                story =
                    "Supplied story.",
                storyboardSceneDescriptions =
                    listOf(
                        "First scene.",
                    ),
                storyboardObjective =
                    "Prepare storyboard context.",
            )

        val storyboard =
            requireNotNull(prepared.storyboard)

        assertFailsWith<IllegalArgumentException> {
            StoryToStoryboardPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace:stage170-invalid-deferred",
                    ),
                status =
                    StoryToStoryboardPreparationStatus.DEFERRED,
                storyboard = storyboard,
            )
        }
    }

    private fun storyCreation(): StoryCreationRecord {
        val project =
            CreativeMediaProjectRecord.create(
                projectId =
                    CreativeMediaProjectId.from(
                        "stage170-runtime-project",
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
