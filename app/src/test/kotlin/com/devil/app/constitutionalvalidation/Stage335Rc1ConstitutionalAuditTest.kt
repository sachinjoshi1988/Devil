package com.devil.app.constitutionalvalidation

import java.io.File
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Stage 335 — RC1 Constitutional Audit.
 *
 * Stage 335 audits the exact RC1 lineage established by Stage 333 and
 * physically validated by Stage 334 against Devil's existing constitutional
 * architecture.
 *
 * It creates no new constitutional authority, runtime path, authorization,
 * execution approval, Observation, Verification, Outcome, World Model update,
 * Learning, Memory Authority approval, autonomy grant, security acceptance,
 * production readiness, or production release.
 *
 * RC1_CONSTITUTIONAL_AUDIT != CONSTITUTIONAL_AUTHORITY.
 * RC1_CONSTITUTIONAL_AUDIT != CONSTITUTIONAL_VERIFICATION.
 * RC1_CONSTITUTIONAL_AUDIT != AUTHENTICATION.
 * RC1_CONSTITUTIONAL_AUDIT != AUTHORIZATION.
 * RC1_CONSTITUTIONAL_AUDIT != BRAIN_DECISION.
 * RC1_CONSTITUTIONAL_AUDIT != PLANNING.
 * RC1_CONSTITUTIONAL_AUDIT != EXECUTIVE_READINESS.
 * RC1_CONSTITUTIONAL_AUDIT != EXECUTION_APPROVAL.
 * RC1_CONSTITUTIONAL_AUDIT != EXECUTION_ATTEMPT.
 * RC1_CONSTITUTIONAL_AUDIT != OBSERVATION.
 * RC1_CONSTITUTIONAL_AUDIT != VERIFICATION.
 * RC1_CONSTITUTIONAL_AUDIT != VERIFIED_OUTCOME.
 * RC1_CONSTITUTIONAL_AUDIT != WORLD_MODEL_UPDATE.
 * RC1_CONSTITUTIONAL_AUDIT != LEARNING.
 * RC1_CONSTITUTIONAL_AUDIT != MEMORY_AUTHORITY_APPROVAL.
 * RC1_CONSTITUTIONAL_AUDIT != AUTONOMY_GRANT.
 * RC1_CONSTITUTIONAL_AUDIT != SECURITY_ACCEPTANCE.
 * RC1_CONSTITUTIONAL_AUDIT != PRODUCTION_READINESS.
 * RC1_CONSTITUTIONAL_AUDIT != PRODUCTION_RELEASE.
 * STAGE_335 != STAGE_336_RC1_SECURITY_AUDIT.
 */
class Stage335Rc1ConstitutionalAuditTest {

    @Test
    fun `Stage 335 preserves exact RC1 and Stage 334 provenance`() {
        val record =
            repositoryFile(
                "docs/release/STAGE_335_RC1_CONSTITUTIONAL_AUDIT.md",
            ).readText()

        listOf(
            "2784b9ee1dff6db1b1d9452264e1f8e5045296ae",
            "d3b9a41c80a354808796398cfdb6cf2e1ee59b06",
            "de5539068df8bdaa9ad85ce49cdbcc85c9210484",
            "devil-stage-334-complete",
            "devil-v1.1.0-rc1",
            "com.devil.app",
            "1.1.0-rc1",
            "44ea0e44b54b179ed2f6e9311a38558ac240ab273979097d8a666895f7fbedd7",
            "96a20adba24a79d102a9c7722a761d290f217270a7e415051849f6a60f73177e",
        ).forEach { required ->
            assertTrue(
                record.contains(required),
                "Missing Stage 335 RC1 provenance: $required",
            )
        }

        val stage333 =
            repositoryFile(
                "app/src/test/kotlin/com/devil/app/integration/Stage333Rc1ContractTest.kt",
            ).readText()

        val stage334 =
            repositoryFile(
                "app/src/test/kotlin/com/devil/app/integration/Stage334Rc1DeviceValidationTest.kt",
            ).readText()

        assertTrue(stage333.contains("RC1 != PRODUCTION_RELEASE"))
        assertTrue(stage333.contains("RELEASE_SIGNING != DEVIL_AUTHORIZATION"))
        assertTrue(stage334.contains("RC1_DEVICE_VALIDATED != CONSTITUTIONAL_ACCEPTANCE"))
        assertTrue(stage334.contains("DEVICE_OBSERVATION != CONSTITUTIONAL_VERIFICATION"))
        assertTrue(stage334.contains("STAGE_334 != STAGE_335_RC1_CONSTITUTIONAL_AUDIT"))
    }

