package com.devil.app.performance

import com.devil.app.internet.AndroidInternetKnowledgeResult
import com.devil.app.reliability.AndroidReliabilityRecoveryCoordinator
import com.devil.app.reliability.AndroidReliabilityRecoveryResult
import com.devil.app.reliability.AndroidReliabilityRecoveryStatus
import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.TraceId
import com.devil.core.model.error.ErrorCode
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.reliability.RecoveryAttemptBudget
import com.devil.core.model.reliability.RecoveryDisposition
import com.devil.core.model.reliability.RecoveryEvidence
import com.devil.core.model.reliability.RecoveryRequestStatus
import com.devil.core.model.reliability.RecoveryStrategy
import com.devil.core.model.reliability.ReliabilityCoordinator
import com.devil.core.model.reliability.ReliabilityCondition
import com.devil.core.runtime.modelprovider.LocalModelFoundationCoordinator
import com.devil.core.runtime.modelprovider.LocalModelFoundationResult
import com.devil.core.runtime.modelprovider.ModelProviderArchitectureCoordinator
import com.devil.core.runtime.modelprovider.ModelProviderArchitectureResult
import com.devil.core.runtime.modelprovider.ModelRoutingCoordinator
import com.devil.core.runtime.modelprovider.ModelRoutingResult
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertSame
import kotlin.test.assertTrue

/**
 * Stage 330 Performance Beta completion validation.
 *
 * This is Beta test-only validation of the already-established bounded
 * Stage 266-274 performance and reliability architecture.
 *
 * Stage 330 does not create a benchmark engine, profiler, performance target,
 * resource controller, retry engine, watchdog, scheduler, background-execution
 * mechanism, recovery executor, authorization path, or execution authority.
 *
 * PERFORMANCE_BETA_PASSED != PERFORMANCE_TARGET_GUARANTEED.
 * PERFORMANCE_BETA_PASSED != APPLICATION_NEVER_FAILS.
 * PERFORMANCE_BETA_PASSED != PRODUCTION_READINESS.
 * PERFORMANCE_BETA_PASSED != AUTHORIZATION.
 * PERFORMANCE_BETA_PASSED != EXECUTION_APPROVAL.
 * PERFORMANCE_BETA_PASSED != VERIFIED_OUTCOME.
 * PERFORMANCE_BETA_EVIDENCE != CONSTITUTIONAL_VERIFICATION.
 * STAGE_330 != STAGE_331_BETA_DEFECT_CLOSURE.
 *
 * Stage 330 does not implement Stage 331 Beta Defect Closure.
 */
class Stage330PerformanceBetaTest {

    @Test
    fun `Stage 330 preserves performance baseline integrity`() {
        val completeEvidence =
            DevilPerformanceBaselineEvidence(
                startupDurationMillis = 1_800L,
                memoryUsageBytes = 128_000_000L,
                cpuUsagePercent = 24.5,
                batteryConsumptionPercent = 1.8,
                responsivenessLatencyMillis = 95L,
            )

        val complete =
            DevilPerformanceBaselineCoordinator()
                .establish(completeEvidence)

        assertEquals(
            DevilPerformanceBaselineStatus.ESTABLISHED,
            complete.status,
        )
        assertSame(
            completeEvidence,
            complete.evidence,
        )

        val incompleteEvidence =
            completeEvidence.copy(
                responsivenessLatencyMillis = null,
            )

        val incomplete =
            DevilPerformanceBaselineCoordinator()
                .establish(incompleteEvidence)

        assertEquals(
            DevilPerformanceBaselineStatus.INCOMPLETE,
            incomplete.status,
        )
        assertSame(
            incompleteEvidence,
            incomplete.evidence,
        )
        assertFalse(
            incomplete.evidence.isComplete(),
        )
    }

