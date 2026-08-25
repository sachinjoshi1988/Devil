package com.devil.app.performance

import com.devil.app.device.AndroidDeviceKnowledgeSnapshot
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertSame
import kotlin.test.assertTrue

/**
 * Stage 274 Redmi Note 12 Production Validation governance tests.
 *
 * Stage 274 evaluates supplied production-validation evidence only.
 *
 * It must not become an APK builder, installer, device controller, execution
 * authority, constitutional Verification authority, or Stage 275 security work.
 */
class Stage274RedmiNote12ProductionValidationTest {

    @Test
    fun `complete Redmi Note 12 production evidence becomes validated`() {
        val compatibility = compatibleRedmiNote12Result()
        val evidence =
            completeEvidence(
                compatibility = compatibility,
            )

        val result =
            DevilRedmiNote12ProductionValidationCoordinator()
                .evaluate(
                    evidence = evidence,
                )

        assertEquals(
            DevilRedmiNote12ProductionValidationStatus.VALIDATED,
            result.status,
        )
        assertSame(
            evidence,
            result.evidence,
        )
        assertSame(
            compatibility,
            result.evidence.deviceCompatibility,
        )
        assertSame(
            compatibility.evidence.deviceSnapshot,
            result.evidence.deviceCompatibility.evidence.deviceSnapshot,
        )
    }

    @Test
    fun `non compatible Stage 273 result keeps Stage 274 deferred`() {
        val compatibility =
            DevilDeviceCompatibilityCoordinator()
                .evaluate(
                    evidence =
                        DevilDeviceCompatibilityEvidence(
                            deviceSnapshot =
                                redmiNote12Snapshot(
                                    sdkInt = 25,
                                ),
                            minimumSupportedSdk = 26,
                            targetSdk = 35,
                            requiredAudioCapabilityCompatible = true,
                            requiredCameraCapabilityCompatible = true,
                            requiredInternetCapabilityCompatible = true,
                        ),
                )

        assertEquals(
            DevilDeviceCompatibilityStatus.NOT_COMPATIBLE,
            compatibility.status,
        )

        val result =
            DevilRedmiNote12ProductionValidationCoordinator()
                .evaluate(
                    evidence =
                        completeEvidence(
                            compatibility = compatibility,
                        ),
                )

        assertEquals(
            DevilRedmiNote12ProductionValidationStatus.DEFERRED,
            result.status,
        )
        assertSame(
            compatibility,
            result.evidence.deviceCompatibility,
        )
    }

    @Test
    fun `missing Redmi Note 12 identity evidence keeps validation deferred`() {
        val result =
            DevilRedmiNote12ProductionValidationCoordinator()
                .evaluate(
                    evidence =
                        completeEvidence(
                            compatibility = compatibleRedmiNote12Result(),
                        ).copy(
                            redmiNote12IdentityMatched = false,
                        ),
                )

        assertEquals(
            DevilRedmiNote12ProductionValidationStatus.DEFERRED,
            result.status,
        )
    }

    @Test
    fun `missing Android 14 compatibility evidence keeps validation deferred`() {
        val result =
            DevilRedmiNote12ProductionValidationCoordinator()
                .evaluate(
                    evidence =
                        completeEvidence(
                            compatibility = compatibleRedmiNote12Result(),
                        ).copy(
                            android14CompatibilityEstablished = false,
                        ),
                )

        assertEquals(
            DevilRedmiNote12ProductionValidationStatus.DEFERRED,
            result.status,
        )
    }

    @Test
    fun `missing APK build evidence keeps validation deferred`() {
        val result =
            DevilRedmiNote12ProductionValidationCoordinator()
                .evaluate(
                    evidence =
                        completeEvidence(
                            compatibility = compatibleRedmiNote12Result(),
                        ).copy(
                            apkBuildEstablished = false,
                        ),
                )

        assertEquals(
            DevilRedmiNote12ProductionValidationStatus.DEFERRED,
            result.status,
        )
    }

    @Test
    fun `missing APK integrity evidence keeps validation deferred`() {
        val result =
            DevilRedmiNote12ProductionValidationCoordinator()
                .evaluate(
                    evidence =
                        completeEvidence(
                            compatibility = compatibleRedmiNote12Result(),
                        ).copy(
                            apkIntegritySha256Established = false,
                        ),
                )

        assertEquals(
            DevilRedmiNote12ProductionValidationStatus.DEFERRED,
            result.status,
        )
    }

    @Test
    fun `installation without functional observation remains deferred`() {
        val evidence =
            completeEvidence(
                compatibility = compatibleRedmiNote12Result(),
            ).copy(
                realDeviceFunctionalObservationEstablished = false,
            )

        assertTrue(
            evidence.apkInstallationEstablished,
        )

        val result =
            DevilRedmiNote12ProductionValidationCoordinator()
                .evaluate(
                    evidence = evidence,
                )

        assertEquals(
            DevilRedmiNote12ProductionValidationStatus.DEFERRED,
            result.status,
        )
    }

    @Test
    fun `Stage 274 preserves exact Stage 273 and Stage 40 provenance`() {
        val snapshot = redmiNote12Snapshot()

        val compatibility =
            DevilDeviceCompatibilityCoordinator()
                .evaluate(
                    evidence =
                        DevilDeviceCompatibilityEvidence(
                            deviceSnapshot = snapshot,
                            minimumSupportedSdk = 26,
                            targetSdk = 35,
                            requiredAudioCapabilityCompatible = true,
                            requiredCameraCapabilityCompatible = true,
                            requiredInternetCapabilityCompatible = true,
                        ),
                )

        val result =
            DevilRedmiNote12ProductionValidationCoordinator()
                .evaluate(
                    evidence =
                        completeEvidence(
                            compatibility = compatibility,
                        ),
                )

        assertSame(
            compatibility,
            result.evidence.deviceCompatibility,
        )
        assertSame(
            snapshot,
            result.evidence.deviceCompatibility.evidence.deviceSnapshot,
        )
    }

