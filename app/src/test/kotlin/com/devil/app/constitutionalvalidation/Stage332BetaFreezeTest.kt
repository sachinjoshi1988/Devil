package com.devil.app.constitutionalvalidation

import java.io.File
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Stage 332 — Beta Freeze completion evidence.
 *
 * Stage 332 closes the Beta phase by preserving the repository state and
 * architectural boundaries established through Stages 322–331.
 *
 * It does not reinterpret successful Beta evidence as production readiness,
 * production security acceptance, constitutional verification, authorization,
 * execution approval, verified outcome, or Release Candidate completion.
 *
 * Stage 332 also does not create a runtime "freeze" concept. Freeze is a
 * repository/development boundary here, not a new Devil authority or capability.
 *
 * BETA_FREEZE != PRODUCTION_READINESS
 * BETA_FREEZE != PRODUCTION_SECURITY_ACCEPTANCE
 * BETA_FREEZE != RELEASE_CANDIDATE
 * BETA_FREEZE != PRODUCTION_RELEASE
 * BETA_FREEZE != APPLICATION_NEVER_FAILS
 * BETA_FREEZE != AUTHENTICATION
 * BETA_FREEZE != AUTHORIZATION
 * BETA_FREEZE != EXECUTION_REQUEST
 * BETA_FREEZE != EXECUTION_APPROVAL
 * BETA_FREEZE != VERIFIED_OUTCOME
 * BETA_FREEZE_EVIDENCE != CONSTITUTIONAL_VERIFICATION
 * STAGE_332 != STAGE_333
 *
 * Stage 332 introduces no Beta Freeze Authority, Beta Freeze Coordinator,
 * runtime freeze state, authorization state, execution path, background
 * service, persistence mechanism, production signing path, or Stage 333
 * Release Candidate behavior.
 */
class Stage332BetaFreezeTest {

    @Test
    fun `Stage 332 preserves Alpha and Beta architecture freeze continuity`() {
        val stage321 =
            repositoryFile(
                "app/src/test/kotlin/com/devil/app/reliability/" +
                    "Stage321AlphaReliabilityFreezeTest.kt",
            ).readText()

        val stage322 =
            repositoryFile(
                "app/src/test/kotlin/com/devil/app/constitutionalvalidation/" +
                    "Stage322BetaArchitectureFreezeTest.kt",
            ).readText()

        listOf(
            "ALPHA_RELIABILITY_FREEZE != APPLICATION_NEVER_FAILS.",
            "ALPHA_RELIABILITY_FREEZE != BACKGROUND_EXECUTION_AUTHORIZED.",
            "ALPHA_RELIABILITY_FREEZE != PERMANENT_AUTHORIZATION.",
            "ALPHA_RELIABILITY_FREEZE != VERIFIED_OUTCOME.",
            "FREEZE_EVIDENCE != CONSTITUTIONAL_VERIFICATION.",
        ).forEach { marker ->
            assertTrue(
                stage321.contains(marker),
                "Stage 332 must preserve the completed Alpha freeze boundary: $marker",
            )
        }

        listOf(
            "BETA_ARCHITECTURE_FREEZE != BETA_APK.",
            "BETA_ARCHITECTURE_FREEZE != PRODUCTION_ARCHITECTURE_CHANGE.",
            "BETA_PHASE_ENTRY != NEW_AUTHORITY.",
            "BETA_PHASE_ENTRY != AUTHORIZATION.",
            "BETA_PHASE_ENTRY != EXECUTION_APPROVAL.",
            "BETA_PHASE_ENTRY != VERIFIED_OUTCOME.",
            "FREEZE_EVIDENCE != CONSTITUTIONAL_VERIFICATION.",
        ).forEach { marker ->
            assertTrue(
                stage322.contains(marker),
                "Stage 332 must preserve the Stage 322 Beta architecture boundary: $marker",
            )
        }

        assertTrue(
            stage322.contains(
                "Stage 322 retains the single unified constitutional architecture",
            ),
            "Stage 332 must retain the existing single unified constitutional architecture.",
        )

        assertTrue(
            stage322.contains(
                "Stage 322 must not create a Beta architecture-freeze coordinator.",
            ),
            "Stage 332 must preserve the precedent that freeze evidence creates no coordinator.",
        )
    }

