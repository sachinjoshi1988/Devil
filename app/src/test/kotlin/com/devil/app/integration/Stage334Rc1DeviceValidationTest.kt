package com.devil.app.integration

import java.io.File
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Stage 334 — RC1 Device Validation.
 *
 * Stage 334 protects the bounded physical-device evidence record for the exact
 * Stage 333 RC1 artifact.
 *
 * This test does not manufacture physical-device evidence. The physical
 * observation is recorded in docs/release/STAGE_334_RC1_DEVICE_VALIDATION.md.
 *
 * RC1_PUBLISHED != RC1_DEVICE_VALIDATED
 * RC1_INSTALLED != RC1_FUNCTIONALLY_VALIDATED
 * RC1_DEVICE_VALIDATED != EVERY_CAPABILITY_VALIDATED
 * RC1_DEVICE_VALIDATED != CONSTITUTIONAL_ACCEPTANCE
 * RC1_DEVICE_VALIDATED != SECURITY_ACCEPTANCE
 * RC1_DEVICE_VALIDATED != PRODUCTION_READINESS
 * RC1_DEVICE_VALIDATED != PRODUCTION_RELEASE
 *
 * ANDROID_PERMISSION != DEVIL_AUTHORIZATION
 * ACCESSIBILITY_ENABLED != DEVIL_AUTHORIZATION
 * ACCESSIBILITY_CONNECTED != EXECUTION_APPROVAL
 * VOICE_INPUT != AUTHENTICATION
 * VOICE_INPUT != AUTHORIZATION
 *
 * OPEN_SETTINGS_REQUESTED != OPEN_SETTINGS_EXECUTED
 * DEFERRED != EXECUTION_SUCCESS
 * DEFERRED != VERIFIED_OUTCOME
 *
 * INSTALLATION_SUCCESS != EXECUTION_APPROVAL
 * DEVICE_OBSERVATION != CONSTITUTIONAL_VERIFICATION
 *
 * STAGE_334 != STAGE_335_RC1_CONSTITUTIONAL_AUDIT
 * STAGE_334 != STAGE_336_RC1_SECURITY_AUDIT
 */
class Stage334Rc1DeviceValidationTest {

    @Test
    fun `Stage 334 remains anchored to the exact Stage 333 RC1 lineage`() {
        val record = validationRecord()
        val build = repositoryFile("app/build.gradle.kts").readText()
        val stage333 =
            repositoryFile(
                "app/src/test/kotlin/com/devil/app/integration/Stage333Rc1ContractTest.kt",
            ).readText()

        assertContainsAll(
            record,
            "d3b9a41c80a354808796398cfdb6cf2e1ee59b06",
            "2784b9ee1dff6db1b1d9452264e1f8e5045296ae",
            "devil-v1.1.0-rc1",
            "33719347053",
            "Stage 334 did not rebuild RC1",
        )

        assertContainsAll(
            build,
            """applicationId = "com.devil.app"""",
            "versionCode = 4",
            """versionName = "1.1.0-rc1"""",
        )

        assertContainsAll(
            stage333,
            "RC1_PUBLISHED != RC1_DEVICE_VALIDATED",
            "STAGE_333 != STAGE_334_RC1_DEVICE_VALIDATION",
        )
    }

    @Test
    fun `Stage 334 records exact RC1 installation integrity and signing evidence`() {
        val record = validationRecord()

        assertContainsAll(
            record,
            "`com.devil.app`",
            "`4`",
            "`1.1.0-rc1`",
            "44ea0e44b54b179ed2f6e9311a38558ac240ab273979097d8a666895f7fbedd7",
            "96a20adba24a79d102a9c7722a761d290f217270a7e415051849f6a60f73177e",
            "CN=Devil V1 Release, OU=Release, O=Devil, C=IN",
            "byte-identical",
            "controlled clean",
        )

        assertContainsAll(
            record,
            "Android Debug",
            "af6613f2c9c50d532ae6d890b9b8e334785c3229154ed43b77609ded7a0d9f25",
            "The installed debug signer did not match the permanent Devil release signer.",
        )
    }

    @Test
    fun `Stage 334 records bounded Redmi Note 12 physical observations`() {
        val record = validationRecord()

        assertContainsAll(
            record,
            "Redmi Note 12",
            "`22111317I`",
            "`14`",
            "successful Devil application launch",
            "successful awakening-to-main-conversation transition",
            "main conversation UI availability",
            "one typed `Hello Devil` submission",
            "`Deferred by the Devil runtime.`",
            "visible speaking presentation",
            "Devil entering its listening presentation",
            "spoken `Hello Devil` being recognized",
            "enabled Devil's",
            "accessibility service through the Android UI",
            "`Open Settings`",
            "no obvious blocker in the bounded recorded smoke path that invalidated RC1",
        )
    }

