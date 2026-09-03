package com.devil.app.security

import java.io.File
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Stage 336 — RC1 Security Audit.
 *
 * Evidence-only RC1 security acceptance over already-established security
 * architecture and test evidence.
 *
 * RC1_SECURITY_AUDIT != SECURITY_AUTHORITY.
 * RC1_SECURITY_AUDIT != AUTHENTICATION.
 * RC1_SECURITY_AUDIT != AUTHORIZATION.
 * RC1_SECURITY_AUDIT != OWNER_MODE.
 * RC1_SECURITY_AUDIT != HIGH_SECURITY_CONFIRMATION.
 * RC1_SECURITY_AUDIT != EXECUTION_APPROVAL.
 * RC1_SECURITY_AUDIT != EXECUTION.
 * RC1_SECURITY_AUDIT != CONSTITUTIONAL_VERIFICATION.
 * RC1_SECURITY_AUDIT != VERIFIED_OUTCOME.
 *
 * RC1_SECURITY_ACCEPTANCE != ATTACK_PREVENTION.
 * RC1_SECURITY_ACCEPTANCE != SECURITY_INCIDENT_ABSENT.
 * RC1_SECURITY_ACCEPTANCE != PRODUCTION_READINESS.
 * RC1_SECURITY_ACCEPTANCE != PRODUCTION_RELEASE.
 *
 * STAGE_336 != STAGE_337_EDUCATION_CHILD_AUDIT.
 */
class Stage336Rc1SecurityAuditTest {

    @Test
    fun `Stage 336 preserves exact RC1 Stage 334 and Stage 335 provenance`() {
        val audit =
            readRepositoryFile(
                "docs/release/STAGE_336_RC1_SECURITY_AUDIT.md",
            )

        val stage334 =
            readRepositoryFile(
                "docs/release/STAGE_334_RC1_DEVICE_VALIDATION.md",
            )

        val stage335 =
            readRepositoryFile(
                "docs/release/STAGE_335_RC1_CONSTITUTIONAL_AUDIT.md",
            )

        val requiredAuditProvenance =
            listOf(
                "2784b9ee1dff6db1b1d9452264e1f8e5045296ae",
                "devil-v1.1.0-rc1",
                "44ea0e44b54b179ed2f6e9311a38558ac240ab273979097d8a666895f7fbedd7",
                "de5539068df8bdaa9ad85ce49cdbcc85c9210484",
                "8ec4527b297f3e07eb267e431e6bb8d73b7b38d5",
                "devil-stage-335-complete",
            )

        requiredAuditProvenance.forEach { required ->
            assertTrue(
                audit.contains(required),
                "Missing Stage 336 RC1 provenance: $required",
            )
        }

        assertTrue(
            stage334.contains(
                "RC1_DEVICE_VALIDATED != SECURITY_ACCEPTANCE",
            ),
        )

        assertTrue(
            stage335.contains(
                "RC1_CONSTITUTIONAL_AUDIT != SECURITY_ACCEPTANCE.",
            ),
        )

        assertTrue(
            stage335.contains(
                "Security acceptance remains owned by Stage 336.",
            ),
        )

        assertTrue(
            audit.contains(
                "Stage 336 does not rebuild the RC1 APK",
            ),
        )

        assertFalse(
            audit.contains(
                "RC1_PUBLISHED = RC1_SECURITY_ACCEPTANCE",
            ),
        )
    }

