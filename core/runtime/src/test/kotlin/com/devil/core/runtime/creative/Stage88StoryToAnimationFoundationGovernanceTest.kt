package com.devil.core.runtime.creative

import com.devil.core.model.common.TraceId
import com.devil.core.model.creative.AnimationSceneRecord
import com.devil.core.model.creative.CreativeMediaMedium
import com.devil.core.model.creative.CreativeMediaObjective
import com.devil.core.model.creative.CreativeMediaProjectId
import com.devil.core.model.creative.CreativeMediaProjectRecord
import com.devil.core.model.creative.StorySource
import com.devil.core.model.creative.StoryToAnimationRecord
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage88StoryToAnimationFoundationGovernanceTest {

    @Test
    fun `existing creative project may receive bounded supplied story and ordered scenes`() {
        val traceId =
            TraceId.from(
                "trace-stage88-story-animation-001",
            )

        val creativeProject =
            creativeProject()

        val result =
            StoryToAnimationCoordinator().prepare(
                traceId = traceId,
                creativeProject = creativeProject,
                story =
                    "A traveller enters an ancient forest and discovers a hidden city.",
                sceneSummaries =
                    listOf(
                        "The traveller approaches the ancient forest.",
                        "The traveller moves through the forest.",
                        "The hidden city is revealed.",
                    ),
            )

        assertEquals(
            traceId,
            result.traceId,
        )

        assertEquals(
            StoryToAnimationPreparationStatus.PREPARED,
            result.status,
        )

        val record =
            requireNotNull(result.record)

        assertSame(
            creativeProject,
            record.creativeProject,
        )

        assertEquals(
            "A traveller enters an ancient forest and discovers a hidden city.",
            record.story.content,
        )

        assertEquals(
            3,
            record.scenes.size,
        )

        assertEquals(
            listOf(1, 2, 3),
            record.scenes.map { it.position },
        )

        assertEquals(
            listOf(
                "The traveller approaches the ancient forest.",
                "The traveller moves through the forest.",
                "The hidden city is revealed.",
            ),
            record.scenes.map { it.summary },
        )
    }

    @Test
    fun `story source is normalized and required`() {
        assertEquals(
            "A bounded story.",
            StorySource.from(
                "  A bounded story.  ",
            ).content,
        )

        assertFailsWith<IllegalArgumentException> {
            StorySource.from("   ")
        }
    }

    @Test
    fun `animation scene normalizes supplied summary`() {
        val scene =
            AnimationSceneRecord.create(
                position = 1,
                summary =
                    "  Opening scene.  ",
            )

        assertEquals(
            1,
            scene.position,
        )

        assertEquals(
            "Opening scene.",
            scene.summary,
        )
    }

    @Test
    fun `animation scene position must be positive`() {
        assertFailsWith<IllegalArgumentException> {
            AnimationSceneRecord.create(
                position = 0,
                summary = "Invalid scene.",
            )
        }
    }

    @Test
    fun `animation scene summary must not be blank`() {
        assertFailsWith<IllegalArgumentException> {
            AnimationSceneRecord.create(
                position = 1,
                summary = "   ",
            )
        }
    }

    @Test
    fun `story to animation record requires at least one supplied scene`() {
        assertFailsWith<IllegalArgumentException> {
            StoryToAnimationRecord.create(
                creativeProject =
                    creativeProject(),
                story =
                    StorySource.from(
                        "A bounded story.",
                    ),
                scenes = emptyList(),
            )
        }
    }

    @Test
    fun `story to animation scenes must remain contiguously ordered`() {
        assertFailsWith<IllegalArgumentException> {
            StoryToAnimationRecord.create(
                creativeProject =
                    creativeProject(),
                story =
                    StorySource.from(
                        "A bounded story.",
                    ),
                scenes =
                    listOf(
                        AnimationSceneRecord.create(
                            position = 1,
                            summary = "First scene.",
                        ),
                        AnimationSceneRecord.create(
                            position = 3,
                            summary = "Invalid third-position scene.",
                        ),
                    ),
            )
        }
    }

    @Test
    fun `single supplied scene remains valid bounded story to animation preparation`() {
        val result =
            StoryToAnimationCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage88-story-animation-002",
                    ),
                creativeProject =
                    creativeProject(),
                story =
                    "A short story occurs entirely in one room.",
                sceneSummaries =
                    listOf(
                        "The complete bounded story is represented in one supplied scene.",
                    ),
            )

        assertEquals(
            StoryToAnimationPreparationStatus.PREPARED,
            result.status,
        )

        assertEquals(
            1,
            requireNotNull(result.record).scenes.size,
        )
    }

    @Test
    fun `blank story remains deferred`() {
        val result =
            StoryToAnimationCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage88-story-animation-003",
                    ),
                creativeProject =
                    creativeProject(),
                story = "   ",
                sceneSummaries =
                    listOf(
                        "One supplied scene.",
                    ),
            )

        assertEquals(
            StoryToAnimationPreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(result.record)
    }

    @Test
    fun `empty scene decomposition remains deferred`() {
        val result =
            StoryToAnimationCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage88-story-animation-004",
                    ),
                creativeProject =
                    creativeProject(),
                story =
                    "A bounded story.",
                sceneSummaries = emptyList(),
            )

        assertEquals(
            StoryToAnimationPreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(result.record)
    }

    @Test
    fun `blank supplied scene remains deferred rather than being invented`() {
        val result =
            StoryToAnimationCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage88-story-animation-005",
                    ),
                creativeProject =
                    creativeProject(),
                story =
                    "A bounded story.",
                sceneSummaries =
                    listOf(
                        "Opening scene.",
                        "   ",
                    ),
            )

        assertEquals(
            StoryToAnimationPreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(result.record)
    }

    @Test
    fun `prepared result requires one story to animation record`() {
        assertFailsWith<IllegalArgumentException> {
            StoryToAnimationPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage88-result-001",
                    ),
                status =
                    StoryToAnimationPreparationStatus.PREPARED,
            )
        }
    }

    @Test
    fun `deferred result cannot smuggle story to animation record`() {
        val record =
            StoryToAnimationRecord.create(
                creativeProject =
                    creativeProject(),
                story =
                    StorySource.from(
                        "A bounded story.",
                    ),
                scenes =
                    listOf(
                        AnimationSceneRecord.create(
                            position = 1,
                            summary = "One supplied scene.",
                        ),
                    ),
            )

        assertFailsWith<IllegalArgumentException> {
            StoryToAnimationPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage88-result-002",
                    ),
                status =
                    StoryToAnimationPreparationStatus.DEFERRED,
                record = record,
            )
        }
    }

    @Test
    fun `deferred result contains no story to animation record`() {
        val result =
            StoryToAnimationPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage88-result-003",
                    ),
                status =
                    StoryToAnimationPreparationStatus.DEFERRED,
            )

        assertNull(result.record)
    }

    private fun creativeProject(): CreativeMediaProjectRecord {
        return CreativeMediaProjectRecord.create(
            projectId =
                CreativeMediaProjectId.from(
                    "creative-project:stage88",
                ),
            objective =
                CreativeMediaObjective.create(
                    medium =
                        CreativeMediaMedium.from(
                            "animation",
                        ),
                    objective =
                        "Prepare a bounded animated story project.",
                ),
        )
    }
}
