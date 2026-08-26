package com.devil.app.memory

import java.io.File
import kotlin.test.Test
import kotlin.test.assertTrue

/**
 * Stage 305 Memory & Continuity Tests completion coverage for the established
 * bounded Devil Memory Authority, logical-memory continuity, recall,
 * persistence, and cross-device memory-continuity architecture.
 *
 * This test surface validates existing memory and continuity behavior only.
 *
 * MEMORY_PROPOSAL != MEMORY_AUTHORITY_APPROVAL.
 * MEMORY_AUTHORITY_APPROVAL != MEMORY_COMMITMENT.
 * MEMORY_COMMITMENT != MEMORY_PERSISTENCE.
 * MEMORY_CONTINUITY != MEMORY_RECALL.
 * RECALL_ELIGIBILITY != MEMORY_RECALL.
 * MEMORY_CONTINUITY != AUTHORIZATION.
 * CROSS_DEVICE_MEMORY_CONTINUITY != MEMORY_SYNC.
 * CROSS_DEVICE_MEMORY_CONTINUITY != MEMORY_REPLICATION.
 * CROSS_DEVICE_MEMORY_CONTINUITY != MEMORY_TRANSFER.
 * CROSS_DEVICE_MEMORY_CONTINUITY != REMOTE_EXECUTION.
 *
 * Stage 305 does not create another runtime, Brain, Planner, Executive,
 * Memory Authority, persistence authority, storage engine, synchronization
 * mechanism, recall engine, authorization authority, or platform capability.
 *
 * It does not modify production architecture and does not implement
 * Stage 306 Voice Tests.
 */
class Stage305MemoryContinuityTests {

    @Test
    fun `Memory Authority remains separate from commitment persistence and recall`() {
        val evaluator =
            source(
                "core/runtime/src/main/kotlin/com/devil/core/runtime/memory/DefaultMemoryAuthorityEvaluator.kt",
            )
        val representation =
            source(
                "core/runtime/src/main/kotlin/com/devil/core/runtime/memory/MemoryRepresentationPreparationCoordinator.kt",
            )

        listOf(
            "MEMORY_PROPOSAL != MEMORY_AUTHORITY_EVIDENCE.",
            "MEMORY_AUTHORITY_EVIDENCE != MEMORY_AUTHORITY_APPROVAL.",
            "MEMORY_AUTHORITY_APPROVAL != MEMORY_COMMITMENT.",
            "MEMORY_COMMITMENT != MEMORY_PERSISTENCE.",
        ).forEach { marker ->
            assertTrue(
                evaluator.contains(marker),
                "Missing Stage 305 Memory Authority boundary: $marker",
            )
        }

        listOf(
            "MEMORY_AUTHORITY_COMMITTABLE != MEMORY_COMMITMENT.",
            "REPRESENTATION_PREPARED != MEMORY_COMMITMENT.",
            "REPRESENTATION_PREPARED != MEMORY_PERSISTENCE.",
            "SUBJECT_MATCH != AUTHENTICATION.",
        ).forEach { marker ->
            assertTrue(
                representation.contains(marker),
                "Missing Stage 305 representation boundary: $marker",
            )
        }
    }