    @Test
    fun `Stage 336 preserves Stage 275 through 285 security hardening continuity`() {
        val expectations =
            linkedMapOf(
                "app/src/test/kotlin/com/devil/app/securityhardening/Stage275FullThreatModelTest.kt" to
                    "THREAT_MODEL != SECURITY_VALIDATION.",
                "app/src/test/kotlin/com/devil/app/securityhardening/Stage276AuthenticationHardeningTest.kt" to
                    "AUTHENTICATION_HARDENED != AUTHENTICATED.",
                "app/src/test/kotlin/com/devil/app/securityhardening/Stage277SessionHardeningTest.kt" to
                    "SESSION_VALID != AUTHORIZATION.",
                "app/src/test/kotlin/com/devil/app/securityhardening/Stage278CapabilityAuthorizationHardeningTest.kt" to
                    "ANDROID_PERMISSION_GRANTED != DEVIL_AUTHORIZATION.",
                "app/src/test/kotlin/com/devil/app/securityhardening/Stage279DataProtectionTest.kt" to
                    "DATA_PROTECTION != MEMORY_SECURITY.",
                "app/src/test/kotlin/com/devil/app/securityhardening/Stage280MemorySecurityTest.kt" to
                    "MEMORY_AUTHORITY_APPROVAL != MEMORY_COMMITMENT.",
                "app/src/test/kotlin/com/devil/app/securityhardening/Stage281ChildGuardianSecurityAuditTest.kt" to
                    "guardianApprovalSeparatedFromDevilAuthorization",
                "app/src/test/kotlin/com/devil/app/securityhardening/Stage282FinanceLegalSecurityAuditTest.kt" to
                    "FINANCIAL_SAFETY_VERIFICATION != EXECUTION_AUTHORIZATION.",
                "app/src/test/kotlin/com/devil/app/securityhardening/Stage283PromptModelAttackResistanceTest.kt" to
                    "MODEL_TOOL_INTENT != AUTHORIZATION.",
                "app/src/test/kotlin/com/devil/app/securityhardening/Stage284SecurityRegressionSuiteTest.kt" to
                    "SECURITY_REGRESSION_COVERED != ATTACK_PREVENTED.",
                "app/src/test/kotlin/com/devil/app/securityhardening/Stage285FinalSecurityReviewTest.kt" to
                    "FINAL_SECURITY_REVIEW != PRODUCTION_SECURITY_ACCEPTANCE.",
            )

        expectations.forEach { (path, marker) ->
            val source = readRepositoryFile(path)

            assertTrue(
                source.contains(marker),
                "Missing Stage 336 upstream security marker in $path: $marker",
            )
        }

        val audit =
            readRepositoryFile(
                "docs/release/STAGE_336_RC1_SECURITY_AUDIT.md",
            )

        listOf(
            "THREAT_MODEL != SECURITY_VALIDATION.",
            "AUTHENTICATION_HARDENED != AUTHENTICATED.",
            "SESSION_VALID != AUTHORIZATION.",
            "ANDROID_PERMISSION_GRANTED != DEVIL_AUTHORIZATION.",
            "DATA_PROTECTION != MEMORY_SECURITY.",
            "MEMORY_AUTHORITY_APPROVAL != MEMORY_COMMITMENT.",
            "FINANCIAL_SAFETY_VERIFICATION != EXECUTION_AUTHORIZATION.",
            "MODEL_TOOL_INTENT != AUTHORIZATION.",
            "SECURITY_REGRESSION_COVERED != ATTACK_PREVENTED.",
            "FINAL_SECURITY_REVIEW != PRODUCTION_SECURITY_ACCEPTANCE.",
        ).forEach { boundary ->
            assertTrue(
                audit.contains(boundary),
                "Stage 336 audit lost upstream boundary: $boundary",
            )
        }
    }