    @Test
    fun `Stage 332 preserves the completed Beta phase without reimplementing it`() {
        val root = repositoryRoot()

        val requiredPaths =
            listOf(
                ".github/workflows/beta-apk.yml",
                "app/src/test/kotlin/com/devil/app/integration/" +
                    "Stage323BetaApkContractTest.kt",
                "app/src/main/kotlin/com/devil/app/accessibility/" +
                    "DevilAccessibilityService.kt",
                "app/src/main/kotlin/com/devil/app/accessibility/" +
                    "Stage314AndroidAccessibilityChangeReadinessStore.kt",
                "app/src/main/kotlin/com/devil/app/education/" +
                    "Stage325ExtendedEducationTestingCoordinator.kt",
                "app/src/test/kotlin/com/devil/app/education/" +
                    "Stage325ExtendedEducationTestingCoordinatorTest.kt",
                "app/src/main/kotlin/com/devil/app/education/" +
                    "Stage326LanguageCurriculumValidationCoordinator.kt",
                "app/src/test/kotlin/com/devil/app/education/" +
                    "Stage326LanguageCurriculumValidationCoordinatorTest.kt",
                "app/src/test/kotlin/com/devil/app/ui/accessibility/" +
                    "Stage327AccessibilityTestingTest.kt",
                "app/src/test/kotlin/com/devil/app/privacy/" +
                    "Stage328PrivacyTestingTest.kt",
                "app/src/test/kotlin/com/devil/app/security/" +
                    "Stage329SecurityPenetrationTestingTest.kt",
                "app/src/test/kotlin/com/devil/app/performance/" +
                    "Stage330PerformanceBetaTest.kt",
                "app/src/test/kotlin/com/devil/app/security/" +
                    "Stage331BetaDefectClosureTest.kt",
            )

        requiredPaths.forEach { path ->
            assertTrue(
                File(root, path).isFile,
                "Completed Beta evidence must remain represented at freeze: $path",
            )
        }

        val removedStage314Diagnostic =
            File(
                root,
                "app/src/main/kotlin/com/devil/app/accessibility/" +
                    "Stage314AccessibilityLifecycleDiagnosticRecorder.kt",
            )

        assertFalse(
            removedStage314Diagnostic.exists(),
            "Stage 332 must preserve the Stage 324 repository result rather than restore " +
                "the removed diagnostic recorder.",
        )

        val stage323 =
            repositoryFile(
                "app/src/test/kotlin/com/devil/app/integration/" +
                    "Stage323BetaApkContractTest.kt",
            ).readText()

        assertTrue(
            stage323.contains("BETA_APK != RELEASE_CANDIDATE."),
            "The dedicated Beta APK must remain distinct from Release Candidate.",
        )
        assertTrue(
            stage323.contains("BETA_APK != PRODUCTION_APK."),
            "The dedicated Beta APK must remain distinct from production APK.",
        )

        val stage327 =
            repositoryFile(
                "app/src/test/kotlin/com/devil/app/ui/accessibility/" +
                    "Stage327AccessibilityTestingTest.kt",
            ).readText()

        assertTrue(
            stage327.contains("STAGE_327 != STAGE_328_PRIVACY_TESTING."),
            "Stage 327 completion boundary must remain intact.",
        )

        val stage328 =
            repositoryFile(
                "app/src/test/kotlin/com/devil/app/privacy/" +
                    "Stage328PrivacyTestingTest.kt",
            ).readText()

        assertTrue(
            stage328.contains("STAGE_328 != STAGE_329_SECURITY_PENETRATION_TESTING."),
            "Stage 328 completion boundary must remain intact.",
        )

        val stage329 =
            repositoryFile(
                "app/src/test/kotlin/com/devil/app/security/" +
                    "Stage329SecurityPenetrationTestingTest.kt",
            ).readText()

        assertTrue(
            stage329.contains("STAGE_329 != STAGE_330_PERFORMANCE_BETA."),
            "Stage 329 completion boundary must remain intact.",
        )

        val stage330 =
            repositoryFile(
                "app/src/test/kotlin/com/devil/app/performance/" +
                    "Stage330PerformanceBetaTest.kt",
            ).readText()

        assertTrue(
            stage330.contains("STAGE_330 != STAGE_331_BETA_DEFECT_CLOSURE."),
            "Stage 330 completion boundary must remain intact.",
        )

        val stage331 =
            repositoryFile(
                "app/src/test/kotlin/com/devil/app/security/" +
                    "Stage331BetaDefectClosureTest.kt",
            ).readText()

        assertTrue(
            stage331.contains("STAGE_331 != STAGE_332_BETA_FREEZE"),
            "Stage 331 must stop before the Stage 332 Beta Freeze boundary.",
        )
    }

