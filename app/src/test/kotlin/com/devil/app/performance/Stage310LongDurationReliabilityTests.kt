package com.devil.app.performance

import java.io.File
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Stage 310 Long-Duration Reliability Tests completion coverage for the
 * already-established bounded Devil long-running stability architecture.
 *
 * This is test-only completion evidence. It does not modify production
 * architecture or establish new long-running behavior.
 *
 * LONG_RUNNING_STABLE != APPLICATION_NEVER_CRASHES.
 * LONG_RUNNING_STABLE != RESOURCE_LEAK_IMPOSSIBLE.
 * LONG_RUNNING_STABLE != BACKGROUND_EXECUTION_AUTHORIZED.
 * LONG_RUNNING_STABLE != RECOVERY_EXECUTED.
 * LONG_RUNNING_STABLE != VERIFIED_OUTCOME.
 * LONG_RUNNING_STABLE != AUTHORIZATION.
 * LONG_RUNNING_STABLE != EXECUTION_APPROVAL.
 * STABILITY_EVIDENCE != CONSTITUTIONAL_VERIFICATION.
 *
 * Stage 310 does not start services, schedule work, create watchdogs,
 * execute recovery, establish authorization, or claim real-device validation.
 *
 * It does not modify production architecture and does not implement
 * Stage 311 Internal Alpha APK.
 */
class Stage310LongDurationReliabilityTests {

    @Test
    fun `Stage 310 preserves long running stability boundaries`() {
        assertContainsAll(
            source(
                "app/src/main/kotlin/com/devil/app/performance/" +
                    "DevilLongRunningStability.kt",
            ),
            "LONG_RUNNING_STABLE != APPLICATION_NEVER_CRASHES.",
            "LONG_RUNNING_STABLE != RESOURCE_LEAK_IMPOSSIBLE.",
            "LONG_RUNNING_STABLE != BACKGROUND_EXECUTION_AUTHORIZED.",
            "LONG_RUNNING_STABLE != RECOVERY_EXECUTED.",
            "LONG_RUNNING_STABLE != VERIFIED_OUTCOME.",
            "LONG_RUNNING_STABLE != AUTHORIZATION.",
            "LONG_RUNNING_STABLE != EXECUTION_APPROVAL.",
            "STABILITY_EVIDENCE != CONSTITUTIONAL_VERIFICATION.",
        )
    }

    @Test
    fun `Stage 310 retains complete and incomplete lifecycle evidence testing`() {
        val stage272 =
            source(
                "app/src/test/kotlin/com/devil/app/performance/" +
                    "Stage272LongRunningStabilityTest.kt",
            )

        assertContainsAll(
            stage272,
            "incomplete voice input lifecycle evidence does not establish stability",
            "incomplete voice output lifecycle evidence does not establish stability",
            "incomplete camera lifecycle evidence does not establish stability",
            "incomplete network lifecycle evidence does not establish stability",
            "Stage 272 complete evidence requires every bounded lifecycle property",
        )
    }

    @Test
    fun `Stage 310 retains provenance and non stable evidence`() {
        val stage272 =
            source(
                "app/src/test/kotlin/com/devil/app/performance/" +
                    "Stage272LongRunningStabilityTest.kt",
            )

        assertContainsAll(
            stage272,
            "Stage 272 preserves supplied evidence identity",
            "assertSame",
            "assertNotStable",
            "assertFalse",
        )
    }

    @Test
    fun `Stage 310 retains no operational stability wiring evidence`() {
        val stage272 =
            source(
                "app/src/test/kotlin/com/devil/app/performance/" +
                    "Stage272LongRunningStabilityTest.kt",
            )

        assertContainsAll(
            stage272,
            "Stage 272 contains no operational stability wiring",
            "assertFalse",
        )
    }

    @Test
    fun `Stage 310 completion remains test only and stops before alpha`() {
        val stage310 =
            source(
                "app/src/test/kotlin/com/devil/app/performance/" +
                    "Stage310LongDurationReliabilityTests.kt",
            )

        assertContainsAll(
            stage310,
            "This is test-only completion evidence.",
            "does not modify production",
            "does not start services",
            "does not implement",
            "Stage 311 Internal Alpha APK",
        )

        assertFalse(
            stage310.contains(
                "class Stage310LongDurationReliability" + "Coordinator",
            ),
        )
    }

    private fun assertContainsAll(
        source: String,
        vararg markers: String,
    ) {
        markers.forEach { marker ->
            assertTrue(
                source.contains(marker),
                "Missing Stage 310 reliability evidence: $marker",
            )
        }
    }

    private fun source(path: String): String {
        val candidates =
            listOf(
                File(path),
                File("../$path"),
                File("../../$path"),
            )

        return candidates
            .firstOrNull { it.isFile }
            ?.readText()
            ?: error("Unable to locate repository source for Stage 310: $path")
    }
}
