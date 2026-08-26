package com.devil.app.reliability

import java.io.File
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Stage 309 Failure / Recovery Tests completion coverage for the already-established
 * bounded Devil failure and recovery architecture.
 *
 * This is test-only completion evidence. It does not modify production architecture
 * or establish new recovery capability.
 *
 * RECOVERY_ELIGIBLE != RETRY_AUTHORIZED.
 * RECOVERY_REQUEST != RECOVERY_EXECUTED.
 * RECOVERY_RECORDED != RECOVERED.
 * VERIFIED_FAILURE != FAILURE_LEARNING.
 * CRASH_RECOVERY_READY != RECOVERY_EXECUTED.
 * RECOVERY_ATTEMPT_BUDGET != RETRY_PERMISSION.
 * MODEL_FAILURE_RECOVERY_PREPARED != RECOVERY_SUCCESS.
 *
 * Recovery verification remains separate from constitutional Verification.
 *
 * Stage 309 does not execute retries, restart processes, reconnect networks,
 * invoke models, establish authorization, or create constitutional Verification.
 *
 * It does not modify production architecture and does not implement
 * Stage 310 Long-Duration Reliability Tests.
 */
class Stage309FailureRecoveryTests {

    @Test
    fun `Stage 309 preserves Android reliability recovery boundaries`() {
        assertContainsAll(
            source(
                "app/src/main/kotlin/com/devil/app/reliability/" +
                    "AndroidReliabilityRecoveryCoordinator.kt",
            ),
            "RECOVERY_ELIGIBLE != RETRY_AUTHORIZED.",
            "RECOVERY_REQUEST != RECOVERY_EXECUTED.",
            "RECOVERY_RECORDED != RECOVERED.",
            "establish constitutional Verification or Outcome",
        )
    }

    @Test
    fun `Stage 309 preserves crash recovery non execution boundaries`() {
        assertContainsAll(
            source(
                "app/src/main/kotlin/com/devil/app/performance/" +
                    "DevilCrashRecovery.kt",
            ),
            "CRASH_RECOVERY_READY != RECOVERY_EXECUTED.",
            "CRASH_RECOVERY_READY != PROCESS_RESTARTED.",
            "CRASH_RECOVERY_READY != COMPONENT_REINITIALIZED.",
            "CRASH_RECOVERY_READY != RETRY_EXECUTED.",
            "CRASH_RECOVERY_READY != RECOVERED.",
            "CRASH_RECOVERY_READY != VERIFIED_OUTCOME.",
            "RECOVERY_ATTEMPT_BUDGET != RETRY_PERMISSION.",
        )
    }

    @Test
    fun `Stage 309 preserves network reliability non retry boundaries`() {
        assertContainsAll(
            source(
                "app/src/main/kotlin/com/devil/app/performance/" +
                    "DevilNetworkReliability.kt",
            ),
            "NETWORK_RELIABLE != RETRY_AUTHORIZED.",
            "NETWORK_RELIABLE != RECONNECTED.",
            "NETWORK_RELIABILITY != VERIFIED_OUTCOME.",
            "FAILED_RETRIEVAL != RETRY_EXECUTED.",
        )
    }

    @Test
    fun `Stage 309 preserves model failure recovery preparation boundaries`() {
        assertContainsAll(
            source(
                "core/runtime/src/main/kotlin/com/devil/core/runtime/" +
                    "modelprovider/ModelFailureRecoveryCoordinator.kt",
            ),
            "MODEL_FAILURE_RECOVERY_PREPARED != RECOVERY_REQUEST.",
            "MODEL_FAILURE_RECOVERY_PREPARED != RECOVERY_EXECUTED.",
            "MODEL_FAILURE_RECOVERY_PREPARED != RETRY_STARTED.",
            "MODEL_FAILURE_RECOVERY_PREPARED != RECOVERY_SUCCESS.",
            "MODEL_FAILURE_RECOVERY_PREPARED != AUTHORIZATION.",
            "MODEL_FAILURE_RECOVERY_PREPARED != EXECUTION.",
        )
    }

    @Test
    fun `Stage 309 preserves failure learning separation`() {
        assertContainsAll(
            source(
                "core/runtime/src/main/kotlin/com/devil/core/runtime/" +
                    "learning/FailureLearningCoordinator.kt",
            ),
            "OPERATIONAL_FAILURE != VERIFIED_OUTCOME_FAILURE.",
            "VERIFIED_FAILURE != FAILURE_LEARNING.",
            "FAILURE_LEARNING_PREPARATION != STRATEGY_ADAPTATION.",
            "FAILURE_LEARNING != MEMORY_PROPOSAL.",
            "FAILURE_LEARNING != CONTROLLED_AUTONOMY.",
        )
    }

    @Test
    fun `Stage 309 preserves recovery verification as non constitutional authority`() {
        val recoveryVerification =
            source(
                "core/model/src/main/kotlin/com/devil/core/model/reliability/" +
                    "RecoveryVerificationCoordinator.kt",
            )

        assertContainsAll(
            recoveryVerification,
            "This coordinator is not the constitutional Verification Authority.",
            "It does not retry, restart, reconnect, execute",
        )
    }

    @Test
    fun `Stage 309 representative recovery tests retain non success and provenance evidence`() {
        val tests =
            listOf(
                "app/src/test/kotlin/com/devil/app/reliability/" +
                    "Stage194AndroidReliabilityRecoveryTest.kt",
                "app/src/test/kotlin/com/devil/app/performance/" +
                    "Stage269NetworkReliabilityTest.kt",
                "app/src/test/kotlin/com/devil/app/performance/" +
                    "Stage271CrashRecoveryTest.kt",
                "core/runtime/src/test/kotlin/com/devil/core/runtime/modelprovider/" +
                    "Stage243BModelFailureRecoveryTest.kt",
                "core/runtime/src/test/kotlin/com/devil/core/runtime/learning/" +
                    "Stage93FailureLearningFoundationGovernanceTest.kt",
                "core/model/src/test/kotlin/com/devil/core/model/reliability/" +
                    "RecoveryVerificationCoordinatorTest.kt",
            ).map(::source)

        tests.forEachIndexed { index, test ->
            assertTrue(
                test.contains("@Test"),
                "Stage 309 representative test $index lacks test evidence.",
            )

            assertTrue(
                test.contains("DEFERRED") ||
                    test.contains("FAILED") ||
                    test.contains("NOT_RECOVERED") ||
                    test.contains("assertFalse"),
                "Stage 309 representative test $index lacks non-success coverage.",
            )

            assertTrue(
                test.contains("assertSame") ||
                    test.contains("assertEquals") ||
                    test.contains("assertFailsWith"),
                "Stage 309 representative test $index lacks provenance/invariant coverage.",
            )
        }
    }

    @Test
    fun `Stage 309 completion remains test only and stops before Stage 310`() {
        val stage309 =
            source(
                "app/src/test/kotlin/com/devil/app/reliability/" +
                    "Stage309FailureRecoveryTests.kt",
            )

        assertContainsAll(
            stage309,
            "This is test-only completion evidence.",
            "does not modify production architecture",
            "does not execute retries",
            "Stage 310 Long-Duration Reliability Tests",
        )

        assertFalse(
            stage309.contains("class Stage309FailureRecovery" + "Coordinator"),
        )
    }

    private fun assertContainsAll(
        source: String,
        vararg markers: String,
    ) {
        markers.forEach { marker ->
            assertTrue(
                source.contains(marker),
                "Missing Stage 309 failure/recovery boundary: $marker",
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
            ?: error("Unable to locate repository source for Stage 309: $path")
    }
}
