package com.devil.app.performance

import java.io.File
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Stage 267 Startup Optimization governance tests.
 *
 * Startup optimization changes initialization ordering only.
 *
 * STARTUP_OPTIMIZATION != AWAKENING_SHORTENING.
 * DEFERRED_INITIALIZATION != DISABLED_CAPABILITY.
 * SET_CONTENT_ESTABLISHED != APPLICATION_READY.
 *
 * Stage 267 does not implement Stage 268 or later Phase-S behavior.
 */
class Stage267StartupOptimizationTest {

    @Test
    fun `startup policy preserves composition-first ordering`() {
        val source = optimizationSource()

        assertTrue(
            source.contains(
                "const val COMPOSE_CONTENT_FIRST: Boolean = true",
            ),
        )

        assertTrue(
            source.contains(
                "const val PRESERVE_AWAKENING_TIMING: Boolean = true",
            ),
        )
    }

    @Test
    fun `Devil Activity establishes Compose content before nonessential startup sources`() {
        val source = activitySource()

        val setContentIndex =
            source.indexOf("setContent {")

        val voiceSourceIndex =
            source.indexOf(
                "voiceInputSource =\n            DefaultAndroidVoiceInputSource(",
            )

        val accessibilitySourceIndex =
            source.indexOf(
                "accessibilityDiagnosticSource =\n            DefaultAndroidAccessibilityServiceDiagnosticSource(",
            )

        assertTrue(setContentIndex >= 0)
        assertTrue(voiceSourceIndex > setContentIndex)
        assertTrue(accessibilitySourceIndex > setContentIndex)
    }

    @Test
    fun `established awakening timing remains unchanged`() {
        val source = launchTimingSource()

        for (
            expected in
                listOf(
                    "500L",
                    "1_000L",
                    "1_300L",
                    "1_200L",
                    "1_000L",
                )
        ) {
            assertTrue(
                source.contains(expected),
                "Missing established awakening timing value: $expected",
            )
        }
    }

    @Test
    fun `Stage 267 preserves startup constitutional boundaries`() {
        val source = optimizationSource()

        for (
            boundary in
                listOf(
                    "STARTUP_OPTIMIZATION != AWAKENING_SHORTENING.",
                    "STARTUP_OPTIMIZATION != STARTUP_PERFORMANCE_TARGET.",
                    "DEFERRED_INITIALIZATION != DISABLED_CAPABILITY.",
                    "DEFERRED_INITIALIZATION != AUTHORIZATION.",
                    "DEFERRED_INITIALIZATION != EXECUTION_APPROVAL.",
                    "SET_CONTENT_ESTABLISHED != APPLICATION_READY.",
                    "SET_CONTENT_ESTABLISHED != VERIFIED_OUTCOME.",
                )
        ) {
            assertTrue(
                source.contains(boundary),
                "Missing Stage 267 boundary: $boundary",
            )
        }
    }

    @Test
    fun `Stage 267 does not implement Stage 268 or later optimization behavior`() {
        val source = optimizationSource()

        assertTrue(
            source.contains(
                "Stage 267 does not optimize memory, CPU, battery, network, offline behavior,",
            ),
        )

        assertTrue(
            source.contains(
                "Those remain Stage 268 and later Phase-S responsibilities.",
            ),
        )
    }

    @Test
    fun `startup optimization contract contains no operational authority wiring`() {
        val source = optimizationSource()

        for (
            forbidden in
                listOf(
                    "UnifiedDevilRuntime",
                    "AuthorizationAuthority",
                    "ExecutionRequest(",
                    "MemoryAuthority",
                    "VerificationAuthority",
                )
        ) {
            assertFalse(
                source.contains(forbidden),
                "Stage 267 startup policy must not invoke authority: $forbidden",
            )
        }
    }

    private fun optimizationSource(): String =
        readSource(
            "app/src/main/kotlin/com/devil/app/performance/DevilStartupOptimization.kt",
            "src/main/kotlin/com/devil/app/performance/DevilStartupOptimization.kt",
        )

    private fun activitySource(): String =
        readSource(
            "app/src/main/kotlin/com/devil/app/DevilActivity.kt",
            "src/main/kotlin/com/devil/app/DevilActivity.kt",
        )

    private fun launchTimingSource(): String =
        readSource(
            "app/src/main/kotlin/com/devil/app/ui/launch/DevilLaunchTiming.kt",
            "src/main/kotlin/com/devil/app/ui/launch/DevilLaunchTiming.kt",
        )

    private fun readSource(
        vararg candidates: String,
    ): String =
        candidates
            .asSequence()
            .map(::File)
            .firstOrNull(File::isFile)
            ?.readText()
            ?: error(
                "Unable to locate Stage 267 source from: ${candidates.joinToString()}",
            )
}