    @Test
    fun `Stage 336 preserves Security Authority security test and penetration boundaries`() {
        val stage290 =
            readRepositoryFile(
                "app/src/test/kotlin/com/devil/app/constitutionalvalidation/Stage290SecurityAuthorityValidationTest.kt",
            )

        val stage304 =
            readRepositoryFile(
                "app/src/test/kotlin/com/devil/app/security/Stage304SecurityTests.kt",
            )

        val stage329 =
            readRepositoryFile(
                "app/src/test/kotlin/com/devil/app/security/Stage329SecurityPenetrationTestingTest.kt",
            )

        assertTrue(
            stage290.contains(
                "SECURITY_AUTHORITY_VALIDATION != AUTHORIZATION.",
            ),
        )

        listOf(
            "TRUST != AUTHENTICATION.",
            "ANDROID_PERMISSION_GRANTED != DEVIL_AUTHORIZATION.",
            "SESSION_VALID != AUTHORIZATION.",
            "FINAL_SECURITY_REVIEW != PRODUCTION_SECURITY_ACCEPTANCE.",
        ).forEach { marker ->
            assertTrue(
                stage304.contains(marker),
                "Missing Stage 304 security boundary: $marker",
            )
        }

        listOf(
            "PENETRATION_TEST_PASSED != ATTACK_PREVENTION.",
            "PENETRATION_TEST_PASSED != SECURITY_INCIDENT_ABSENT.",
            "PENETRATION_TEST_PASSED != AUTHENTICATION.",
            "PENETRATION_TEST_PASSED != AUTHORIZATION.",
            "PENETRATION_TEST_PASSED != OWNER_MODE.",
            "PENETRATION_TEST_PASSED != EXECUTION_APPROVAL.",
            "PENETRATION_TEST_PASSED != CONSTITUTIONAL_VERIFICATION.",
            "PENETRATION_TEST_PASSED != PRODUCTION_SECURITY_ACCEPTANCE.",
        ).forEach { marker ->
            assertTrue(
                stage329.contains(marker),
                "Missing Stage 329 penetration boundary: $marker",
            )
        }

        val audit =
            readRepositoryFile(
                "docs/release/STAGE_336_RC1_SECURITY_AUDIT.md",
            )

        listOf(
            "RC1_SECURITY_AUDIT != SECURITY_AUTHORITY.",
            "RC1_SECURITY_AUDIT != AUTHENTICATION.",
            "RC1_SECURITY_AUDIT != AUTHORIZATION.",
            "RC1_SECURITY_AUDIT != OWNER_MODE.",
            "RC1_SECURITY_AUDIT != HIGH_SECURITY_CONFIRMATION.",
            "RC1_SECURITY_AUDIT != EXECUTION_APPROVAL.",
            "RC1_SECURITY_AUDIT != EXECUTION.",
            "RC1_SECURITY_AUDIT != CONSTITUTIONAL_VERIFICATION.",
            "RC1_SECURITY_AUDIT != VERIFIED_OUTCOME.",
            "RC1_SECURITY_ACCEPTANCE != ATTACK_PREVENTION.",
            "RC1_SECURITY_ACCEPTANCE != SECURITY_INCIDENT_ABSENT.",
        ).forEach { boundary ->
            assertTrue(
                audit.contains(boundary),
                "Missing Stage 336 security non-authority boundary: $boundary",
            )
        }
    }

    @Test
    fun `Stage 336 does not reinterpret RC1 device evidence as security authority or success`() {
        val stage334 =
            readRepositoryFile(
                "docs/release/STAGE_334_RC1_DEVICE_VALIDATION.md",
            )

        val audit =
            readRepositoryFile(
                "docs/release/STAGE_336_RC1_SECURITY_AUDIT.md",
            )

        val deviceBoundaries =
            listOf(
                "ANDROID_PERMISSION != DEVIL_AUTHORIZATION",
                "ACCESSIBILITY_ENABLED != DEVIL_AUTHORIZATION",
                "ACCESSIBILITY_CONNECTED != EXECUTION_APPROVAL",
                "VOICE_INPUT != AUTHENTICATION",
                "VOICE_INPUT != AUTHORIZATION",
                "OPEN_SETTINGS_REQUESTED != OPEN_SETTINGS_EXECUTED",
                "DEFERRED != EXECUTION_SUCCESS",
                "DEFERRED != VERIFIED_OUTCOME",
                "INSTALLATION_SUCCESS != EXECUTION_APPROVAL",
                "DEVICE_OBSERVATION != CONSTITUTIONAL_VERIFICATION",
            )

        deviceBoundaries.forEach { boundary ->
            assertTrue(
                stage334.contains(boundary),
                "Stage 334 lost device boundary: $boundary",
            )

            assertTrue(
                audit.contains("$boundary."),
                "Stage 336 lost device security boundary: $boundary",
            )
        }

        assertFalse(
            audit.contains(
                "ACCESSIBILITY_ENABLED = DEVIL_AUTHORIZATION",
            ),
        )

        assertFalse(
            audit.contains(
                "VOICE_INPUT = AUTHENTICATION",
            ),
        )

        assertFalse(
            audit.contains(
                "DEFERRED = EXECUTION_SUCCESS",
            ),
        )

        assertFalse(
            audit.contains(
                "DEVICE_OBSERVATION = CONSTITUTIONAL_VERIFICATION",
            ),
        )
    }

