package com.devil.app.constitutionalvalidation

import java.io.File
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Stage 322 — Beta Architecture Freeze completion evidence.
 *
 * This is test-only completion evidence that Devil enters the Beta phase with
 * the already-established constitutional architecture intact.
 *
 * Stage 322 verifies that:
 *
 * - the single Unified Devil Runtime architecture remains intact;
 * - protected constitutional authority boundaries remain distinct;
 * - the completed system-wide constitutional-validation phase remains present;
 * - the completed Alpha reliability-freeze boundary remains intact;
 * - entering Beta does not create a new constitutional or operational authority;
 * - historical Closed Beta infrastructure does not itself complete Stage 323.
 *
 * Stage 322 does not modify production architecture and does not create a new
 * Brain, Executive, Planner, Memory Authority, Security Authority,
 * Authorization Authority, Execution Authority, Observation Authority,
 * Verification Authority, Outcome Authority, persistence authority,
 * scheduling authority, recovery authority, or background-work authority.
 *
 * BETA_ARCHITECTURE_FREEZE != BETA_APK.
 * BETA_ARCHITECTURE_FREEZE != PRODUCTION_ARCHITECTURE_CHANGE.
 * BETA_PHASE_ENTRY != NEW_AUTHORITY.
 * BETA_PHASE_ENTRY != AUTHORIZATION.
 * BETA_PHASE_ENTRY != EXECUTION_APPROVAL.
 * BETA_PHASE_ENTRY != BACKGROUND_EXECUTION_AUTHORIZED.
 * BETA_PHASE_ENTRY != VERIFIED_OUTCOME.
 * FREEZE_EVIDENCE != CONSTITUTIONAL_VERIFICATION.
 * EXISTING_BETA_RELEASE_INFRASTRUCTURE != STAGE_323_BETA_APK_COMPLETION.
 *
 * Stage 322 does not implement Stage 323 Beta APK or later Beta-stage behavior.
 */
class Stage322BetaArchitectureFreezeTest {

    @Test
    fun `Stage 322 retains the single unified constitutional architecture`() {
        val stage96 =
            source(
                "core/runtime/src/test/kotlin/com/devil/core/runtime/architecture/" +
                    "Stage96ConstitutionalArchitectureIntegrationAuditTest.kt",
            )

        assertContainsAll(
            stage96,
            "This test records architecture truth at the Stage 95 freeze boundary.",
            "there remains one UnifiedDevilRuntime contract;",
            "Memory Authority remains distinct from proposal, commitment, and persistence;",
            "Controlled Autonomy is not Authorization, Decision, Planning, Execution,",
            "STAGE_96_AUDIT != STAGE_97_RUNTIME_INTEGRATION.",
        )
    }

    @Test
    fun `Stage 322 retains completed system wide constitutional validation`() {
        val validationTests =
            listOf(
                "Stage286ConstitutionalChainValidationTest.kt",
                "Stage287BrainAuthorityValidationTest.kt",
                "Stage288PlannerBoundaryValidationTest.kt",
                "Stage289ExecutiveBoundaryValidationTest.kt",
                "Stage290SecurityAuthorityValidationTest.kt",
                "Stage291MemoryAuthorityValidationTest.kt",
                "Stage292ExecutionEvidenceValidationTest.kt",
                "Stage293ObservationVerificationValidationTest.kt",
                "Stage294WorldModelLearningValidationTest.kt",
                "Stage295ControlledAutonomyValidationTest.kt",
            )

        validationTests.forEach { fileName ->
            val validation =
                source(
                    "app/src/test/kotlin/com/devil/app/constitutionalvalidation/$fileName",
                )

            val expectedClassName = fileName.removeSuffix(".kt")

            assertTrue(
                validation.contains("class $expectedClassName"),
                "Missing constitutional-validation class: $expectedClassName",
            )

            assertTrue(
                validation.contains("@Test"),
                "Constitutional-validation test must retain executable evidence: $fileName",
            )
        }
    }