    @Test
    fun `Stage 274 complete evidence requires every bounded production prerequisite`() {
        val complete =
            completeEvidence(
                compatibility = compatibleRedmiNote12Result(),
            )

        assertTrue(
            complete.isComplete(),
        )

        listOf(
            complete.copy(redmiNote12IdentityMatched = false),
            complete.copy(android14CompatibilityEstablished = false),
            complete.copy(apkBuildEstablished = false),
            complete.copy(apkIntegritySha256Established = false),
            complete.copy(apkInstallationEstablished = false),
            complete.copy(realDeviceFunctionalObservationEstablished = false),
        ).forEach { incomplete ->
            assertFalse(
                incomplete.isComplete(),
            )
        }
    }

    @Test
    fun `Stage 274 preserves production validation and constitutional boundaries`() {
        val source = stage274Source()

        listOf(
            "REDMI_NOTE_12_PRODUCTION_VALIDATED != DEVIL_PRODUCTION_READY.",
            "REDMI_NOTE_12_PRODUCTION_VALIDATED != CONSTITUTIONAL_VERIFICATION.",
            "REDMI_NOTE_12_PRODUCTION_VALIDATED != SECURITY_VALIDATED.",
            "REDMI_NOTE_12_PRODUCTION_VALIDATED != AUTHORIZATION.",
            "REDMI_NOTE_12_PRODUCTION_VALIDATED != EXECUTION_APPROVAL.",
            "REDMI_NOTE_12_PRODUCTION_VALIDATED != VERIFIED_OUTCOME.",
            "APK_BUILT != APK_INSTALLED.",
            "APK_INSTALLED != FUNCTIONALLY_VALIDATED.",
            "DEVICE_OBSERVATION != CONSTITUTIONAL_VERIFICATION.",
        ).forEach { boundary ->
            assertTrue(
                source.contains(boundary),
                "Missing Stage 274 boundary: $boundary",
            )
        }
    }

    @Test
    fun `Stage 274 stops before Stage 275 Full Threat Model`() {
        val source = stage274Source()

        assertTrue(
            source.contains(
                "Stage 275 Full Threat Model",
            ),
        )
    }

    @Test
    fun `Stage 274 contains no operational APK device or security wiring`() {
        val executableSource =
            stage274Source()
                .replace(
                    Regex("(?s)/\\*.*?\\*/"),
                    "",
                )
                .replace(
                    Regex("(?m)//.*$"),
                    "",
                )

        listOf(
            "Runtime.getRuntime()",
            "ProcessBuilder(",
            "PackageInstaller",
            "PackageManager",
            "Build.",
            "MessageDigest",
            "FileInputStream",
            "startActivity(",
            "startService(",
            "startForegroundService(",
            "requestPermissions(",
            "ActivityCompat.requestPermissions",
        ).forEach { forbidden ->
            assertFalse(
                executableSource.contains(forbidden),
                "Stage 274 must not introduce operational production-validation wiring: $forbidden",
            )
        }
    }

    private fun completeEvidence(
        compatibility: DevilDeviceCompatibilityResult,
    ): DevilRedmiNote12ProductionValidationEvidence =
        DevilRedmiNote12ProductionValidationEvidence(
            deviceCompatibility = compatibility,
            redmiNote12IdentityMatched = true,
            android14CompatibilityEstablished = true,
            apkBuildEstablished = true,
            apkIntegritySha256Established = true,
            apkInstallationEstablished = true,
            realDeviceFunctionalObservationEstablished = true,
        )

    private fun compatibleRedmiNote12Result():
        DevilDeviceCompatibilityResult =
        DevilDeviceCompatibilityCoordinator()
            .evaluate(
                evidence =
                    DevilDeviceCompatibilityEvidence(
                        deviceSnapshot = redmiNote12Snapshot(),
                        minimumSupportedSdk = 26,
                        targetSdk = 35,
                        requiredAudioCapabilityCompatible = true,
                        requiredCameraCapabilityCompatible = true,
                        requiredInternetCapabilityCompatible = true,
                    ),
            )
            .also { result ->
                assertEquals(
                    DevilDeviceCompatibilityStatus.COMPATIBLE,
                    result.status,
                )
            }

    private fun redmiNote12Snapshot(
        sdkInt: Int = 34,
    ): AndroidDeviceKnowledgeSnapshot =
        AndroidDeviceKnowledgeSnapshot.create(
            sdkInt = sdkInt,
            androidRelease = "14",
            manufacturer = "Xiaomi",
            model = "22111317I",
            device = "topaz",
            product = "topaz_in",
        )

    private fun stage274Source(): String {
        val candidates =
            listOf(
                File(
                    "app/src/main/kotlin/com/devil/app/performance/DevilRedmiNote12ProductionValidation.kt",
                ),
                File(
                    "src/main/kotlin/com/devil/app/performance/DevilRedmiNote12ProductionValidation.kt",
                ),
            )

        return candidates
            .firstOrNull { it.isFile }
            ?.readText()
            ?: error(
                "Unable to locate Stage 274 source from: ${candidates.joinToString()}",
            )
    }
}