    @Test
    fun `Stage 334 preserves truthful non success semantics for representative Android request`() {
        val record = validationRecord()

        assertContainsAll(
            record,
            "successful Android Settings execution",
            "was not established",
            "OPEN_SETTINGS_REQUESTED != OPEN_SETTINGS_EXECUTED",
            "DEFERRED != EXECUTION_SUCCESS",
            "DEFERRED != VERIFIED_OUTCOME",
            "Stage 334 does not establish successful execution for the `Open Settings`",
            "request because the observed runtime result remained `DEFERRED`.",
        )

        assertFalse(
            record.contains("OPEN_SETTINGS_EXECUTED = true"),
            "Stage 334 must not manufacture successful Open Settings execution.",
        )

        assertFalse(
            record.contains("DEFERRED = EXECUTION_SUCCESS"),
            "Stage 334 must not rewrite DEFERRED as execution success.",
        )
    }

    @Test
    fun `Stage 334 stops before constitutional security and production acceptance`() {
        val source =
            repositoryFile(
                "app/src/test/kotlin/com/devil/app/integration/Stage334Rc1DeviceValidationTest.kt",
            ).readText()
        val record = validationRecord()

        val requiredBoundaries =
            listOf(
                "RC1_PUBLISHED != RC1_DEVICE_VALIDATED",
                "RC1_INSTALLED != RC1_FUNCTIONALLY_VALIDATED",
                "RC1_DEVICE_VALIDATED != EVERY_CAPABILITY_VALIDATED",
                "RC1_DEVICE_VALIDATED != CONSTITUTIONAL_ACCEPTANCE",
                "RC1_DEVICE_VALIDATED != SECURITY_ACCEPTANCE",
                "RC1_DEVICE_VALIDATED != PRODUCTION_READINESS",
                "RC1_DEVICE_VALIDATED != PRODUCTION_RELEASE",
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
                "STAGE_334 != STAGE_335_RC1_CONSTITUTIONAL_AUDIT",
                "STAGE_334 != STAGE_336_RC1_SECURITY_AUDIT",
            )

        requiredBoundaries.forEach { boundary ->
            assertTrue(
                source.contains(boundary),
                "Stage 334 test source must preserve boundary: $boundary",
            )
            assertTrue(
                record.contains(boundary),
                "Stage 334 validation record must preserve boundary: $boundary",
            )
        }

        assertContainsAll(
            record,
            "Stage 335 remains responsible for the RC1 Constitutional Audit.",
            "Stage 336 remains responsible for the RC1 Security Audit.",
        )

        val importLines =
            source
                .lineSequence()
                .map(String::trim)
                .filter { it.startsWith("import ") }
                .toList()

        assertTrue(
            importLines.none { it.startsWith("import android.") },
            "Stage 334 evidence test must not depend on Android operational APIs: $importLines",
        )

        val productionRoots =
            listOf(
                "app/src/main",
                "core/model/src/main",
                "core/runtime/src/main",
            )
                .map(::repositoryFile)
                .filter(File::exists)

        val forbiddenProductionTerms =
            listOf(
                "Stage334Rc1DeviceValidationCoordinator",
                "Stage334DeviceValidationAuthority",
                "Rc1DeviceValidationAuthority",
            )

        val violations =
            productionRoots
                .flatMap { root ->
                    root.walkTopDown()
                        .filter { it.isFile && it.extension == "kt" }
                        .toList()
                }
                .flatMap { file ->
                    val productionSource = file.readText()
                    forbiddenProductionTerms
                        .filter(productionSource::contains)
                        .map { term -> "${file.path}: $term" }
                }

        assertTrue(
            violations.isEmpty(),
            "Stage 334 must not create production device-validation authority: $violations",
        )
    }

    private fun validationRecord(): String {
        return repositoryFile(
            "docs/release/STAGE_334_RC1_DEVICE_VALIDATION.md",
        ).readText()
    }

    private fun assertContainsAll(
        source: String,
        vararg expected: String,
    ) {
        expected.forEach { text ->
            assertTrue(
                source.contains(text),
                "Expected Stage 334 evidence to contain: $text",
            )
        }
    }

    private fun repositoryFile(path: String): File {
        return File(repositoryRoot(), path)
    }

    private fun repositoryRoot(): File {
        var current =
            File(
                System.getProperty("user.dir")
                    ?: error("JVM user.dir is unavailable."),
            ).absoluteFile

        while (true) {
            val hasSettings =
                File(current, "settings.gradle.kts").isFile ||
                    File(current, "settings.gradle").isFile

            if (hasSettings && File(current, "app").isDirectory) {
                return current
            }

            current =
                current.parentFile
                    ?: error(
                        "Unable to locate Devil repository root from JVM user.dir.",
                    )
        }
    }
}