    @Test
    fun `Stage 336 records bounded RC1 security acceptance only and creates no operational authority`() {
        val audit =
            readRepositoryFile(
                "docs/release/STAGE_336_RC1_SECURITY_AUDIT.md",
            )

        assertTrue(
            audit.contains(
                "Stage 336 records bounded RC1 security acceptance for the audited",
            ),
        )

        listOf(
            "RC1_SECURITY_ACCEPTANCE != ATTACK_PREVENTION.",
            "RC1_SECURITY_ACCEPTANCE != SECURITY_INCIDENT_ABSENT.",
            "RC1_SECURITY_ACCEPTANCE != AUTHENTICATION.",
            "RC1_SECURITY_ACCEPTANCE != AUTHORIZATION.",
            "RC1_SECURITY_ACCEPTANCE != EXECUTION_APPROVAL.",
            "RC1_SECURITY_ACCEPTANCE != VERIFIED_OUTCOME.",
            "RC1_SECURITY_ACCEPTANCE != PRODUCTION_READINESS.",
            "RC1_SECURITY_ACCEPTANCE != PRODUCTION_RELEASE.",
            "STAGE_336 != STAGE_337_EDUCATION_CHILD_AUDIT.",
        ).forEach { boundary ->
            assertTrue(
                audit.contains(boundary),
                "Missing Stage 336 acceptance boundary: $boundary",
            )
        }

        assertTrue(
            audit.contains(
                "Stage 336 is evidence-only.",
            ),
        )

        assertTrue(
            audit.contains(
                "Stage 337 remains responsible for the RC1 Education / Child Audit.",
            ),
        )

        val productionRoots =
            listOf(
                repositoryPath("app/src/main"),
                repositoryPath("core/model/src/main"),
                repositoryPath("core/runtime/src/main"),
            )

        val violations =
            mutableListOf<String>()

        productionRoots
            .filter(File::exists)
            .forEach { root ->
                root.walkTopDown()
                    .filter { file ->
                        file.isFile &&
                            (
                                file.extension == "kt" ||
                                    file.extension == "kts"
                            )
                    }
                    .forEach { file ->
                        val source = file.readText()

                        if (
                            source.contains("Stage336") ||
                            source.contains("Stage 336")
                        ) {
                            violations += file.relativeTo(repositoryRoot()).path
                        }
                    }
            }

        assertTrue(
            violations.isEmpty(),
            "Stage 336 must not introduce production operational authority: $violations",
        )

        assertFalse(
            audit.contains(
                "RC1_SECURITY_ACCEPTANCE = PRODUCTION_READINESS",
            ),
        )

        assertFalse(
            audit.contains(
                "RC1_SECURITY_ACCEPTANCE = PRODUCTION_RELEASE",
            ),
        )
    }

    private fun readRepositoryFile(path: String): String =
        repositoryFile(path).readText()

    private fun repositoryFile(path: String): File {
        val file = File(repositoryRoot(), path)

        check(file.isFile) {
            "Required repository evidence file does not exist: $path"
        }

        return file
    }

    private fun repositoryPath(path: String): File =
        File(repositoryRoot(), path)

    private fun repositoryRoot(): File {
        var current: File? =
            File(checkNotNull(System.getProperty("user.dir")) { "JVM user.dir system property is unavailable." })
                .absoluteFile

        while (current != null) {
            if (
                File(current, "settings.gradle.kts").isFile ||
                File(current, "settings.gradle").isFile
            ) {
                return current
            }

            current = current.parentFile
        }

        error(
            "Unable to locate Devil repository root from ${System.getProperty("user.dir")}",
        )
    }
}
