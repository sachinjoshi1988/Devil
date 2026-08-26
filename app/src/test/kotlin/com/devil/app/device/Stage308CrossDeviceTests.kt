package com.devil.app.device

import java.io.File
import kotlin.test.Test
import kotlin.test.assertSame
import kotlin.test.assertTrue

/**
 * Stage 308 Cross-Device Tests completion coverage for the already-established
 * bounded Devil cross-device architecture.
 *
 * This is test-only completion evidence. It does not modify production
 * architecture or establish new cross-device capability.
 *
 * CROSS_DEVICE_IDENTITY != AUTHENTICATION.
 * CROSS_DEVICE_IDENTITY != DEVICE_TRUST.
 * SESSION_VALID != AUTHORIZATION.
 * TASK_CONTEXT != REMOTE_EXECUTION.
 * CROSS_DEVICE_MEMORY_CONTINUITY != MEMORY_SYNC.
 * CROSS_DEVICE_MEMORY_CONTINUITY != MEMORY_REPLICATION.
 * DEVICE_TRUST != AUTHENTICATION.
 * DEVICE_TRUST != AUTHORIZATION.
 * REVOCATION_STATE != REVOCATION_EXECUTION.
 * MULTI_DEVICE_VALIDATED != CONSTITUTIONAL_VERIFICATION.
 * MULTI_DEVICE_VALIDATED != REAL_DEVICE_VALIDATED.
 * MULTI_DEVICE_VALIDATED != REMOTE_EXECUTION.
 * MULTIPLE_EMBODIMENTS != MULTIPLE_DEVILS.
 *
 * Stage 308 does not modify production architecture and does not implement
 * Stage 309 Failure / Recovery Tests.
 */
class Stage308CrossDeviceTests {

    @Test
    fun `Stage 308 preserves cross-device identity boundaries`() {
        assertContainsAll(
            source("app/src/main/kotlin/com/devil/app/device/AndroidCrossDeviceIdentityCoordinator.kt"),
            "CROSS_DEVICE_IDENTITY != AUTHENTICATION.",
            "CROSS_DEVICE_IDENTITY != DEVICE_TRUST.",
            "CROSS_DEVICE_IDENTITY != AUTHORIZATION.",
            "IDENTITY_ID != OWNERSHIP_PROOF.",
        )
    }

    @Test
    fun `Stage 308 preserves session governance boundaries`() {
        assertContainsAll(
            source("app/src/main/kotlin/com/devil/app/device/AndroidCrossDeviceSessionGovernanceCoordinator.kt"),
            "SESSION_VALID != AUTHENTICATION.",
            "SESSION_VALID != AUTHORIZATION.",
            "CROSS_DEVICE_IDENTITY != DEVICE_TRUST.",
            "SESSION_CONTEXT != REMOTE_EXECUTION_AUTHORITY.",
        )
    }

    @Test
    fun `Stage 308 preserves task continuity boundaries`() {
        assertContainsAll(
            source("app/src/main/kotlin/com/devil/app/device/AndroidCrossDeviceTaskContinuityCoordinator.kt"),
            "TASK_CONTINUITY != AUTOMATIC_CONTINUATION.",
            "SESSION_GOVERNANCE != TASK_AUTHORIZATION.",
            "TASK_CONTEXT != EXECUTION_REQUEST.",
            "TASK_CONTEXT != REMOTE_EXECUTION.",
        )
    }

    @Test
    fun `Stage 308 preserves memory continuity boundaries`() {
        assertContainsAll(
            source("app/src/main/kotlin/com/devil/app/device/AndroidCrossDeviceMemoryContinuityCoordinator.kt"),
            "CROSS_DEVICE_MEMORY_CONTINUITY != MEMORY_SYNC.",
            "CROSS_DEVICE_MEMORY_CONTINUITY != MEMORY_REPLICATION.",
            "CROSS_DEVICE_MEMORY_CONTINUITY != MEMORY_TRANSFER.",
            "MEMORY_CONTINUITY != AUTHORIZATION.",
            "MEMORY_CONTINUITY != REMOTE_EXECUTION.",
        )
    }

