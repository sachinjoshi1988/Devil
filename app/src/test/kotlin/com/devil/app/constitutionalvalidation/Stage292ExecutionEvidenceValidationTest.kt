package com.devil.app.constitutionalvalidation

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Stage 292 Execution Evidence Validation governance tests.
 *
 * Stage 292 validates existing constitutional execution/evidence boundaries only.
 *
 * It must not become Execution Authority, perform an execution attempt, establish
 * Observation or Verification, establish Outcome, or implement Stage 293
 * Observation / Verification Validation.
 */
class Stage292ExecutionEvidenceValidationTest {

    @Test
    fun `Stage 292 validation status remains architectural only`() {
        assertEquals(
            DevilExecutionEvidenceValidationStatus.VALIDATED,
            DevilExecutionEvidenceValidationStatus.valueOf("VALIDATED"),
        )

        assertEquals(
            DevilExecutionEvidenceValidationStatus.NOT_VALIDATED,
            DevilExecutionEvidenceValidationStatus.valueOf("NOT_VALIDATED"),
        )
    }

    @Test
    fun `Stage 292 evidence contract retains Stage 291 provenance and execution boundaries`() {
        val fieldNames =
            DevilExecutionEvidenceValidationEvidence::class.java
                .declaredFields
                .map { it.name }
                .toSet()

        listOf(
            "memoryAuthorityValidation",
            "executionAuthorityRemainsBoundedExecutionEvaluationAuthority",
            "executiveReadinessRemainsUpstreamOfExecutionApproval",
            "executionApprovalCannotBecomeExecutionAttemptEvidence",
            "executionAttemptMustRepresentAGenuineAttempt",
            "executionAttemptRemainsUpstreamOfObservation",
            "observationAndVerificationRemainDownstreamAndDistinct",
            "executionTraceAndResultInvariantsPreserved",
            "downstreamCapabilitiesCannotCreateOrReplaceExecutionAuthority",
        ).forEach { required ->
            assertTrue(
                fieldNames.contains(required),
                "Missing Stage 292 evidence property: $required",
            )
        }
    }

    @Test
    fun `Stage 292 preserves execution evidence boundaries and stops before Stage 293`() {
        val source = stage292Source()

        listOf(
            "EXECUTION_EVIDENCE_VALIDATION != EXECUTION_APPROVAL.",
            "EXECUTION_EVIDENCE_VALIDATION != EXECUTION_ATTEMPT.",
            "EXECUTION_EVIDENCE_VALIDATION != OBSERVATION.",
            "EXECUTION_EVIDENCE_VALIDATION != VERIFICATION.",
            "EXECUTION_EVIDENCE_VALIDATION != VERIFIED_OUTCOME.",
            "EXECUTION_APPROVED != EXECUTION_ATTEMPTED.",
            "EXECUTION_ATTEMPTED != OBSERVED.",
            "OBSERVATION != VERIFICATION.",
            "EXECUTION != VERIFICATION.",
        ).forEach { boundary ->
            assertTrue(
                source.contains(boundary),
                "Missing Stage 292 boundary: $boundary",
            )
        }

        assertTrue(
            source.contains(
                "implement Stage 293 Observation / Verification Validation",
            ),
        )
    }

    @Test
    fun `Stage 292 introduces no operational execution observation or verification wiring`() {
        val source = stage292Source()

        listOf(
            "DefaultExecutionAuthority(",
            "DefaultExecutionAttemptPort(",
            "DefaultObservationAuthority(",
            "DefaultVerificationAuthority(",
            "UnifiedDevilRuntime(",
            "ExecutionRequest(",
            "ObservationRequest(",
            "VerificationRequest(",
            "Runtime.getRuntime(",
            "ProcessBuilder(",
            "HttpURLConnection",
            "OkHttpClient",
        ).forEach { forbidden ->
            assertTrue(
                !source.contains(forbidden),
                "Stage 292 must not introduce operational execution, observation, verification, or future-stage wiring: $forbidden",
            )
        }
    }

    private fun stage292Source(): String {
        val candidates =
            listOf(
                File(
                    "src/main/kotlin/com/devil/app/constitutionalvalidation/DevilExecutionEvidenceValidation.kt",
                ),
                File(
                    "app/src/main/kotlin/com/devil/app/constitutionalvalidation/DevilExecutionEvidenceValidation.kt",
                ),
            )

        return candidates
            .firstOrNull { it.isFile }
            ?.readText()
            ?: error(
                "Unable to locate Stage 292 production source from: ${candidates.joinToString()}",
            )
    }
}