    @Test
    fun `Stage 322 retains the completed Alpha freeze boundary`() {
        val stage321 =
            source(
                "app/src/test/kotlin/com/devil/app/reliability/" +
                    "Stage321AlphaReliabilityFreezeTest.kt",
            )

        assertContainsAll(
            stage321,
            "This is test-only completion evidence",
            "does not modify production architecture",
            "ALPHA_RELIABILITY_FREEZE != APPLICATION_NEVER_FAILS.",
            "ALPHA_RELIABILITY_FREEZE != BACKGROUND_EXECUTION_AUTHORIZED.",
            "ALPHA_RELIABILITY_FREEZE != AUTOMATIC_RECOVERY_AUTHORIZED.",
            "ALPHA_RELIABILITY_FREEZE != AUTOMATIC_CONTINUATION_AUTHORIZED.",
            "ALPHA_RELIABILITY_FREEZE != PERMANENT_AUTHORIZATION.",
            "ALPHA_RELIABILITY_FREEZE != VERIFIED_OUTCOME.",
            "FREEZE_EVIDENCE != CONSTITUTIONAL_VERIFICATION.",
            "does not implement Stage 322 or any Beta-stage behavior",
        )
    }

    @Test
    fun `Stage 322 adds no production Beta architecture authority`() {
        val productionSources = productionSources()

        assertTrue(
            productionSources.none { file ->
                file.readText().contains("Stage322BetaArchitectureFreeze")
            },
            "Stage 322 must remain test-only and must not create production Beta architecture.",
        )

        assertTrue(
            productionSources.none { file ->
                file.readText().contains(
                    "Stage322BetaArchitectureFreeze" + "Coordinator",
                )
            },
            "Stage 322 must not create a Beta architecture-freeze coordinator.",
        )
    }

    @Test
    fun `Stage 322 locks Beta architecture freeze semantics`() {
        val stage322 =
            source(
                "app/src/test/kotlin/com/devil/app/constitutionalvalidation/" +
                    "Stage322BetaArchitectureFreezeTest.kt",
            )

        assertContainsAll(
            stage322,
            "This is test-only completion evidence",
            "does not modify production architecture",
            "BETA_ARCHITECTURE_FREEZE != BETA_APK.",
            "BETA_ARCHITECTURE_FREEZE != PRODUCTION_ARCHITECTURE_CHANGE.",
            "BETA_PHASE_ENTRY != NEW_AUTHORITY.",
            "BETA_PHASE_ENTRY != AUTHORIZATION.",
            "BETA_PHASE_ENTRY != EXECUTION_APPROVAL.",
            "BETA_PHASE_ENTRY != BACKGROUND_EXECUTION_AUTHORIZED.",
            "BETA_PHASE_ENTRY != VERIFIED_OUTCOME.",
            "FREEZE_EVIDENCE != CONSTITUTIONAL_VERIFICATION.",
            "EXISTING_BETA_RELEASE_INFRASTRUCTURE != STAGE_323_BETA_APK_COMPLETION.",
            "does not implement Stage 323 Beta APK or later Beta-stage behavior",
        )

        val historicalWorkflow =
            source(".github/workflows/closed-beta-apk.yml")

        val historicalContract =
            source("docs/release/STAGE_52_CLOSED_BETA.md")

        assertContainsAll(
            historicalWorkflow,
            "Devil Closed Beta APK",
            "devil-closed-beta.apk",
            "devil-closed-beta-",
        )

        assertContainsAll(
            historicalContract,
            "Devil Stage 52",
            "Closed Beta",
            "Stage 53 — RC1",
        )

        assertFalse(
            historicalWorkflow.contains("Stage 323"),
            "Historical Closed Beta workflow must not masquerade as Stage 323.",
        )

        assertFalse(
            historicalContract.contains("Stage 323"),
            "Historical Stage 52 contract must not masquerade as Stage 323.",
        )
    }

    private fun assertContainsAll(
        source: String,
        vararg markers: String,
    ) {
        markers.forEach { marker ->
            assertTrue(
                source.contains(marker),
                "Missing Stage 322 Beta architecture-freeze evidence: $marker",
            )
        }
    }

    private fun productionSources(): List<File> {
        val relativePath = "app/src/main/kotlin"

        val root =
            listOf(
                File(relativePath),
                File("../$relativePath"),
                File("../../$relativePath"),
            ).firstOrNull { it.isDirectory }
                ?: error(
                    "Unable to locate production source tree for Stage 322.",
                )

        return root
            .walkTopDown()
            .filter { it.isFile && it.extension == "kt" }
            .toList()
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
            ?: error(
                "Unable to locate repository source for Stage 322: $path",
            )
    }
}
