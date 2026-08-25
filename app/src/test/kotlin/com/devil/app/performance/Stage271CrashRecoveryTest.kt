package com.devil.app.performance

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
import com.devil.core.model.reliability.ReliabilityAssessment
import com.devil.core.model.reliability.ReliabilityCondition
import com.devil.core.model.reliability.ReliabilityCoordinator
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertSame
import kotlin.test.assertTrue

/**
 * Stage 271 Crash Recovery governance tests.
 *
 * Stage 271 evaluates explicit recovery-readiness evidence only.
 *
 * It must not become a retry engine, restart mechanism, recovery executor,
 * scheduler, capability-health authority, or constitutional outcome authority.
 */
class Stage271CrashRecoveryTest {

    @Test
    fun `complete supplied crash recovery evidence becomes recovery ready`() {
        val upstream =
            availableRecoveryResult()

        val evidence =
            DevilCrashRecoveryEvidence(
                reliabilityRecovery = upstream,
                crashFailurePreserved = true,
                boundedRecoveryPathEstablished = true,
                lifecycleReentryPrepared = true,
            )

        val result =
            DevilCrashRecoveryCoordinator()
                .evaluate(evidence)

        assertEquals(
            DevilCrashRecoveryStatus.RECOVERY_READY,
            result.status,
        )

        assertSame(
            evidence,
            result.evidence,
        )

        assertSame(
            upstream,
            result.evidence.reliabilityRecovery,
        )

        assertSame(
            upstream.assessment,
            result.evidence.reliabilityRecovery.assessment,
        )

        assertSame(
            upstream.recoveryRequestResult,
            result.evidence.reliabilityRecovery.recoveryRequestResult,
        )
    }

    @Test
    fun `deferred Stage 194 recovery remains crash recovery deferred`() {
        val assessment =
            healthyAssessment()

        val upstream =
            AndroidReliabilityRecoveryCoordinator()
                .prepare(
                    assessment = assessment,
                    strategy =
                        RecoveryStrategy.REINITIALIZE_COMPONENT,
                    attemptBudget =
                        RecoveryAttemptBudget.create(
                            maximumAttempts = 1,
                        ),
                )

        assertEquals(
            AndroidReliabilityRecoveryStatus.DEFERRED,
            upstream.status,
        )

        val result =
            DevilCrashRecoveryCoordinator()
                .evaluate(
                    DevilCrashRecoveryEvidence(
                        reliabilityRecovery = upstream,
                        crashFailurePreserved = true,
                        boundedRecoveryPathEstablished = true,
                        lifecycleReentryPrepared = true,
                    ),
                )

        assertEquals(
            DevilCrashRecoveryStatus.RECOVERY_DEFERRED,
            result.status,
        )

        assertSame(
            upstream,
            result.evidence.reliabilityRecovery,
        )
    }

    @Test
    fun `incomplete crash recovery preparedness remains deferred`() {
        val upstream =
            availableRecoveryResult()

        val incompleteEvidence =
            listOf(
                DevilCrashRecoveryEvidence(
                    reliabilityRecovery = upstream,
                    crashFailurePreserved = false,
                    boundedRecoveryPathEstablished = true,
                    lifecycleReentryPrepared = true,
                ),
                DevilCrashRecoveryEvidence(
                    reliabilityRecovery = upstream,
                    crashFailurePreserved = true,
                    boundedRecoveryPathEstablished = false,
                    lifecycleReentryPrepared = true,
                ),
                DevilCrashRecoveryEvidence(
                    reliabilityRecovery = upstream,
                    crashFailurePreserved = true,
                    boundedRecoveryPathEstablished = true,
                    lifecycleReentryPrepared = false,
                ),
            )

        incompleteEvidence.forEach { evidence ->
            val result =
                DevilCrashRecoveryCoordinator()
                    .evaluate(evidence)

            assertEquals(
                DevilCrashRecoveryStatus.RECOVERY_DEFERRED,
                result.status,
            )

            assertFalse(
                evidence.isComplete(),
            )
        }
    }

    @Test
    fun `Stage 271 preserves exact Stage 194 and Stage 45 provenance`() {
        val upstream =
            availableRecoveryResult()

        val evidence =
            DevilCrashRecoveryEvidence(
                reliabilityRecovery = upstream,
                crashFailurePreserved = true,
                boundedRecoveryPathEstablished = true,
                lifecycleReentryPrepared = true,
            )

        val result =
            DevilCrashRecoveryCoordinator()
                .evaluate(evidence)

        assertSame(
            upstream,
            result.evidence.reliabilityRecovery,
        )

        assertSame(
            upstream.assessment,
            result.evidence.reliabilityRecovery.assessment,
        )

        assertSame(
            upstream.assessment.evidence,
            result.evidence.reliabilityRecovery.assessment.evidence,
        )

        assertSame(
            upstream.recoveryRequestResult,
            result.evidence.reliabilityRecovery.recoveryRequestResult,
        )

        assertSame(
            upstream.recoveryRequestResult.request,
            result.evidence.reliabilityRecovery.recoveryRequestResult.request,
        )
    }