    @Test
    fun `logical memory continuity preserves representation without becoming persistence recall or authorization`() {
        val continuity =
            source(
                "core/runtime/src/main/kotlin/com/devil/core/runtime/memory/MemoryContinuityCoordinator.kt",
            )
        val status =
            source(
                "core/runtime/src/main/kotlin/com/devil/core/runtime/memory/MemoryContinuityStatus.kt",
            )

        listOf(
            "MEMORY_CONTINUITY != MEMORY_COMMITMENT.",
            "MEMORY_CONTINUITY != MEMORY_PERSISTENCE.",
            "MEMORY_CONTINUITY != STORAGE_SUCCESS.",
            "MEMORY_CONTINUITY != RECALL_AVAILABILITY.",
            "MEMORY_CONTINUITY != MEMORY_RECALL.",
            "SUBJECT_CONTINUITY != AUTHENTICATION.",
        ).forEach { marker ->
            assertTrue(
                continuity.contains(marker),
                "Missing Stage 305 continuity boundary: $marker",
            )
        }

        listOf(
            "MEMORY_CONTINUITY_ESTABLISHED != MEMORY_COMMITMENT.",
            "MEMORY_CONTINUITY_ESTABLISHED != MEMORY_PERSISTENCE.",
            "MEMORY_CONTINUITY_ESTABLISHED != STORAGE_SUCCESS.",
            "MEMORY_CONTINUITY_ESTABLISHED != RECALL_AVAILABILITY.",
            "MEMORY_CONTINUITY_ESTABLISHED != MEMORY_RECALL.",
        ).forEach { marker ->
            assertTrue(
                status.contains(marker),
                "Missing Stage 305 continuity-status boundary: $marker",
            )
        }
    }

    @Test
    fun `recall eligibility request and evaluation remain separate from actual memory recall`() {
        val eligibility =
            source(
                "core/runtime/src/main/kotlin/com/devil/core/runtime/memory/MemoryRecallEligibilityCoordinator.kt",
            )
        val request =
            source(
                "core/runtime/src/main/kotlin/com/devil/core/runtime/memory/MemoryRecallRequestProvider.kt",
            )
        val evaluator =
            source(
                "core/runtime/src/main/kotlin/com/devil/core/runtime/memory/MemoryRecallEvaluator.kt",
            )

        listOf(
            "RECALL_ELIGIBILITY != MEMORY_RECALL.",
            "RECALL_ELIGIBILITY != STORAGE_READ.",
            "RECALL_ELIGIBILITY != DISCLOSURE_PERMISSION.",
            "AUTHORIZATION != PRIVACY_DISCLOSURE_PERMISSION.",
            "MEMORY_SENSITIVITY != PRIVACY_DISCLOSURE_POLICY.",
        ).forEach { marker ->
            assertTrue(
                eligibility.contains(marker),
                "Missing Stage 305 recall-eligibility boundary: $marker",
            )
        }

        assertTrue(request.contains("does not read storage"))
        assertTrue(request.contains("recall logical memory"))
        assertTrue(request.contains("establish disclosure permission"))

        assertTrue(
            evaluator.contains(
                "read storage, retrieve memory, expose content",
            ),
        )
        assertTrue(
            evaluator.contains(
                "execute an action, or claim successful recall.",
            ),
        )
    }

    @Test
    fun `memory commitment persistence and Android storage remain distinct governed boundaries`() {
        val commitment =
            source(
                "core/runtime/src/main/kotlin/com/devil/core/runtime/memory/DefaultMemoryCommitmentAuthority.kt",
            )
        val persistence =
            source(
                "core/runtime/src/main/kotlin/com/devil/core/runtime/memory/DefaultMemoryPersistenceAuthority.kt",
            )
        val androidPersistence =
            source(
                "app/src/main/kotlin/com/devil/app/memory/DefaultAndroidMemoryPersistenceCoordinator.kt",
            )

        assertTrue(
            commitment.contains(
                "It does not create, persist, store, expose, recall, or commit logical memory.",
            ),
        )

        assertTrue(
            persistence.contains(
                "It does not create, persist, store, expose, recall, delete, or commit",
            ),
        )
        assertTrue(
            persistence.contains(
                "It remains governed by the single Memory Authority",
            ),
        )

        assertTrue(
            androidPersistence.contains(
                "Core PERSISTABLE means only that a bounded MemoryPersistenceRequest became",
            ),
        )
        assertTrue(
            androidPersistence.contains(
                "It does not mean logical memory was",
            ),
        )
        assertTrue(
            androidPersistence.contains(
                "The default Android store remains DEFERRED and performs no durable write.",
            ),
        )
    }