    @Test
    fun `Stage 335 preserves Stage 286 through 295 constitutional validation continuity`() {
        val requiredEvidence =
            listOf(
                "app/src/test/kotlin/com/devil/app/constitutionalvalidation/" +
                    "Stage286ConstitutionalChainValidationTest.kt" to
                    "CONSTITUTIONAL_CHAIN_VALIDATION != CONSTITUTIONAL_VERIFICATION.",
                "app/src/test/kotlin/com/devil/app/constitutionalvalidation/" +
                    "Stage287BrainAuthorityValidationTest.kt" to
                    "MODEL != BRAIN.",
                "app/src/test/kotlin/com/devil/app/constitutionalvalidation/" +
                    "Stage288PlannerBoundaryValidationTest.kt" to
                    "PLANNER != BRAIN.",
                "app/src/test/kotlin/com/devil/app/constitutionalvalidation/" +
                    "Stage289ExecutiveBoundaryValidationTest.kt" to
                    "EXECUTIVE_READY != EXECUTION.",
                "app/src/test/kotlin/com/devil/app/constitutionalvalidation/" +
                    "Stage290SecurityAuthorityValidationTest.kt" to
                    "SECURITY_AUTHORITY_VALIDATION != AUTHORIZATION.",
                "app/src/test/kotlin/com/devil/app/constitutionalvalidation/" +
                    "Stage291MemoryAuthorityValidationTest.kt" to
                    "MEMORY_AUTHORITY_APPROVAL != MEMORY_COMMITMENT.",
                "app/src/test/kotlin/com/devil/app/constitutionalvalidation/" +
                    "Stage292ExecutionEvidenceValidationTest.kt" to
                    "EXECUTION_APPROVED != EXECUTION_ATTEMPTED.",
                "app/src/test/kotlin/com/devil/app/constitutionalvalidation/" +
                    "Stage293ObservationVerificationValidationTest.kt" to
                    "VERIFIED != OUTCOME.",
                "app/src/test/kotlin/com/devil/app/constitutionalvalidation/" +
                    "Stage294WorldModelLearningValidationTest.kt" to
                    "LEARNING != MEMORY_PROPOSAL.",
                "app/src/test/kotlin/com/devil/app/constitutionalvalidation/" +
                    "Stage295ControlledAutonomyValidationTest.kt" to
                    "PREPARED != AUTHORIZED.",
            )

        requiredEvidence.forEach { (path, marker) ->
            val source = repositoryFile(path).readText()

            assertTrue(
                source.contains(marker),
                "Stage 335 must preserve constitutional evidence $marker from $path",
            )
        }
    }