    @Test
    fun `Stage 332 keeps successful Beta evidence fail closed`() {
        val stage323 =
            repositoryFile(
                "app/src/test/kotlin/com/devil/app/integration/" +
                    "Stage323BetaApkContractTest.kt",
            ).readText()

        val stage329 =
            repositoryFile(
                "app/src/test/kotlin/com/devil/app/security/" +
                    "Stage329SecurityPenetrationTestingTest.kt",
            ).readText()

        val stage330 =
            repositoryFile(
                "app/src/test/kotlin/com/devil/app/performance/" +
                    "Stage330PerformanceBetaTest.kt",
            ).readText()

        val stage331 =
            repositoryFile(
                "app/src/test/kotlin/com/devil/app/security/" +
                    "Stage331BetaDefectClosureTest.kt",
            ).readText()

        assertTrue(stage323.contains("BETA_APK != RELEASE_CANDIDATE."))
        assertTrue(stage323.contains("BETA_APK != PRODUCTION_APK."))

        assertTrue(
            stage329.contains(
                "PENETRATION_TEST_PASSED != PRODUCTION_SECURITY_ACCEPTANCE.",
            ),
        )
        assertTrue(
            stage329.contains(
                "PENETRATION_TEST_PASSED != CONSTITUTIONAL_VERIFICATION.",
            ),
        )

        assertTrue(
            stage330.contains("PERFORMANCE_BETA_PASSED != PRODUCTION_READINESS."),
        )
        assertTrue(
            stage330.contains("PERFORMANCE_BETA_PASSED != VERIFIED_OUTCOME."),
        )
        assertTrue(
            stage330.contains(
                "PERFORMANCE_BETA_EVIDENCE != CONSTITUTIONAL_VERIFICATION.",
            ),
        )

        assertTrue(
            stage331.contains(
                "BETA_DEFECT_CLOSED != PRODUCTION_SECURITY_ACCEPTANCE",
            ),
        )
        assertTrue(
            stage331.contains(
                "BETA_DEFECT_CLOSED != CONSTITUTIONAL_VERIFICATION",
            ),
        )

        val stage332 =
            repositoryFile(
                "app/src/test/kotlin/com/devil/app/constitutionalvalidation/" +
                    "Stage332BetaFreezeTest.kt",
            ).readText()

        val freezeBoundaries =
            listOf(
                "BETA_FREEZE != PRODUCTION_READINESS",
                "BETA_FREEZE != PRODUCTION_SECURITY_ACCEPTANCE",
                "BETA_FREEZE != RELEASE_CANDIDATE",
                "BETA_FREEZE != PRODUCTION_RELEASE",
                "BETA_FREEZE != APPLICATION_NEVER_FAILS",
                "BETA_FREEZE != AUTHENTICATION",
                "BETA_FREEZE != AUTHORIZATION",
                "BETA_FREEZE != EXECUTION_REQUEST",
                "BETA_FREEZE != EXECUTION_APPROVAL",
                "BETA_FREEZE != VERIFIED_OUTCOME",
                "BETA_FREEZE_EVIDENCE != CONSTITUTIONAL_VERIFICATION",
            )

        freezeBoundaries.forEach { boundary ->
            assertTrue(
                stage332.contains(boundary),
                "Missing Stage 332 Beta Freeze boundary: $boundary",
            )
        }
    }

