package com.devil.app.reliability

import java.io.File
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Stage 321 Alpha Reliability Freeze completion evidence.
 *
 * This is test-only completion evidence for the already-established Devil
 * reliability architecture and completed Alpha boundary.
 *
 * Stage 321 does not modify production architecture and does not create a new
 * reliability, recovery, authorization, execution, observation, verification,
 * outcome, persistence, scheduling, or background-work authority.
 *
 * ALPHA_RELIABILITY_FREEZE != APPLICATION_NEVER_FAILS.
 * ALPHA_RELIABILITY_FREEZE != BACKGROUND_EXECUTION_AUTHORIZED.
 * ALPHA_RELIABILITY_FREEZE != AUTOMATIC_RECOVERY_AUTHORIZED.
 * ALPHA_RELIABILITY_FREEZE != AUTOMATIC_CONTINUATION_AUTHORIZED.
 * ALPHA_RELIABILITY_FREEZE != PERMANENT_AUTHORIZATION.
 * ALPHA_RELIABILITY_FREEZE != VERIFIED_OUTCOME.
 * FREEZE_EVIDENCE != CONSTITUTIONAL_VERIFICATION.
 *
 * Stage 321 does not implement Stage 322 or any Beta-stage behavior.
 */
class Stage321AlphaReliabilityFreezeTest {

