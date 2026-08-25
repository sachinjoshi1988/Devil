package com.devil.app.performance

import com.devil.app.device.AndroidDeviceKnowledgeSnapshot
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertSame

/**
 * Stage 273 Device Compatibility governance tests.
 *
 * Stage 273 evaluates supplied compatibility evidence only.
 *
 * It must not become Android hardware probing, permission authority, execution,
 * production validation, or Stage 274 Redmi Note 12 validation.
 */
class Stage273DeviceCompatibilityTest {

    @Test
    fun `supported device with complete supplied compatibility evidence becomes compatible`() {
        val snapshot =
            compatibleSnapshot()

        val evidence =
            completeEvidence(
                snapshot = snapshot,
            )

        val result =
            DevilDeviceCompatibilityCoordinator()
                .evaluate(
                    evidence = evidence,
                )

        assertEquals(
            DevilDeviceCompatibilityStatus.COMPATIBLE,
            result.status,
        )

        assertSame(
            evidence,
            result.evidence,
        )

        assertSame(
            snapshot,
            result.evidence.deviceSnapshot,
        )
    }

    @Test
    fun `device below minimum SDK remains not compatible`() {
        val snapshot =
            AndroidDeviceKnowledgeSnapshot.create(
                sdkInt = 25,
                androidRelease = "7.1",
                manufacturer = "Test Manufacturer",
                model = "Legacy Device",
                device = "legacy",
                product = "legacy_product",
            )

        val result =
            DevilDeviceCompatibilityCoordinator()
                .evaluate(
                    evidence =
                        completeEvidence(
                            snapshot = snapshot,
                        ),
                )

        assertEquals(
            DevilDeviceCompatibilityStatus.NOT_COMPATIBLE,
            result.status,
        )
    }

    @Test
    fun `missing required capability compatibility remains not compatible`() {
        val snapshot =
            compatibleSnapshot()

        listOf(
            completeEvidence(snapshot).copy(
                requiredAudioCapabilityCompatible = false,
            ),
            completeEvidence(snapshot).copy(
                requiredCameraCapabilityCompatible = false,
            ),
            completeEvidence(snapshot).copy(
                requiredInternetCapabilityCompatible = false,
            ),
        ).forEach { evidence ->
            val result =
                DevilDeviceCompatibilityCoordinator()
                    .evaluate(
                        evidence = evidence,
                    )

            assertEquals(
                DevilDeviceCompatibilityStatus.NOT_COMPATIBLE,
                result.status,
            )
        }
    }

    @Test
    fun `Stage 273 preserves exact Stage 40 device provenance`() {
        val snapshot =
            compatibleSnapshot()

        val evidence =
            completeEvidence(
                snapshot = snapshot,
            )

        val result =
            DevilDeviceCompatibilityResult.create(
                evidence = evidence,
            )

        assertSame(
            evidence,
            result.evidence,
        )

        assertSame(
            snapshot,
            result.evidence.deviceSnapshot,
        )
    }

    @Test
    fun `invalid SDK compatibility envelope is rejected`() {
        val snapshot =
            compatibleSnapshot()

        assertFailsWith<IllegalArgumentException> {
            completeEvidence(
                snapshot = snapshot,
                minimumSupportedSdk = 0,
            )
        }

        assertFailsWith<IllegalArgumentException> {
            completeEvidence(
                snapshot = snapshot,
                minimumSupportedSdk = 35,
                targetSdk = 34,
            )
        }
    }

    @Test
    fun `Stage 273 preserves device compatibility and constitutional boundaries`() {
        val source =
            stage273Source()

        listOf(
            "DEVICE_COMPATIBLE != PRODUCTION_VALIDATED.",
            "DEVICE_COMPATIBLE != REDMI_NOTE_12_VALIDATED.",
            "DEVICE_COMPATIBLE != HARDWARE_FEATURE_GUARANTEED.",
            "DEVICE_COMPATIBLE != ANDROID_PERMISSION_GRANTED.",
            "DEVICE_COMPATIBLE != DEVIL_AUTHORIZATION.",
            "DEVICE_COMPATIBLE != EXECUTION_APPROVAL.",
            "DEVICE_COMPATIBLE != VERIFIED_OUTCOME.",
            "DEVICE_KNOWLEDGE != DEVICE_COMPATIBILITY.",
        ).forEach { boundary ->
            assert(
                source.contains(boundary),
            ) {
                "Missing Stage 273 boundary: $boundary"
            }
        }
    }

    @Test
    fun `Stage 273 stops before Stage 274 Redmi Note 12 Production Validation`() {
        val source =
            stage273Source()

        assert(
            source.contains(
                "Stage 274 Redmi Note 12 Production Validation",
            ),
        )
    }

    @Test
    fun `Stage 273 contains no operational device control or production validation wiring`() {
        val executableSource =
            stage273Source()
                .replace(Regex("(?s)/\\*.*?\\*/"), "")
                .replace(Regex("(?m)//.*$"), "")

        listOf(
            "android.os.Build",
            "Build.VERSION",
            "PackageManager",
            "requestPermissions(",
            "startActivity(",
            "startService(",
            "startForegroundService(",
            "WorkManager",
            "JobScheduler",
            "Settings.",
            "Runtime.getRuntime()",
            "System.exit",
        ).forEach { forbidden ->
            assertFalse(
                executableSource.contains(forbidden),
                "Stage 273 must not introduce operational device compatibility wiring: $forbidden",
            )
        }
    }

    private fun completeEvidence(
        snapshot: AndroidDeviceKnowledgeSnapshot,
        minimumSupportedSdk: Int = 26,
        targetSdk: Int = 35,
    ): DevilDeviceCompatibilityEvidence =
        DevilDeviceCompatibilityEvidence(
            deviceSnapshot = snapshot,
            minimumSupportedSdk = minimumSupportedSdk,
            targetSdk = targetSdk,
            requiredAudioCapabilityCompatible = true,
            requiredCameraCapabilityCompatible = true,
            requiredInternetCapabilityCompatible = true,
        )

    private fun compatibleSnapshot():
        AndroidDeviceKnowledgeSnapshot =
        AndroidDeviceKnowledgeSnapshot.create(
            sdkInt = 34,
            androidRelease = "14",
            manufacturer = "Xiaomi",
            model = "Test Android Device",
            device = "test_device",
            product = "test_product",
        )

    private fun stage273Source(): String {
        val candidates =
            listOf(
                File(
                    "app/src/main/kotlin/com/devil/app/performance/DevilDeviceCompatibility.kt",
                ),
                File(
                    "src/main/kotlin/com/devil/app/performance/DevilDeviceCompatibility.kt",
                ),
            )

        return candidates
            .firstOrNull { it.isFile }
            ?.readText()
            ?: error(
                "Unable to locate Stage 273 source from: ${candidates.joinToString()}",
            )
    }
}