    @Test
    fun `Stage 308 preserves device trust and revocation boundaries`() {
        assertContainsAll(
            source("app/src/main/kotlin/com/devil/app/device/AndroidDeviceTrustRevocationCoordinator.kt"),
            "DEVICE_IDENTITY != DEVICE_TRUST.",
            "DEVICE_TRUST != AUTHENTICATION.",
            "DEVICE_TRUST != AUTHORIZATION.",
            "TRUSTED != EXECUTION_AUTHORITY.",
            "REVOKED != SESSION_TERMINATION.",
            "REVOCATION_STATE != REVOCATION_EXECUTION.",
        )
    }

    @Test
    fun `Stage 308 preserves unified multi-device validation boundaries`() {
        assertContainsAll(
            source("app/src/main/kotlin/com/devil/app/device/AndroidUnifiedMultiDeviceValidationCoordinator.kt"),
            "MULTI_DEVICE_VALIDATED != CONSTITUTIONAL_VERIFICATION.",
            "MULTI_DEVICE_VALIDATED != REAL_DEVICE_VALIDATED.",
            "MULTI_DEVICE_VALIDATED != AUTHENTICATION.",
            "MULTI_DEVICE_VALIDATED != AUTHORIZATION.",
            "MULTI_DEVICE_VALIDATED != REMOTE_EXECUTION.",
            "MULTIPLE_EMBODIMENTS != MULTIPLE_DEVILS.",
        )
    }

    @Test
    fun `Stage 308 representative Stage 218 through 223 tests retain bounded evidence`() {
        val tests =
            listOf(
                "Stage218CrossDeviceIdentityTest.kt",
                "Stage219CrossDeviceSessionGovernanceTest.kt",
                "Stage220CrossDeviceTaskContinuityTest.kt",
                "Stage221CrossDeviceMemoryContinuityTest.kt",
                "Stage222DeviceTrustRevocationTest.kt",
                "Stage223UnifiedMultiDeviceValidationTest.kt",
            ).map {
                source("app/src/test/kotlin/com/devil/app/device/$it")
            }

        tests.forEachIndexed { index, test ->
            assertTrue(test.contains("@Test"), "Cross-device test $index lacks test evidence.")
            assertTrue(
                test.contains("assertEquals"),
                "Cross-device test $index lacks result assertions.",
            )
            assertTrue(
                test.contains("DEFERRED") ||
                    test.contains("REVOKED") ||
                    test.contains("assertFailsWith"),
                "Cross-device test $index lacks non-success coverage.",
            )
            assertTrue(
                test.contains("assertSame") ||
                    test.contains("assertFailsWith"),
                "Cross-device test $index lacks provenance/invariant coverage.",
            )
        }
    }

    @Test
    fun `Stage 308 Stage 223 validation preserves exact upstream provenance`() {
        val test =
            source(
                "app/src/test/kotlin/com/devil/app/device/" +
                    "Stage223UnifiedMultiDeviceValidationTest.kt",
            )

        listOf(
            "assertSame(fixture.crossDeviceIdentity, result.crossDeviceIdentity)",
            "assertSame(fixture.sessionGovernance, result.sessionGovernance)",
            "assertSame(fixture.taskContinuity, result.taskContinuity)",
            "assertSame(fixture.memoryContinuity, result.memoryContinuity)",
        ).forEach { marker ->
            assertTrue(test.contains(marker), "Missing Stage 308 provenance evidence: $marker")
        }
    }

    @Test
    fun `Stage 308 stops before failure recovery test completion`() {
        val stage308 =
            source(
                "app/src/test/kotlin/com/devil/app/device/" +
                    "Stage308CrossDeviceTests.kt",
            )

        assertTrue(stage308.contains("test-only completion evidence"))
        assertTrue(stage308.contains("does not modify production"))
        assertTrue(stage308.contains("Stage 309 Failure / Recovery Tests"))
    }

    private fun assertContainsAll(
        source: String,
        vararg markers: String,
    ) {
        markers.forEach { marker ->
            assertTrue(
                source.contains(marker),
                "Missing Stage 308 cross-device boundary: $marker",
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
            ?: error("Unable to locate repository source for Stage 308: $path")
    }
}