    @Test
    fun `cross device memory continuity remains continuity not synchronization transfer persistence recall or execution`() {
        val coordinator =
            source(
                "app/src/main/kotlin/com/devil/app/device/AndroidCrossDeviceMemoryContinuityCoordinator.kt",
            )
        val result =
            source(
                "app/src/main/kotlin/com/devil/app/device/AndroidCrossDeviceMemoryContinuityResult.kt",
            )

        listOf(
            "CROSS_DEVICE_MEMORY_CONTINUITY != MEMORY_SYNC.",
            "CROSS_DEVICE_MEMORY_CONTINUITY != MEMORY_REPLICATION.",
            "CROSS_DEVICE_MEMORY_CONTINUITY != MEMORY_TRANSFER.",
            "CROSS_DEVICE_MEMORY_CONTINUITY != MEMORY_COMMITMENT.",
            "CROSS_DEVICE_MEMORY_CONTINUITY != MEMORY_PERSISTENCE.",
            "CROSS_DEVICE_MEMORY_CONTINUITY != STORAGE_SUCCESS.",
            "CROSS_DEVICE_MEMORY_CONTINUITY != RECALL_AVAILABILITY.",
            "CROSS_DEVICE_MEMORY_CONTINUITY != MEMORY_RECALL.",
            "MEMORY_CONTINUITY != AUTHORIZATION.",
            "MEMORY_CONTINUITY != REMOTE_EXECUTION.",
        ).forEach { marker ->
            assertTrue(
                coordinator.contains(marker),
                "Missing Stage 305 cross-device memory boundary: $marker",
            )
            assertTrue(result.contains(marker))
        }

        assertTrue(
            result.contains(
                "Available Stage 221 Cross-Device Memory Continuity requires available Stage 220 Cross-Device Task Continuity.",
            ),
        )
        assertTrue(
            result.contains(
                "Available Stage 221 Cross-Device Memory Continuity requires ESTABLISHED Stage 103 Memory Continuity.",
            ),
        )
    }

    @Test
    fun `existing memory tests preserve provenance deferral failure and result invariants`() {
        val representativeTests =
            listOf(
                "core/runtime/src/test/kotlin/com/devil/core/runtime/memory/Stage103MemoryContinuityGovernanceTest.kt",
                "core/runtime/src/test/kotlin/com/devil/core/runtime/memory/Stage104MemoryRecallEligibilityGovernanceTest.kt",
                "core/runtime/src/test/kotlin/com/devil/core/runtime/memory/Stage105MemoryRecallRequestGovernanceTest.kt",
                "core/runtime/src/test/kotlin/com/devil/core/runtime/memory/Stage106MemoryRecallEvaluationGovernanceTest.kt",
                "app/src/test/kotlin/com/devil/app/memory/DefaultAndroidMemoryPersistenceCoordinatorTest.kt",
                "app/src/test/kotlin/com/devil/app/device/Stage221CrossDeviceMemoryContinuityTest.kt",
            )

        representativeTests.forEachIndexed { index, path ->
            val test = source(path)

            assertTrue(
                test.contains("DEFERRED") ||
                    test.contains("UNAVAILABLE") ||
                    test.contains("FAILED"),
                "Stage 305 representative memory test $index lacks non-success coverage.",
            )

            assertTrue(
                test.contains("assertSame") ||
                    test.contains("assertFailsWith"),
                "Stage 305 representative memory test $index lacks provenance or invariant coverage.",
            )
        }
    }

    @Test
    fun `Stage 305 stops before voice test completion`() {
        val stage305 =
            source(
                "app/src/test/kotlin/com/devil/app/memory/Stage305MemoryContinuityTests.kt",
            )

        assertTrue(stage305.contains("does not implement"))
        assertTrue(stage305.contains("Stage 306 Voice Tests"))
    }

    private fun source(path: String): String {
        val candidates =
            listOf(
                File(path),
                File("..", path),
            )

        return candidates
            .firstOrNull { it.isFile }
            ?.readText()
            ?: error(
                "Unable to locate repository source for Stage 305: $path",
            )
    }
}
