package com.devil.app.performance

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertSame
import kotlin.test.assertTrue

/**
 * Stage 268 Memory / CPU / Battery Optimization governance tests.
 */
class Stage268ResourceOptimizationTest {

    @Test
    fun `improved or equal supplied resources produce optimized status`() {
        val baseline =
            baseline(
                memory = 1_000L,
                cpu = 40.0,
                battery = 10.0,
            )

        val evidence =
            DevilResourceOptimizationEvidence(
                memoryUsageBytes = 900L,
                cpuUsagePercent = 40.0,
                batteryConsumptionPercent = 8.0,
            )

        val result =
            DevilResourceOptimizationCoordinator()
                .evaluate(
                    baseline = baseline,
                    optimizedEvidence = evidence,
                )

        assertEquals(
            DevilResourceOptimizationStatus.OPTIMIZED,
            result.status,
        )

        assertSame(
            baseline,
            result.baseline,
        )

        assertSame(
            evidence,
            result.optimizedEvidence,
        )
    }

    @Test
    fun `worse supplied resource evidence remains not optimized`() {
        val result =
            DevilResourceOptimizationCoordinator()
                .evaluate(
                    baseline =
                        baseline(
                            memory = 1_000L,
                            cpu = 40.0,
                            battery = 10.0,
                        ),
                    optimizedEvidence =
                        DevilResourceOptimizationEvidence(
                            memoryUsageBytes = 1_001L,
                            cpuUsagePercent = 40.0,
                            batteryConsumptionPercent = 10.0,
                        ),
                )

        assertEquals(
            DevilResourceOptimizationStatus.NOT_OPTIMIZED,
            result.status,
        )
    }

    @Test
    fun `missing Stage 266 resource baseline evidence is rejected`() {
        val incomplete =
            DevilPerformanceBaselineEvidence(
                startupDurationMillis = 5_000L,
                memoryUsageBytes = null,
                cpuUsagePercent = 40.0,
                batteryConsumptionPercent = 10.0,
                responsivenessLatencyMillis = 100L,
            )

        assertFailsWith<IllegalArgumentException> {
            DevilResourceOptimizationCoordinator()
                .evaluate(
                    baseline = incomplete,
                    optimizedEvidence =
                        DevilResourceOptimizationEvidence(
                            memoryUsageBytes = 900L,
                            cpuUsagePercent = 35.0,
                            batteryConsumptionPercent = 8.0,
                        ),
                )
        }
    }

    @Test
    fun `negative Stage 268 measurements are rejected`() {
        assertFailsWith<IllegalArgumentException> {
            DevilResourceOptimizationEvidence(
                memoryUsageBytes = -1L,
                cpuUsagePercent = 1.0,
                batteryConsumptionPercent = 1.0,
            )
        }

        assertFailsWith<IllegalArgumentException> {
            DevilResourceOptimizationEvidence(
                memoryUsageBytes = 1L,
                cpuUsagePercent = -1.0,
                batteryConsumptionPercent = 1.0,
            )
        }

        assertFailsWith<IllegalArgumentException> {
            DevilResourceOptimizationEvidence(
                memoryUsageBytes = 1L,
                cpuUsagePercent = 1.0,
                batteryConsumptionPercent = -1.0,
            )
        }
    }

    @Test
    fun `Stage 268 preserves constitutional and performance boundaries`() {
        val source = source()

        for (
            boundary in
                listOf(
                    "RESOURCE_OPTIMIZED != PERFORMANCE_TARGET.",
                    "RESOURCE_OPTIMIZED != PRODUCTION_READINESS.",
                    "LOWER_MEMORY_USAGE != MEMORY_CORRECTNESS.",
                    "LOWER_CPU_USAGE != EXECUTION_SUCCESS.",
                    "LOWER_BATTERY_CONSUMPTION != BACKGROUND_AUTHORIZATION.",
                    "PERFORMANCE_EVIDENCE != CONSTITUTIONAL_VERIFICATION.",
                    "OPTIMIZATION != VERIFIED_OUTCOME.",
                    "OPTIMIZATION != AUTHORIZATION.",
                    "OPTIMIZATION != EXECUTION_APPROVAL.",
                )
        ) {
            assertTrue(
                source.contains(boundary),
                "Missing Stage 268 boundary: $boundary",
            )
        }
    }

    @Test
    fun `Stage 268 stops before Stage 269 Network Reliability`() {
        assertTrue(
            source().contains(
                "Stage 268 does not implement Stage 269 Network Reliability",
            ),
        )
    }

    @Test
    fun `Stage 268 contains no operational authority or resource control wiring`() {
        val source = source()

        for (
            forbidden in
                listOf(
                    "UnifiedDevilRuntime",
                    "AuthorizationAuthority",
                    "ExecutionRequest(",
                    "MemoryAuthority",
                    "WorkManager",
                    "JobScheduler",
                    "PowerManager",
                    "BatteryManager",
                    "ActivityManager",
                    "Debug.MemoryInfo",
                    "TrafficStats",
                )
        ) {
            assertFalse(
                source.contains(forbidden),
                "Stage 268 must not introduce operational wiring: $forbidden",
            )
        }
    }

    private fun baseline(
        memory: Long,
        cpu: Double,
        battery: Double,
    ): DevilPerformanceBaselineEvidence =
        DevilPerformanceBaselineEvidence(
            startupDurationMillis = 5_000L,
            memoryUsageBytes = memory,
            cpuUsagePercent = cpu,
            batteryConsumptionPercent = battery,
            responsivenessLatencyMillis = 100L,
        )

    private fun source(): String =
        readSource(
            "app/src/main/kotlin/com/devil/app/performance/DevilResourceOptimization.kt",
            "src/main/kotlin/com/devil/app/performance/DevilResourceOptimization.kt",
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
                "Unable to locate Stage 268 source from: ${candidates.joinToString()}",
            )
}