    @Test
    fun `Stage 332 adds no production Beta freeze authority`() {
        val root = repositoryRoot()

        val productionRoots =
            listOf(
                File(root, "app/src/main"),
                File(root, "core/model/src/main"),
                File(root, "core/runtime/src/main"),
            ).filter { it.isDirectory }

        val forbiddenProductionTerms =
            listOf(
                "Stage332BetaFreeze",
                "Stage332BetaFreezeCoordinator",
                "BetaFreezeAuthority",
            )

        val violations =
            productionRoots
                .asSequence()
                .flatMap { directory ->
                    directory
                        .walkTopDown()
                        .filter { file -> file.isFile && file.extension == "kt" }
                }
                .flatMap { file ->
                    val source = file.readText()

                    forbiddenProductionTerms
                        .asSequence()
                        .filter { term -> source.contains(term) }
                        .map { term -> "${file.relativeTo(root).path}: $term" }
                }
                .toList()

        assertTrue(
            violations.isEmpty(),
            "Stage 332 must remain test-only and create no production Beta-freeze " +
                "authority or coordinator: $violations",
        )
    }

    @Test
    fun `Stage 332 locks Beta Freeze semantics and stops before Stage 333`() {
        val stage332 =
            repositoryFile(
                "app/src/test/kotlin/com/devil/app/constitutionalvalidation/" +
                    "Stage332BetaFreezeTest.kt",
            ).readText()

        val requiredMarkers =
            listOf(
                "Stage 332 — Beta Freeze completion evidence.",
                "BETA_FREEZE != PRODUCTION_READINESS",
                "BETA_FREEZE != PRODUCTION_SECURITY_ACCEPTANCE",
                "BETA_FREEZE != RELEASE_CANDIDATE",
                "BETA_FREEZE != PRODUCTION_RELEASE",
                "BETA_FREEZE != APPLICATION_NEVER_FAILS",
                "BETA_FREEZE != AUTHENTICATION",
                "BETA_FREEZE != AUTHORIZATION",
                "BETA_FREEZE != EXECUTION_REQUEST",
                "BETA_FREEZE != EXECUTION_APPROVAL",
                "BETA_FREEZE != VERIFIED_OUTCOME",
                "BETA_FREEZE_EVIDENCE != CONSTITUTIONAL_VERIFICATION",
                "STAGE_332 != STAGE_333",
                "Stage 332 introduces no Beta Freeze Authority",
            )

        requiredMarkers.forEach { marker ->
            assertTrue(
                stage332.contains(marker),
                "Missing Stage 332 Beta Freeze evidence: $marker",
            )
        }

        assertTrue(
            stage332.contains(
                "does not create a runtime \"freeze\" concept",
            ),
            "Beta Freeze must remain a repository/development boundary.",
        )

        assertTrue(
            stage332.contains(
                "Stage 333 Release Candidate behavior",
            ),
            "Stage 332 must explicitly stop before later RC behavior.",
        )
    }

    private fun repositoryFile(path: String): File {
        val file = File(repositoryRoot(), path)

        require(file.isFile) {
            "Unable to locate Stage 332 repository file: $path"
        }

        return file
    }

    private fun repositoryRoot(): File {
        val userDir =
            System.getProperty("user.dir")
                ?.let(::File)
                ?.absoluteFile
                ?: error("Stage 332 requires a JVM user.dir for repository validation.")

        return generateSequence(userDir) { current -> current.parentFile }
            .firstOrNull { candidate ->
                File(candidate, "app").isDirectory &&
                    (
                        File(candidate, "settings.gradle.kts").isFile ||
                            File(candidate, "settings.gradle").isFile
                    )
            }
            ?: error(
                "Unable to locate Devil repository root for Stage 332 validation.",
            )
    }
}
