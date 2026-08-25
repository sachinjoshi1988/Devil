package com.devil.app.performance

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertSame
import kotlin.test.assertTrue

/**
 * Stage 272 Long-Running Stability governance tests.
 *
 * Stage 272 evaluates explicitly supplied lifecycle-stability evidence only.
 *
 * It must not become a resource controller, watchdog, scheduler, retry engine,
 * process-restart mechanism, or constitutional authority.
 */
class Stage272LongRunningStabilityTest {

    @Test
    fun `complete supplied stability evidence becomes stable`() {
        val evidence =
            completeEvidence()

        val result =
            DevilLongRunningStabilityCoordinator()
                .evaluate(
                    evidence = evidence,
                )

        assertEquals(
            DevilLongRunningStabilityStatus.STABLE,
            result.status,
        )

        assertSame(
            evidence,
            result.evidence,
        )
    }

    @Test
    fun `incomplete voice input lifecycle evidence does not establish stability`() {
        assertNotStable(
            completeEvidence().copy(
                voiceInputLifecycleBounded = false,
            ),
        )
    }

    @Test
    fun `incomplete voice output lifecycle evidence does not establish stability`() {
        assertNotStable(
            completeEvidence().copy(
                voiceOutputLifecycleBounded = false,
            ),
        )
    }

    @Test
    fun `incomplete camera lifecycle evidence does not establish stability`() {
        assertNotStable(
            completeEvidence().copy(
                cameraResourceLifecycleBounded = false,
            ),
        )
    }

    @Test
    fun `incomplete network lifecycle evidence does not establish stability`() {
        assertNotStable(
            completeEvidence().copy(
                networkConnectionLifecycleBounded = false,
            ),
        )
    }

    @Test
    fun `uncontrolled background work evidence prevents stable classification`() {
        assertNotStable(
            completeEvidence().copy(
                uncontrolledBackgroundWorkAbsent = false,
            ),
        )
    }

    @Test
    fun `automatic recovery loop evidence prevents stable classification`() {
        assertNotStable(
            completeEvidence().copy(
                automaticRecoveryLoopAbsent = false,
            ),
        )
    }

    @Test
    fun `Stage 272 preserves supplied evidence identity`() {
        val evidence =
            DevilLongRunningStabilityEvidence(
                voiceInputLifecycleBounded = true,
                voiceOutputLifecycleBounded = false,
                cameraResourceLifecycleBounded = true,
                networkConnectionLifecycleBounded = true,
                uncontrolledBackgroundWorkAbsent = true,
                automaticRecoveryLoopAbsent = true,
            )

        val result =
            DevilLongRunningStabilityResult.create(
                evidence = evidence,
            )

        assertSame(
            evidence,
            result.evidence,
        )

        assertEquals(
            DevilLongRunningStabilityStatus.STABILITY_NOT_ESTABLISHED,
            result.status,
        )
    }

    @Test
    fun `Stage 272 complete evidence requires every bounded lifecycle property`() {
        val evidence =
            completeEvidence()

        assertTrue(
            evidence.voiceInputLifecycleBounded,
        )
        assertTrue(
            evidence.voiceOutputLifecycleBounded,
        )
        assertTrue(
            evidence.cameraResourceLifecycleBounded,
        )
        assertTrue(
            evidence.networkConnectionLifecycleBounded,
        )
        assertTrue(
            evidence.uncontrolledBackgroundWorkAbsent,
        )
        assertTrue(
            evidence.automaticRecoveryLoopAbsent,
        )
        assertTrue(
            evidence.isComplete(),
        )
    }

    @Test
    fun `Stage 272 preserves long running stability and constitutional boundaries`() {
        val source =
            stage272Source()

        listOf(
            "LONG_RUNNING_STABLE != APPLICATION_NEVER_CRASHES.",
            "LONG_RUNNING_STABLE != RESOURCE_LEAK_IMPOSSIBLE.",
            "LONG_RUNNING_STABLE != BACKGROUND_EXECUTION_AUTHORIZED.",
            "LONG_RUNNING_STABLE != RECOVERY_EXECUTED.",
            "LONG_RUNNING_STABLE != VERIFIED_OUTCOME.",
            "LONG_RUNNING_STABLE != AUTHORIZATION.",
            "LONG_RUNNING_STABLE != EXECUTION_APPROVAL.",
            "STABILITY_EVIDENCE != CONSTITUTIONAL_VERIFICATION.",
        ).forEach { boundary ->
            assertTrue(
                source.contains(boundary),
                "Missing Stage 272 boundary: $boundary",
            )
        }
    }

    @Test
    fun `Stage 272 stops before Stage 273`() {
        val source =
            stage272Source()

        assertTrue(
            source.contains(
                "or implement Stage 273.",
            ),
        )
    }

    @Test
    fun `Stage 272 contains no operational stability wiring`() {
        val executableSource =
            stage272Source()
                .replace(
                    Regex("(?s)/\\*.*?\\*/"),
                    "",
                )
                .replace(
                    Regex("(?m)//.*$"),
                    "",
                )

        listOf(
            "WorkManager",
            "Worker(",
            "JobScheduler",
            "JobService",
            "AlarmManager",
            "WakeLock",
            "newSingleThreadExecutor",
            "ScheduledExecutor",
            "HandlerThread(",
            "Timer(",
            "while (true)",
            "startService(",
            "startForegroundService(",
            "startActivity(",
            "recreate()",
            "finishAffinity()",
            "killProcess",
            "System.exit",
            "Runtime.getRuntime().exit",
            "RecoveryAttemptCoordinator(",
            "RecoveryVerificationCoordinator(",
            "SpeechRecognizer.",
            "TextToSpeech(",
            "openCamera(",
            "openConnection(",
            "disconnect()",
        ).forEach { forbidden ->
            assertFalse(
                executableSource.contains(forbidden),
                "Stage 272 must not introduce operational stability wiring: $forbidden",
            )
        }
    }

    private fun completeEvidence():
        DevilLongRunningStabilityEvidence =
        DevilLongRunningStabilityEvidence(
            voiceInputLifecycleBounded = true,
            voiceOutputLifecycleBounded = true,
            cameraResourceLifecycleBounded = true,
            networkConnectionLifecycleBounded = true,
            uncontrolledBackgroundWorkAbsent = true,
            automaticRecoveryLoopAbsent = true,
        )

    private fun assertNotStable(
        evidence: DevilLongRunningStabilityEvidence,
    ) {
        val result =
            DevilLongRunningStabilityCoordinator()
                .evaluate(
                    evidence = evidence,
                )

        assertEquals(
            DevilLongRunningStabilityStatus.STABILITY_NOT_ESTABLISHED,
            result.status,
        )

        assertSame(
            evidence,
            result.evidence,
        )

        assertFalse(
            evidence.isComplete(),
        )
    }

    private fun stage272Source(): String {
        val candidates =
            listOf(
                File(
                    "app/src/main/kotlin/com/devil/app/performance/DevilLongRunningStability.kt",
                ),
                File(
                    "src/main/kotlin/com/devil/app/performance/DevilLongRunningStability.kt",
                ),
            )

        return candidates
            .firstOrNull { it.isFile }
            ?.readText()
            ?: error(
                "Unable to locate Stage 272 source from: ${candidates.joinToString()}",
            )
    }
}
