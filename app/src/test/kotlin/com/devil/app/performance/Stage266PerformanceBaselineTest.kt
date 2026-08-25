package com.devil.app.performance

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertSame
import kotlin.test.assertTrue

/**
 * Stage 266 Performance Baseline governance tests.
 *
 * Stage 266 records explicitly supplied performance evidence only.
 *
 * It does not profile Android, benchmark the runtime, optimize performance,
 * establish performance thresholds, authorize execution, or implement
 * Stage 267 Startup Optimization.
 */
class Stage266PerformanceBaselineTest {

    @Test
    fun `complete supplied measurements establish performance baseline`() {
        val evidence =
            DevilPerformanceBaselineEvidence(
                startupDurationMillis = 1800L,
                memoryUsageBytes = 128_000_000L,
                cpuUsagePercent = 24.5,
                batteryConsumptionPercent = 1.8,
                responsivenessLatencyMillis = 95L,
            )

        val result =
            DevilPerformanceBaselineCoordinator()
                .establish(evidence)

        assertEquals(
            DevilPerformanceBaselineStatus.ESTABLISHED,
            result.status,
        )

        assertSame(
            evidence,
            result.evidence,
        )
    }

    @Test
    fun `missing supplied measurement keeps baseline incomplete`() {
        val evidence =
            DevilPerformanceBaselineEvidence(
                startupDurationMillis = 1800L,
                memoryUsageBytes = 128_000_000L,
                cpuUsagePercent = null,
                batteryConsumptionPercent = 1.8,
                responsivenessLatencyMillis = 95L,
            )

        val result =
            DevilPerformanceBaselineCoordinator()
                .establish(evidence)

        assertEquals(
            DevilPerformanceBaselineStatus.INCOMPLETE,
            result.status,
        )

        assertFalse(
            result.evidence.isComplete(),
        )
    }

    @Test
    fun `baseline requires every established Stage 266 measurement category`() {
        val source = productionSource()

        for (
            expected in
                listOf(
                    "startupDurationMillis",
                    "memoryUsageBytes",
                    "cpuUsagePercent",
                    "batteryConsumptionPercent",
                    "responsivenessLatencyMillis",
                )
        ) {
            assertTrue(
                source.contains(expected),
                "Missing Stage 266 baseline measurement: $expected",
            )
        }
    }

    @Test
    fun `performance baseline preserves constitutional boundaries`() {
        val source = productionSource()

        for (
            boundary in
                listOf(
                    "PERFORMANCE_BASELINE != PERFORMANCE_OPTIMIZATION.",
                    "MEASURED != OPTIMIZED.",
                    "BASELINE_EVIDENCE != PERFORMANCE_TARGET.",
                    "BASELINE_EVIDENCE != PRODUCTION_READINESS.",
                    "PERFORMANCE_MEASUREMENT != CONSTITUTIONAL_VERIFICATION.",
                    "PERFORMANCE_MEASUREMENT != VERIFIED_OUTCOME.",
                    "PERFORMANCE_BASELINE != AUTHORIZATION.",
                    "PERFORMANCE_BASELINE != EXECUTION_APPROVAL.",
                )
        ) {
            assertTrue(
                source.contains(boundary),
                "Missing Stage 266 boundary: $boundary",
            )
        }
    }

    @Test
    fun `Stage 266 stops before Startup Optimization and later Phase S work`() {
        val source = productionSource()

        assertTrue(
            source.contains(
                "Stage 266 does not implement Stage 267 Startup Optimization",
            ),
        )

        assertTrue(
            source.contains(
                "or any later Phase-S optimization or reliability behavior.",
            ),
        )
    }

    @Test
    fun `performance baseline contains no operational profiling or authority wiring`() {
        val source = productionSource()

        for (
            forbidden in
                listOf(
                    "UnifiedDevilRuntime",
                    "AuthorizationAuthority",
                    "ExecutionRequest(",
                    "MemoryAuthority",
                    "ActivityManager",
                    "BatteryManager",
                    "Debug.MemoryInfo",
                    "android.os.Process",
                    "SystemClock.elapsedRealtime",
                )
        ) {
            assertFalse(
                source.contains(forbidden),
                "Stage 266 baseline must not perform operational measurement or authority wiring: $forbidden",
            )
        }
    }

    private fun productionSource(): String =
        readSource(
            "app/src/main/kotlin/com/devil/app/performance/DevilPerformanceBaseline.kt",
            "src/main/kotlin/com/devil/app/performance/DevilPerformanceBaseline.kt",
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
                "Unable to locate Stage 266 source from: ${candidates.joinToString()}",
            )
}