    @Test
    fun `Stage 271 recovery ready does not claim recovery execution`() {
        val upstream =
            availableRecoveryResult()

        val result =
            DevilCrashRecoveryCoordinator()
                .evaluate(
                    DevilCrashRecoveryEvidence(
                        reliabilityRecovery = upstream,
                        crashFailurePreserved = true,
                        boundedRecoveryPathEstablished = true,
                        lifecycleReentryPrepared = true,
                    ),
                )

        assertEquals(
            DevilCrashRecoveryStatus.RECOVERY_READY,
            result.status,
        )

        assertEquals(
            RecoveryRequestStatus.AVAILABLE,
            result.evidence
                .reliabilityRecovery
                .recoveryRequestResult
                .status,
        )

        assertTrue(
            result.evidence
                .reliabilityRecovery
                .recoveryRequestResult
                .request != null,
        )
    }

    @Test
    fun `Stage 271 preserves crash recovery and constitutional boundaries`() {
        val source =
            stage271Source()

        listOf(
            "CRASH_RECOVERY_READY != RECOVERY_EXECUTED.",
            "CRASH_RECOVERY_READY != PROCESS_RESTARTED.",
            "CRASH_RECOVERY_READY != COMPONENT_REINITIALIZED.",
            "CRASH_RECOVERY_READY != RETRY_EXECUTED.",
            "CRASH_RECOVERY_READY != RECOVERED.",
            "CRASH_RECOVERY_READY != VERIFIED_OUTCOME.",
            "CRASH_RECOVERY_READY != AUTHORIZATION.",
            "CRASH_RECOVERY_READY != EXECUTION_APPROVAL.",
            "RECOVERY_REQUEST_AVAILABLE != RECOVERY_EXECUTED.",
            "RECOVERY_ATTEMPT_BUDGET != RETRY_PERMISSION.",
        ).forEach { boundary ->
            assertTrue(
                source.contains(boundary),
                "Missing Stage 271 boundary: $boundary",
            )
        }
    }

    @Test
    fun `Stage 271 stops before Stage 272 Long Running Stability`() {
        val source =
            stage271Source()

        assertTrue(
            source.contains("Stage 272 Long-Running Stability"),
        )

        assertTrue(
            source.contains(
                "or any later Phase-S reliability behavior.",
            ),
        )
    }

    @Test
    fun `Stage 271 contains no operational crash recovery wiring`() {
        val source =
            stage271Source()
                .replace(Regex("(?s)/\\*.*?\\*/"), "")
                .replace(Regex("(?m)//.*$"), "")

        listOf(
            "WorkManager",
            "JobScheduler",
            "ProcessPhoenix",
            "Runtime.getRuntime().exit",
            "System.exit",
            "killProcess",
            "restartPackage",
            "startService(",
            "startForegroundService(",
            "startActivity(",
            "recreate()",
            "finishAffinity()",
            "RecoveryAttemptCoordinator(",
            "RecoveryVerificationCoordinator(",
        ).forEach { forbidden ->
            assertFalse(
                source.contains(forbidden),
                "Stage 271 must not introduce operational recovery wiring: $forbidden",
            )
        }
    }

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
                                        "STAGE_271_CRASH_FAILURE",
                                    ),
                                traceId =
                                    TraceId.from(
                                        "stage-271-crash-recovery-trace",
                                    ),
                                occurredAt =
                                    DevilTimestamp.fromEpochMilliseconds(
                                        1_755_734_400_000L,
                                    ),
                                summary =
                                    "Bounded Stage 271 crash failure evidence.",
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

    private fun healthyAssessment():
        ReliabilityAssessment {
        return ReliabilityCoordinator()
            .assess(
                RecoveryEvidence.create(
                    condition =
                        ReliabilityCondition.HEALTHY,
                ),
            )
    }

    private fun stage271Source(): String {
        val candidates =
            listOf(
                File(
                    "app/src/main/kotlin/com/devil/app/performance/DevilCrashRecovery.kt",
                ),
                File(
                    "src/main/kotlin/com/devil/app/performance/DevilCrashRecovery.kt",
                ),
            )

        return candidates
            .firstOrNull { it.isFile }
            ?.readText()
            ?: error(
                "Unable to locate Stage 271 source from: ${candidates.joinToString()}",
            )
    }
}