    @Test
    fun `Stage 335 preserves one ordered Unified Devil Runtime`() {
        val runtime =
            repositoryFile(
                "core/runtime/src/main/kotlin/com/devil/core/runtime/" +
                    "DefaultUnifiedDevilRuntime.kt",
            ).readText()

        val orderedMarkers =
            listOf(
                "constitutionValidationAuthority.validate(",
                "identityAuthority.resolve(",
                "trustAuthority.evaluate(",
                "authorizationAuthority.authorize(",
                "conversationIntakeAuthority.intake(",
                "conversationRecordAuthority.record(",
                "conversationPersistenceAuthority.evaluatePersistence(",
                "understandingAuthority.understand(",
                "decisionAuthority.decide(",
                "taskAuthority.createTask(",
                "planAuthority.createPlan(",
                "capabilitySelectionAuthority.select(",
                "executiveReadinessAuthority.evaluate(",
                "executionAuthority.evaluate(",
                "executionAttemptPort.attempt(",
                "observationEvidencePort.observe(",
                "observationAuthority.observe(",
                "verificationEvidencePort.verify(",
                "verificationAuthority.verify(",
                "outcomeEvidencePort.establish(",
                "outcomeAuthority.establish(",
                "worldModelUpdateEvidencePort.establish(",
                "worldModelUpdateAuthority.evaluateUpdate(",
                "learningEvidencePort.establish(",
                "learningAuthority.evaluateLearning(",
                "memoryProposalEvidencePort.establish(",
                "memoryProposalAuthority.evaluateProposal(",
                "memoryAuthorityEvidencePort.establish(",
                "memoryAuthority.evaluateMemory(",
                "memoryCommitmentAuthority.evaluateCommitment(",
                "memoryPersistenceAuthority.evaluatePersistence(",
            )

        var previousIndex = -1

        orderedMarkers.forEach { marker ->
            val currentIndex =
                runtime.indexOf(
                    marker,
                    startIndex = previousIndex + 1,
                )

            assertTrue(
                currentIndex >= 0,
                "Missing Stage 335 constitutional runtime marker: $marker",
            )

            assertTrue(
                currentIndex > previousIndex,
                "Stage 335 constitutional runtime marker is out of order: $marker",
            )

            previousIndex = currentIndex
        }

        val stage49 =
            repositoryFile(
                "core/runtime/src/test/kotlin/com/devil/core/runtime/" +
                    "Stage49ConstitutionalRuntimeMatrixTest.kt",
            ).readText()

        val stage298 =
            repositoryFile(
                "core/runtime/src/test/kotlin/com/devil/core/runtime/" +
                    "Stage298EndToEndConstitutionalRuntimeTest.kt",
            ).readText()

        val stage96 =
            repositoryFile(
                "core/runtime/src/test/kotlin/com/devil/core/runtime/architecture/" +
                    "Stage96ConstitutionalArchitectureIntegrationAuditTest.kt",
            ).readText()

        assertTrue(stage49.contains("unified runtime preserves one ordered constitutional path"))
        assertTrue(stage298.contains("end to end runtime preserves the protected constitutional authority ordering"))
        assertTrue(
            stage96.contains(
                "this audit grants no authority and performs no execution",
            ),
        )
    }

    @Test
    fun `Stage 335 does not reinterpret RC1 device evidence as constitutional success`() {
        val record =
            repositoryFile(
                "docs/release/STAGE_335_RC1_CONSTITUTIONAL_AUDIT.md",
            ).readText()

        listOf(
            "ANDROID_PERMISSION != DEVIL_AUTHORIZATION.",
            "ACCESSIBILITY_ENABLED != DEVIL_AUTHORIZATION.",
            "ACCESSIBILITY_CONNECTED != EXECUTION_APPROVAL.",
            "VOICE_INPUT != AUTHENTICATION.",
            "VOICE_INPUT != AUTHORIZATION.",
            "RELEASE_SIGNING != DEVIL_AUTHORIZATION.",
            "RELEASE_SIGNING != EXECUTION_APPROVAL.",
            "RELEASE_SIGNING != VERIFIED_OUTCOME.",
            "DEVICE_OBSERVATION != CONSTITUTIONAL_VERIFICATION.",
            "OPEN_SETTINGS_REQUESTED != OPEN_SETTINGS_EXECUTED.",
            "DEFERRED != EXECUTION_SUCCESS.",
            "DEFERRED != VERIFIED_OUTCOME.",
        ).forEach { boundary ->
            assertTrue(
                record.contains(boundary),
                "Missing Stage 335 fail-closed RC1 boundary: $boundary",
            )
        }

        assertFalse(record.contains("DEFERRED = EXECUTION_SUCCESS"))
        assertFalse(record.contains("DEVICE_OBSERVATION = CONSTITUTIONAL_VERIFICATION"))
        assertFalse(record.contains("RC1_DEVICE_VALIDATED = CONSTITUTIONAL_ACCEPTANCE"))
    }

