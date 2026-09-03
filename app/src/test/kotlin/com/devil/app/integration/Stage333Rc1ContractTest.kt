package com.devil.app.integration

import java.io.File
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Stage 333 — RC1
 *
 * Stage 333 establishes the Release Candidate 1 artifact contract for the
 * current post-Beta Devil source lineage.
 *
 * It reuses the already-established permanent Devil V1 release-signing
 * architecture. Release maturity must not create a new Devil authority,
 * execution path, runtime, signing authority, or constitutional bypass.
 *
 * The historical Devil V1.0.0 RC1 and production release identities remain
 * frozen history. Stage 333 therefore advances the current release lineage to
 * versionCode 4 / versionName 1.1.0-rc1 and a distinct RC1 prerelease identity.
 *
 * RC1 is still pre-production.
 *
 * BETA_FREEZE != RC1
 * RC1_BUILT != RC1_INSTALLED
 * RC1_PUBLISHED != RC1_DEVICE_VALIDATED
 * RC1_CHECKSUM_VERIFIED != FUNCTIONALLY_VALIDATED
 * RC1_SIGNER_VERIFIED != FUNCTIONALLY_VALIDATED
 * RELEASE_SIGNING != DEVIL_AUTHORIZATION
 * RELEASE_SIGNING != EXECUTION_APPROVAL
 * RELEASE_SIGNING != VERIFIED_OUTCOME
 * RC1 != PRODUCTION_RELEASE
 * RC1 != PRODUCTION_READINESS
 * STAGE_333 != STAGE_334_RC1_DEVICE_VALIDATION
 */
class Stage333Rc1ContractTest {

