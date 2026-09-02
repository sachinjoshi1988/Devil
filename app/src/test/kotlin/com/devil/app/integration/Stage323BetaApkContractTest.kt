package com.devil.app.integration

import java.io.File
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Stage 323 — Beta APK completion-contract evidence.
 *
 * Stage 323 establishes a dedicated controlled Beta APK release path from the
 * frozen Stage 322 architecture.
 *
 * The Beta artifact remains a debug-signed pre-production test artifact.
 * Stage 323 does not create production architecture, constitutional authority,
 * execution authority, authorization, persistence, scheduling, background
 * execution, Observation, Verification, Outcome, RC, or production status.
 *
 * Semantic locks:
 *
 * BETA_APK != RELEASE_CANDIDATE.
 * BETA_APK != PRODUCTION_APK.
 * BETA_APK != PRODUCTION_SIGNED_APK.
 * APK_BUILT != APK_INSTALLED.
 * APK_PUBLISHED != DEVICE_VALIDATED.
 * APK_CHECKSUM_VERIFIED != FUNCTIONALLY_VALIDATED.
 * BETA_DISTRIBUTION != NEW_AUTHORITY.
 * STAGE_323 != STAGE_324_EXTENDED_DEVICE_TESTING.
 *
 * Stage 324 remains authoritative for extended physical-device testing.
 */
class Stage323BetaApkContractTest {

    @Test
    fun `Stage 323 retains the frozen Stage 322 Beta architecture boundary`() {
        val stage322 =
            source(
                "app/src/test/kotlin/com/devil/app/constitutionalvalidation/" +
                    "Stage322BetaArchitectureFreezeTest.kt",
            )

        assertContainsAll(
            stage322,
            "BETA_ARCHITECTURE_FREEZE != BETA_APK.",
            "EXISTING_BETA_RELEASE_INFRASTRUCTURE != STAGE_323_BETA_APK_COMPLETION.",
            "does not implement Stage 323 Beta APK or later Beta-stage behavior",
        )
    }

    @Test
    fun `Stage 323 has a dedicated Beta APK release identity`() {
        val workflow = source(".github/workflows/beta-apk.yml")

        assertContainsAll(
            workflow,
            "name: Devil Beta APK",
            "workflow_dispatch:",
            "name: Build and Release Beta APK",
            "release/devil-beta.apk",
            "devil-beta.apk.sha256",
            "RELEASE_TAG: devil-beta-\${{ github.run_number }}",
            "--title \"Devil Beta \${{ github.run_number }}\"",
            "--prerelease",
        )

        assertFalse(
            workflow.contains("devil-closed-beta.apk"),
            "Stage 323 must not masquerade as the historical Stage 52 Closed Beta artifact.",
        )

        assertFalse(
            workflow.contains("devil-v1-rc1.apk"),
            "Stage 323 must not masquerade as RC1.",
        )

        assertFalse(
            workflow.contains("devil-v1.0.0.apk"),
            "Stage 323 must not masquerade as the production APK.",
        )
    }

    @Test
    fun `Stage 323 Beta APK workflow preserves required build and integrity gates`() {
        val workflow = source(".github/workflows/beta-apk.yml")

        assertContainsAll(
            workflow,
            "actions/checkout@v4",
            "actions/setup-java@v4",
            "distribution: temurin",
            "java-version: \"17\"",
            "./gradlew --no-daemon :core:model:test",
            "./gradlew --no-daemon :core:runtime:test",
            "./gradlew --no-daemon :app:testDebugUnitTest",
            "./gradlew --no-daemon :app:assembleDebug",
            "app/build/outputs/apk/debug/app-debug.apk",
            "sha256sum devil-beta.apk",
            "sha256sum --check devil-beta.apk.sha256",
        )
    }

    @Test
    fun `Stage 323 preserves exact release source provenance`() {
        val workflow = source(".github/workflows/beta-apk.yml")

        assertContainsAll(
            workflow,
            "--target \"\$GITHUB_SHA\"",
            "Source commit: \$GITHUB_SHA",
        )
    }

    @Test
    fun `Stage 323 does not use release candidate or production signing path`() {
        val workflow = source(".github/workflows/beta-apk.yml")

        assertFalse(
            workflow.contains(":app:assembleRelease"),
            "Stage 323 Beta APK must not use the release build path.",
        )

        assertFalse(
            workflow.contains("DEVIL_RELEASE_KEYSTORE_PATH"),
            "Stage 323 must not consume the production release keystore.",
        )

        assertFalse(
            workflow.contains("DEVIL_RELEASE_CERT_SHA256"),
            "Stage 323 must not claim the permanent production signing identity.",
        )

        assertContainsAll(
            workflow,
            "controlled debug-signed Beta test build",
            "not a Release Candidate or production release",
        )
    }

    @Test
    fun `Stage 323 keeps extended device testing in Stage 324`() {
        val workflow = source(".github/workflows/beta-apk.yml")
        val thisSource =
            source(
                "app/src/test/kotlin/com/devil/app/integration/" +
                    "Stage323BetaApkContractTest.kt",
            )

        assertContainsAll(
            workflow,
            "APK_BUILT != APK_INSTALLED.",
            "APK_PUBLISHED != DEVICE_VALIDATED.",
            "APK_CHECKSUM_VERIFIED != FUNCTIONALLY_VALIDATED.",
            "Extended physical-device testing belongs to Stage 324.",
        )

        assertContainsAll(
            thisSource,
            "BETA_APK != RELEASE_CANDIDATE.",
            "BETA_APK != PRODUCTION_APK.",
            "BETA_APK != PRODUCTION_SIGNED_APK.",
            "APK_BUILT != APK_INSTALLED.",
            "APK_PUBLISHED != DEVICE_VALIDATED.",
            "APK_CHECKSUM_VERIFIED != FUNCTIONALLY_VALIDATED.",
            "BETA_DISTRIBUTION != NEW_AUTHORITY.",
            "STAGE_323 != STAGE_324_EXTENDED_DEVICE_TESTING.",
            "Stage 324 remains authoritative for extended physical-device testing.",
        )

        assertFalse(
            workflow.contains("adb install"),
            "Stage 323 workflow must not perform Stage 324 physical-device installation.",
        )
    }

    private fun assertContainsAll(
        source: String,
        vararg expected: String,
    ) {
        expected.forEach { text ->
            assertTrue(
                source.contains(text),
                "Expected source to contain: $text",
            )
        }
    }

    private fun source(path: String): String {
        val candidates =
            listOf(
                File(path),
                File("../$path"),
                File("../../$path"),
                File("../../../$path"),
            )

        val file =
            candidates.firstOrNull { it.isFile }
                ?: error("Required Stage 323 evidence source not found: $path")

        return file.readText()
    }
}