    @Test
    fun `Stage 330 preserves startup and resource optimization regression boundaries`() {
        val startupSource =
            source(
                "app/src/main/kotlin/com/devil/app/performance/" +
                    "DevilStartupOptimization.kt",
            )

        assertContainsAll(
            startupSource,
            "const val COMPOSE_CONTENT_FIRST: Boolean = true",
            "const val PRESERVE_AWAKENING_TIMING: Boolean = true",
            "STARTUP_OPTIMIZATION != AWAKENING_SHORTENING.",
            "STARTUP_OPTIMIZATION != STARTUP_PERFORMANCE_TARGET.",
            "DEFERRED_INITIALIZATION != AUTHORIZATION.",
            "DEFERRED_INITIALIZATION != EXECUTION_APPROVAL.",
            "SET_CONTENT_ESTABLISHED != APPLICATION_READY.",
            "SET_CONTENT_ESTABLISHED != VERIFIED_OUTCOME.",
        )

        val baseline =
            DevilPerformanceBaselineEvidence(
                startupDurationMillis = 5_000L,
                memoryUsageBytes = 1_000L,
                cpuUsagePercent = 40.0,
                batteryConsumptionPercent = 10.0,
                responsivenessLatencyMillis = 100L,
            )

        val improved =
            DevilResourceOptimizationEvidence(
                memoryUsageBytes = 900L,
                cpuUsagePercent = 39.0,
                batteryConsumptionPercent = 9.0,
            )

        val optimized =
            DevilResourceOptimizationCoordinator()
                .evaluate(
                    baseline = baseline,
                    optimizedEvidence = improved,
                )

        assertEquals(
            DevilResourceOptimizationStatus.OPTIMIZED,
            optimized.status,
        )
        assertSame(
            baseline,
            optimized.baseline,
        )
        assertSame(
            improved,
            optimized.optimizedEvidence,
        )

        val worse =
            DevilResourceOptimizationCoordinator()
                .evaluate(
                    baseline = baseline,
                    optimizedEvidence =
                        improved.copy(
                            memoryUsageBytes = 1_001L,
                        ),
                )

        assertEquals(
            DevilResourceOptimizationStatus.NOT_OPTIMIZED,
            worse.status,
        )

        assertContainsAll(
            source(
                "app/src/main/kotlin/com/devil/app/performance/" +
                    "DevilResourceOptimization.kt",
            ),
            "RESOURCE_OPTIMIZED != PERFORMANCE_TARGET.",
            "RESOURCE_OPTIMIZED != PRODUCTION_READINESS.",
            "PERFORMANCE_EVIDENCE != CONSTITUTIONAL_VERIFICATION.",
            "OPTIMIZATION != VERIFIED_OUTCOME.",
            "OPTIMIZATION != AUTHORIZATION.",
            "OPTIMIZATION != EXECUTION_APPROVAL.",
        )
    }

    @Test
    fun `Stage 330 keeps degraded network and incomplete offline evidence fail closed`() {
        val degradedNetwork =
            DevilNetworkReliabilityCoordinator()
                .evaluate(
                    DevilNetworkReliabilityEvidence(
                        retrievalResult =
                            AndroidInternetKnowledgeResult.unavailable(),
                        connectTimeoutBounded = true,
                        readTimeoutBounded = true,
                        connectionCleanupBounded = true,
                    ),
                )

        assertEquals(
            DevilNetworkReliabilityStatus.DEGRADED,
            degradedNetwork.status,
        )

        val foundation =
            availableLocalModelFoundation()

        val incompleteOffline =
            DevilOfflineBehaviourCoordinator()
                .evaluate(
                    evidence =
                        DevilOfflineBehaviourEvidence(
                            localModelFoundation = foundation,
                            modelFileAvailable = true,
                            deviceCompatibilityEstablished = true,
                            localRuntimeAvailable = true,
                            offlineInvocationAvailable = false,
                        ),
                )

        assertEquals(
            DevilOfflineBehaviourStatus.NOT_OFFLINE_READY,
            incompleteOffline.status,
        )
        assertSame(
            foundation,
            incompleteOffline.evidence.localModelFoundation,
        )
        assertFalse(
            incompleteOffline.evidence.isComplete(),
        )
    }

    @Test
    fun `Stage 330 keeps incomplete recovery and stability evidence fail closed`() {
        val recovery =
            availableRecoveryResult()

        val incompleteRecoveryEvidence =
            DevilCrashRecoveryEvidence(
                reliabilityRecovery = recovery,
                crashFailurePreserved = true,
                boundedRecoveryPathEstablished = false,
                lifecycleReentryPrepared = true,
            )

        val recoveryResult =
            DevilCrashRecoveryCoordinator()
                .evaluate(incompleteRecoveryEvidence)

        assertEquals(
            DevilCrashRecoveryStatus.RECOVERY_DEFERRED,
            recoveryResult.status,
        )
        assertSame(
            recovery,
            recoveryResult.evidence.reliabilityRecovery,
        )
        assertFalse(
            incompleteRecoveryEvidence.isComplete(),
        )

        val incompleteStabilityEvidence =
            DevilLongRunningStabilityEvidence(
                voiceInputLifecycleBounded = true,
                voiceOutputLifecycleBounded = true,
                cameraResourceLifecycleBounded = true,
                networkConnectionLifecycleBounded = true,
                uncontrolledBackgroundWorkAbsent = false,
                automaticRecoveryLoopAbsent = true,
            )

        val stabilityResult =
            DevilLongRunningStabilityCoordinator()
                .evaluate(
                    evidence = incompleteStabilityEvidence,
                )

        assertEquals(
            DevilLongRunningStabilityStatus.STABILITY_NOT_ESTABLISHED,
            stabilityResult.status,
        )
        assertSame(
            incompleteStabilityEvidence,
            stabilityResult.evidence,
        )
        assertFalse(
            incompleteStabilityEvidence.isComplete(),
        )
    }

    @Test
    fun `Stage 330 preserves long duration completion and Alpha freeze continuity`() {
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
            "STABILITY_EVIDENCE != CONSTITUTIONAL_VERIFICATION.",
            "Stage 311 Internal Alpha APK",
        )

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