    @Test
    fun `Stage 335 remains evidence only and stops before security or production acceptance`() {
        val source =
            repositoryFile(
                "app/src/test/kotlin/com/devil/app/constitutionalvalidation/" +
                    "Stage335Rc1ConstitutionalAuditTest.kt",
            ).readText()

        val record =
            repositoryFile(
                "docs/release/STAGE_335_RC1_CONSTITUTIONAL_AUDIT.md",
            ).readText()

        listOf(
            "RC1_CONSTITUTIONAL_AUDIT != CONSTITUTIONAL_AUTHORITY.",
            "RC1_CONSTITUTIONAL_AUDIT != CONSTITUTIONAL_VERIFICATION.",
            "RC1_CONSTITUTIONAL_AUDIT != AUTHORIZATION.",
            "RC1_CONSTITUTIONAL_AUDIT != EXECUTION_APPROVAL.",
            "RC1_CONSTITUTIONAL_AUDIT != VERIFIED_OUTCOME.",
            "RC1_CONSTITUTIONAL_AUDIT != SECURITY_ACCEPTANCE.",
            "RC1_CONSTITUTIONAL_AUDIT != PRODUCTION_READINESS.",
            "RC1_CONSTITUTIONAL_AUDIT != PRODUCTION_RELEASE.",
            "STAGE_335 != STAGE_336_RC1_SECURITY_AUDIT.",
        ).forEach { boundary ->
            assertTrue(
                record.contains(boundary),
                "Missing Stage 335 completion boundary: $boundary",
            )
        }

        val importLines =
            source
                .lineSequence()
                .map(String::trim)
                .filter { it.startsWith("import ") }
                .toList()

        assertTrue(
            importLines.none { it.startsWith("import android.") },
            "Stage 335 evidence test must not depend on Android operational APIs: $importLines",
        )

        val forbiddenProductionNames =
            listOf(
                "Stage335Rc1ConstitutionalAuditCoordinator",
                "Stage335ConstitutionalAuthority",
                "Rc1ConstitutionalAuditAuthority",
                "Stage335ExecutionAuthority",
            )

        val productionRoots =
            listOf(
                repositoryFile("app/src/main"),
                repositoryFile("core/model/src/main"),
                repositoryFile("core/runtime/src/main"),
            )

        val violations =
            productionRoots
                .filter { it.exists() }
                .flatMap { root ->
                    root
                        .walkTopDown()
                        .filter { it.isFile }
                        .filter { it.extension == "kt" || it.extension == "kts" }
                        .filter { file ->
                            forbiddenProductionNames.any { forbidden ->
                                file.name.contains(forbidden) ||
                                    file.readText().contains(forbidden)
                            }
                        }
                        .toList()
                }

        assertTrue(
            violations.isEmpty(),
            "Stage 335 must not create operational constitutional-audit authority: $violations",
        )
    }

    private fun repositoryFile(path: String): File {
        return File(repositoryRoot(), path)
    }

    private fun repositoryRoot(): File {
        val candidates =
            listOf(
                File(".").absoluteFile.normalize(),
                File("..").absoluteFile.normalize(),
            )

        return candidates.firstOrNull { candidate ->
            File(candidate, "settings.gradle.kts").isFile &&
                File(candidate, "app").isDirectory &&
                File(candidate, "core").isDirectory
        } ?: error(
            "Unable to locate Devil repository root from: ${candidates.joinToString()}",
        )
    }
}
