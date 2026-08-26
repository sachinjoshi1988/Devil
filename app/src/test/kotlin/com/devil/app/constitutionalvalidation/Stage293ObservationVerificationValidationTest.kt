package com.devil.app.constitutionalvalidation

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Stage 293 Observation / Verification Validation governance tests.
 *
 * Stage 293 validates existing constitutional Observation and Verification
 * boundaries only.
 *
 * It must not become Observation Authority, Verification Authority, establish
 * Outcome, or implement Stage 294 World Model & Learning Validation.
 */
class Stage293ObservationVerificationValidationTest {

    @Test
    fun `Stage 293 validation status remains architectural only`() {
        assertEquals(
            DevilObservationVerificationValidationStatus.VALIDATED,
            DevilObservationVerificationValidationStatus.valueOf("VALIDATED"),
        )

        assertEquals(
            DevilObservationVerificationValidationStatus.NOT_VALIDATED,
            DevilObservationVerificationValidationStatus.valueOf("NOT_VALIDATED"),
        )
    }

    @Test
    fun `Stage 293 evidence retains Stage 292 provenance and authority boundaries`() {
        val fieldNames =
            DevilObservationVerificationValidationEvidence::class.java
                .declaredFields
                .map { it.name }
                .toSet()

        listOf(
            "executionEvidenceValidation",
            "observationAuthorityRemainsBoundedObservationAuthority",
            "observationEvidenceRequiresGenuineExecutionAttempt",
            "observationEvidenceCannotBecomeVerification",
            "verificationAuthorityRemainsBoundedVerificationAuthority",
            "verificationEvidenceRequiresGenuineObservation",
            "verifiedStatusCannotBecomeOutcome",
            "observationVerificationTraceAndResultInvariantsPreserved",
            "downstreamCapabilitiesCannotCreateOrReplaceObservationOrVerificationAuthority",
        ).forEach { required ->
            assertTrue(
                fieldNames.contains(required),
                "Missing Stage 293 evidence property: $required",
            )
        }
    }

    @Test
    fun `Stage 293 preserves Observation Verification separation and stops before Stage 294`() {
        val source = stage293Source()

        listOf(
            "OBSERVATION_VERIFICATION_VALIDATION != OBSERVATION.",
            "OBSERVATION_VERIFICATION_VALIDATION != VERIFICATION.",
            "OBSERVATION_VERIFICATION_VALIDATION != VERIFIED_OUTCOME.",
            "EXECUTION_ATTEMPTED != OBSERVED.",
            "OBSERVATION_EVIDENCE != OBSERVATION.",
            "OBSERVATION != VERIFICATION.",
            "VERIFICATION_EVIDENCE != VERIFICATION.",
            "VERIFIED != OUTCOME.",
        ).forEach { boundary ->
            assertTrue(
                source.contains(boundary),
                "Missing Stage 293 boundary: $boundary",
            )
        }

        assertTrue(
            source.contains(
                "implement Stage 294 World Model & Learning Validation",
            ),
        )
    }

    @Test
    fun `Stage 293 preserves independent verification trace checks`() {
        val source = verificationAuthoritySource()

        val observationCheck =
            "require(observation.traceId == context.traceId) {"
        val evidenceCheck =
            "require(verificationEvidence.traceId == context.traceId) {"

        assertTrue(source.contains(observationCheck))
        assertTrue(source.contains(evidenceCheck))

        val observationIndex = source.indexOf(observationCheck)
        val observationMessageIndex =
            source.indexOf(
                "\"Context and observation result must use the same trace identity.\"",
                observationIndex,
            )
        val evidenceIndex = source.indexOf(evidenceCheck)

        assertTrue(observationIndex >= 0)
        assertTrue(observationMessageIndex > observationIndex)
        assertTrue(evidenceIndex > observationMessageIndex)
    }

    @Test
    fun `Stage 293 introduces no operational Observation Verification or Outcome wiring`() {
        val source = stage293Source()

        listOf(
            "DefaultObservationAuthority(",
            "DefaultObservationEvidencePort(",
            "DefaultVerificationAuthority(",
            "DefaultVerificationEvidencePort(",
            "DefaultOutcomeAuthority(",
            "UnifiedDevilRuntime(",
            "ObservationRequest(",
            "VerificationRequest(",
            "OutcomeRequest(",
            "Runtime.getRuntime(",
            "ProcessBuilder(",
            "HttpURLConnection",
            "OkHttpClient",
        ).forEach { forbidden ->
            assertTrue(
                !source.contains(forbidden),
                "Stage 293 must not introduce operational or future-stage wiring: $forbidden",
            )
        }
    }

    private fun stage293Source(): String {
        val candidates =
            listOf(
                File(
                    "src/main/kotlin/com/devil/app/constitutionalvalidation/DevilObservationVerificationValidation.kt",
                ),
                File(
                    "app/src/main/kotlin/com/devil/app/constitutionalvalidation/DevilObservationVerificationValidation.kt",
                ),
            )

        return candidates
            .firstOrNull { it.isFile }
            ?.readText()
            ?: error(
                "Unable to locate Stage 293 production source from: ${candidates.joinToString()}",
            )
    }

    private fun verificationAuthoritySource(): String {
        val candidates =
            listOf(
                File(
                    "../core/runtime/src/main/kotlin/com/devil/core/runtime/verification/DefaultVerificationAuthority.kt",
                ),
                File(
                    "core/runtime/src/main/kotlin/com/devil/core/runtime/verification/DefaultVerificationAuthority.kt",
                ),
            )

        return candidates
            .firstOrNull { it.isFile }
            ?.readText()
            ?: error(
                "Unable to locate DefaultVerificationAuthority source from: ${candidates.joinToString()}",
            )
    }
}