    @Test
    fun `Stage 330 remains test only non authoritative and stops before Stage 331`() {
        val stage330 =
            source(
                "app/src/test/kotlin/com/devil/app/performance/" +
                    "Stage330PerformanceBetaTest.kt",
            )

        assertContainsAll(
            stage330,
            "PERFORMANCE_BETA_PASSED != PERFORMANCE_TARGET_GUARANTEED.",
            "PERFORMANCE_BETA_PASSED != APPLICATION_NEVER_FAILS.",
            "PERFORMANCE_BETA_PASSED != PRODUCTION_READINESS.",
            "PERFORMANCE_BETA_PASSED != AUTHORIZATION.",
            "PERFORMANCE_BETA_PASSED != EXECUTION_APPROVAL.",
            "PERFORMANCE_BETA_PASSED != VERIFIED_OUTCOME.",
            "PERFORMANCE_BETA_EVIDENCE != CONSTITUTIONAL_VERIFICATION.",
            "STAGE_330 != STAGE_331_BETA_DEFECT_CLOSURE.",
            "Stage 330 does not implement Stage 331 Beta Defect Closure.",
        )


        val productionRoot =
            repositoryFile(
                "app/src/main/kotlin",
            )

        assertTrue(
            productionRoot
                .walkTopDown()
                .filter { it.isFile && it.extension == "kt" }
                .none { file ->
                    file.readText()
                        .contains("Stage330PerformanceBeta")
                },
            "Stage 330 must remain test-only and must not create production Beta performance authority.",
        )
    }

    private fun availableLocalModelFoundation():
        LocalModelFoundationResult =
        LocalModelFoundationCoordinator()
            .prepare(
                routing = routedModel(),
                localModelId =
                    "local-model:stage330:performance-beta",
                localModelDescription =
                    "Bounded Stage 330 offline-performance provenance.",
            )

    private fun routedModel(): ModelRoutingResult =
        ModelRoutingCoordinator()
            .route(
                providerArchitecture =
                    availableProviderArchitecture(),
                routingRationale =
                    "Explicit bounded Stage 330 routing destination.",
            )

    private fun availableProviderArchitecture():
        ModelProviderArchitectureResult =
        ModelProviderArchitectureCoordinator()
            .prepare(
                providerId =
                    "provider:stage330:performance-beta",
                providerName =
                    "Stage 330 Performance Beta Test Provider",
                providerDescription =
                    "Provider-neutral Stage 330 performance fixture.",
            )

    private fun availableRecoveryResult():
        AndroidReliabilityRecoveryResult {
        val assessment =
            ReliabilityCoordinator()
                .assess(
                    RecoveryEvidence.create(
                        condition =
                            ReliabilityCondition.FAILED,
                        error =
                            UniversalErrorRecord.create(
                                errorCode =
                                    ErrorCode.from(
                                        "STAGE_330_PERFORMANCE_RECOVERY",
                                    ),
                                traceId =
                                    TraceId.from(
                                        "stage-330-performance-beta-trace",
                                    ),
                                occurredAt =
                                    DevilTimestamp.fromEpochMilliseconds(
                                        1_755_734_400_000L,
                                    ),
                                summary =
                                    "Bounded Stage 330 recovery-readiness evidence.",
                            ),
                        recoveryPathKnown = true,
                    ),
                )

        assertEquals(
            RecoveryDisposition.RECOVERY_ELIGIBLE,
            assessment.disposition,
        )

        return AndroidReliabilityRecoveryCoordinator()
            .prepare(
                assessment = assessment,
                strategy =
                    RecoveryStrategy.REINITIALIZE_COMPONENT,
                attemptBudget =
                    RecoveryAttemptBudget.create(
                        maximumAttempts = 2,
                        attemptsAlreadyUsed = 0,
                    ),
            )
            .also { result ->
                assertEquals(
                    AndroidReliabilityRecoveryStatus.AVAILABLE,
                    result.status,
                )
                assertEquals(
                    RecoveryRequestStatus.AVAILABLE,
                    result.recoveryRequestResult.status,
                )
            }
    }

    private fun assertContainsAll(
        source: String,
        vararg markers: String,
    ) {
        markers.forEach { marker ->
            assertTrue(
                source.contains(marker),
                "Missing Stage 330 performance Beta evidence: $marker",
            )
        }
    }

    private fun source(
        path: String,
    ): String =
        repositoryFile(path)
            .takeIf(File::isFile)
            ?.readText()
            ?: error(
                "Unable to locate repository source for Stage 330: $path",
            )

    private fun repositoryFile(
        path: String,
    ): File =
        listOf(
            File(path),
            File("../$path"),
            File("../../$path"),
        ).firstOrNull { candidate ->
            candidate.exists()
        }
            ?: error(
                "Unable to locate repository path for Stage 330: $path",
            )
}