    @Test
    fun `Stage 333 advances the current Android release identity monotonically`() {
        val build = repositoryFile("app/build.gradle.kts").readText()
        val historicalRc1 = repositoryFile("docs/release/STAGE_53_RC1.md").readText()
        val historicalProduction =
            repositoryFile("docs/release/STAGE_55_PRODUCTION.md").readText()

        assertTrue(
            build.contains("""applicationId = "com.devil.app""""),
            "Stage 333 must preserve the Devil Android package identity.",
        )
        assertTrue(
            build.contains("versionCode = 4"),
            "Stage 333 RC1 must advance beyond historical production versionCode 3.",
        )
        assertTrue(
            build.contains("""versionName = "1.1.0-rc1""""),
            "Stage 333 must expose the approved current RC1 version name.",
        )

        assertTrue(
            historicalRc1.contains("`1.0.0-rc1`"),
            "Historical Stage 53 RC1 evidence must remain preserved.",
        )
        assertTrue(
            historicalProduction.contains("`1.0.0`"),
            "Historical Stage 55 production evidence must remain preserved.",
        )
    }

    @Test
    fun `Stage 333 has a distinct current RC1 prerelease identity`() {
        val workflow = repositoryFile(".github/workflows/rc1-apk.yml").readText()

        listOf(
            "name: Devil V1 RC1 APK",
            "RELEASE_TAG: devil-v1.1.0-rc1",
            "release/devil-v1.1.0-rc1.apk",
            "release/devil-v1.1.0-rc1.apk.sha256",
            "release/devil-v1.1.0-rc1.signer.txt",
            """--title "Devil V1.1.0 RC1"""",
            "--prerelease",
            "Version name: 1.1.0-rc1",
            "Version code: 4",
        ).forEach { marker ->
            assertTrue(
                workflow.contains(marker),
                "Stage 333 RC1 workflow must contain: $marker",
            )
        }

        assertFalse(
            workflow.contains("RELEASE_TAG: devil-v1.0.0-rc1"),
            "Stage 333 must not collide with the historical RC1 Git tag.",
        )
        assertFalse(
            workflow.contains("""--title "Devil V1.0.0 RC1""""),
            "Stage 333 must not masquerade as the historical Stage 53 RC1 release.",
        )
    }

    @Test
    fun `Stage 333 preserves release signing integrity and provenance gates`() {
        val workflow = repositoryFile(".github/workflows/rc1-apk.yml").readText()
        val build = repositoryFile("app/build.gradle.kts").readText()

        listOf(
            "DEVIL_RELEASE_KEYSTORE_BASE64",
            "DEVIL_RELEASE_KEYSTORE_PASSWORD",
            "DEVIL_RELEASE_KEY_ALIAS",
            "DEVIL_RELEASE_KEY_PASSWORD",
            "DEVIL_RELEASE_CERT_SHA256",
            "./gradlew --no-daemon :app:assembleRelease",
            "apksigner",
            "--print-certs",
            "certificate SHA-256 digest:",
            "sha256sum",
            "sha256sum --check",
            """--target "${'$'}GITHUB_SHA"""",
        ).forEach { marker ->
            assertTrue(
                workflow.contains(marker),
                "Stage 333 must preserve RC1 integrity/provenance gate: $marker",
            )
        }

        assertTrue(
            build.contains("Devil release signing credentials are unavailable."),
            "Release builds must continue failing closed without signing credentials.",
        )
        assertTrue(
            build.contains(
                "Devil release keystore does not exist at the configured path.",
            ),
            "Release builds must continue failing closed when the configured keystore is absent.",
        )
    }

    @Test
    fun `Stage 333 preserves Beta to RC and RC to production boundaries`() {
        val betaFreeze =
            repositoryFile(
                "app/src/test/kotlin/com/devil/app/constitutionalvalidation/" +
                    "Stage332BetaFreezeTest.kt",
            ).readText()

        val betaApk =
            repositoryFile(
                "app/src/test/kotlin/com/devil/app/integration/" +
                    "Stage323BetaApkContractTest.kt",
            ).readText()

        val workflow = repositoryFile(".github/workflows/rc1-apk.yml").readText()

        assertTrue(betaFreeze.contains("BETA_FREEZE != RELEASE_CANDIDATE"))
        assertTrue(betaFreeze.contains("STAGE_332 != STAGE_333"))
        assertTrue(betaApk.contains("BETA_APK != RELEASE_CANDIDATE."))

        assertTrue(
            workflow.contains(
                "RC1 is pre-production. It is not the final Devil V1 production release.",
            ),
            "Stage 333 RC1 must remain explicitly pre-production.",
        )

        assertFalse(
            workflow.contains("RELEASE_TAG: devil-v1.0.0"),
            "RC1 workflow must not publish the historical production release identity.",
        )
    }

    @Test
    fun `Stage 333 stops before Stage 334 device validation and creates no runtime RC authority`() {
        val stage333 = repositoryFile(
            "app/src/test/kotlin/com/devil/app/integration/Stage333Rc1ContractTest.kt",
        ).readText()

        listOf(
            "BETA_FREEZE != RC1",
            "RC1_BUILT != RC1_INSTALLED",
            "RC1_PUBLISHED != RC1_DEVICE_VALIDATED",
            "RC1_CHECKSUM_VERIFIED != FUNCTIONALLY_VALIDATED",
            "RC1_SIGNER_VERIFIED != FUNCTIONALLY_VALIDATED",
            "RELEASE_SIGNING != DEVIL_AUTHORIZATION",
            "RELEASE_SIGNING != EXECUTION_APPROVAL",
            "RELEASE_SIGNING != VERIFIED_OUTCOME",
            "RC1 != PRODUCTION_RELEASE",
            "RC1 != PRODUCTION_READINESS",
            "STAGE_333 != STAGE_334_RC1_DEVICE_VALIDATION",
        ).forEach { marker ->
            assertTrue(
                stage333.contains(marker),
                "Stage 333 must preserve fail-closed marker: $marker",
            )
        }

        val productionRoots =
            listOf(
                "app/src/main",
                "core/model/src/main",
                "core/runtime/src/main",
            )
                .map(::repositoryFile)
                .filter(File::exists)

        val forbiddenTerms =
            listOf(
                "Stage333Rc1Coordinator",
                "Stage333ReleaseCandidateAuthority",
                "Rc1Authority",
                "ReleaseCandidateAuthority",
            )

        val violations =
            productionRoots
                .flatMap { root ->
                    root.walkTopDown()
                        .filter { it.isFile && it.extension == "kt" }
                        .toList()
                }
                .flatMap { file ->
                    val source = file.readText()
                    forbiddenTerms
                        .filter(source::contains)
                        .map { term -> "${file.path}: $term" }
                }

        assertTrue(
            violations.isEmpty(),
            "Stage 333 must not create runtime RC authority: $violations",
        )
    }

    private fun repositoryFile(path: String): File {
        return File(repositoryRoot(), path)
    }

    private fun repositoryRoot(): File {
        var current = File(System.getProperty("user.dir") ?: error("JVM user.dir is unavailable.")).absoluteFile

        while (true) {
            val hasSettings =
                File(current, "settings.gradle.kts").isFile ||
                    File(current, "settings.gradle").isFile

            if (hasSettings && File(current, "app").isDirectory) {
                return current
            }

            current = current.parentFile
                ?: error(
                    "Unable to locate Devil repository root from JVM user.dir.",
                )
        }
    }
}