    @Test
    fun `Stage 321 retains Stage 310 test only long duration reliability boundary`() {
        val stage310 =
            source(
                "app/src/test/kotlin/com/devil/app/performance/" +
                    "Stage310LongDurationReliabilityTests.kt",
            )

        assertContainsAll(
            stage310,
            "This is test-only completion evidence.",
            "does not modify production",
            "LONG_RUNNING_STABLE != APPLICATION_NEVER_CRASHES.",
            "LONG_RUNNING_STABLE != BACKGROUND_EXECUTION_AUTHORIZED.",
            "LONG_RUNNING_STABLE != RECOVERY_EXECUTED.",
            "LONG_RUNNING_STABLE != VERIFIED_OUTCOME.",
            "LONG_RUNNING_STABLE != AUTHORIZATION.",
            "LONG_RUNNING_STABLE != EXECUTION_APPROVAL.",
            "STABILITY_EVIDENCE != CONSTITUTIONAL_VERIFICATION.",
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

    @Test
    fun `Stage 321 retains bounded network crash and long running reliability contracts`() {
        val stage269 =
            source(
                "app/src/main/kotlin/com/devil/app/performance/" +
                    "DevilNetworkReliability.kt",
            )

        val stage271 =
            source(
                "app/src/main/kotlin/com/devil/app/performance/" +
                    "DevilCrashRecovery.kt",
            )

        val stage272 =
            source(
                "app/src/main/kotlin/com/devil/app/performance/" +
                    "DevilLongRunningStability.kt",
            )

        assertContainsAll(
            stage269,
            "NETWORK_RELIABLE != CONNECTIVITY_GUARANTEED.",
            "NETWORK_RELIABLE != RETRY_AUTHORIZED.",
            "NETWORK_RELIABILITY != VERIFIED_OUTCOME.",
            "NETWORK_RELIABILITY != AUTHORIZATION.",
            "NETWORK_RELIABILITY != EXECUTION_APPROVAL.",
            "does not perform networking",
        )

        assertContainsAll(
            stage271,
            "CRASH_RECOVERY_READY != RECOVERY_EXECUTED.",
            "CRASH_RECOVERY_READY != PROCESS_RESTARTED.",
            "CRASH_RECOVERY_READY != VERIFIED_OUTCOME.",
            "CRASH_RECOVERY_READY != AUTHORIZATION.",
            "CRASH_RECOVERY_READY != EXECUTION_APPROVAL.",
            "RECOVERY_REQUEST_AVAILABLE != RECOVERY_EXECUTED.",
            "RECOVERY_ATTEMPT_BUDGET != RETRY_PERMISSION.",
        )

        assertContainsAll(
            stage272,
            "LONG_RUNNING_STABLE != APPLICATION_NEVER_CRASHES.",
            "LONG_RUNNING_STABLE != RESOURCE_LEAK_IMPOSSIBLE.",
            "LONG_RUNNING_STABLE != BACKGROUND_EXECUTION_AUTHORIZED.",
            "LONG_RUNNING_STABLE != RECOVERY_EXECUTED.",
            "LONG_RUNNING_STABLE != VERIFIED_OUTCOME.",
            "LONG_RUNNING_STABLE != AUTHORIZATION.",
            "LONG_RUNNING_STABLE != EXECUTION_APPROVAL.",
            "STABILITY_EVIDENCE != CONSTITUTIONAL_VERIFICATION.",
            "STABLE",
            "STABILITY_NOT_ESTABLISHED",
        )
    }

    @Test
    fun `Stage 321 retains Stage 320 fail closed stability dependency and provenance boundary`() {
        val coordinator =
            source(
                "app/src/main/kotlin/com/devil/app/reliability/" +
                    "Stage320LongRunningAssistantAlphaCoordinator.kt",
            )

        val result =
            source(
                "app/src/main/kotlin/com/devil/app/reliability/" +
                    "Stage320LongRunningAssistantAlphaResult.kt",
            )

        val test =
            source(
                "app/src/test/kotlin/com/devil/app/reliability/" +
                    "Stage320LongRunningAssistantAlphaCoordinatorTest.kt",
            )

        assertContainsAll(
            coordinator,
            "DevilLongRunningStabilityCoordinator",
            "DevilLongRunningStabilityStatus.STABLE",
            "Stage320LongRunningAssistantAlphaStatus.AVAILABLE",
            "DEFERRED",
            "LONG_RUNNING_GOAL != PERMANENT_AUTHORIZATION.",
            "GOAL_CONTINUITY != EXECUTION_CONTINUITY.",
            "LONG_RUNNING_ALPHA != BACKGROUND_EXECUTION_AUTHORIZED.",
            "LONG_RUNNING_ALPHA != AUTOMATIC_CONTINUATION_AUTHORITY.",
            "STABLE != VERIFIED_OUTCOME.",
        )

        assertContainsAll(
            result,
            "DevilLongRunningStabilityStatus.STABLE",
            "Stage320LongRunningAssistantAlphaStatus.AVAILABLE",
            "Stage320LongRunningAssistantAlphaStatus.DEFERRED",
            "goal",
            "stability",
        )

        assertContainsAll(
            test,
            "assertSame",
            "STABILITY_NOT_ESTABLISHED",
            "assertFailsWith<IllegalArgumentException>",
        )
    }

    @Test
    fun `Stage 321 adds no production freeze authority`() {
        val productionSources = productionSources()

        assertTrue(
            productionSources.none { file ->
                file.readText().contains("Stage321AlphaReliabilityFreeze")
            },
            "Stage 321 must remain test-only and must not create production freeze authority.",
        )

        assertTrue(
            productionSources.none { file ->
                file.readText().contains(
                    "Stage321AlphaReliabilityFreeze" + "Coordinator",
                )
            },
            "Stage 321 must not create a reliability-freeze coordinator.",
        )
    }

    @Test
    fun `Stage 321 locks Alpha reliability freeze semantics`() {
        val stage321 =
            source(
                "app/src/test/kotlin/com/devil/app/reliability/" +
                    "Stage321AlphaReliabilityFreezeTest.kt",
            )

        assertContainsAll(
            stage321,
            "This is test-only completion evidence",
            "does not modify production architecture",
            "ALPHA_RELIABILITY_FREEZE != APPLICATION_NEVER_FAILS.",
            "ALPHA_RELIABILITY_FREEZE != BACKGROUND_EXECUTION_AUTHORIZED.",
            "ALPHA_RELIABILITY_FREEZE != AUTOMATIC_RECOVERY_AUTHORIZED.",
            "ALPHA_RELIABILITY_FREEZE != AUTOMATIC_CONTINUATION_AUTHORIZED.",
            "ALPHA_RELIABILITY_FREEZE != PERMANENT_AUTHORIZATION.",
            "ALPHA_RELIABILITY_FREEZE != VERIFIED_OUTCOME.",
            "FREEZE_EVIDENCE != CONSTITUTIONAL_VERIFICATION.",
            "does not implement Stage 322 or any Beta-stage behavior",
        )
    }

    private fun assertContainsAll(
        source: String,
        vararg markers: String,
    ) {
        markers.forEach { marker ->
            assertTrue(
                source.contains(marker),
                "Missing Stage 321 Alpha reliability-freeze evidence: $marker",
            )
        }
    }

    private fun productionSources(): List<File> {
        val relativePath = "app/src/main/kotlin"

        val root =
            listOf(
                File(relativePath),
                File("../$relativePath"),
                File("../../$relativePath"),
            ).firstOrNull { it.isDirectory }
                ?: error(
                    "Unable to locate production source tree for Stage 321.",
                )

        return root
            .walkTopDown()
            .filter { it.isFile && it.extension == "kt" }
            .toList()
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
            ?: error("Unable to locate repository source for Stage 321: $path")
    }
}
