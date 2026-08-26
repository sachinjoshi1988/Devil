package com.devil.app.constitutionalvalidation

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Stage 289 Executive Boundary Validation governance tests.
 *
 * Stage 289 validates the existing constitutional Executive / Executive Readiness
 * Authority boundary only.
 *
 * It must not become Executive, Executive Readiness Authority, execution,
 * constitutional Verification, or Stage 290 Security Authority Validation.
 */
class Stage289ExecutiveBoundaryValidationTest {

    @Test
    fun `Stage 289 validation status remains architectural only`() {
        assertEquals(
            DevilExecutiveBoundaryValidationStatus.VALIDATED,
            DevilExecutiveBoundaryValidationStatus.valueOf("VALIDATED"),
        )

        assertEquals(
            DevilExecutiveBoundaryValidationStatus.NOT_VALIDATED,
            DevilExecutiveBoundaryValidationStatus.valueOf("NOT_VALIDATED"),
        )
    }

    @Test
    fun `Stage 289 evidence contract retains Stage 288 provenance field`() {
        val fieldNames =
            DevilExecutiveBoundaryValidationEvidence::class.java
                .declaredFields
                .map { it.name }
                .toSet()

        listOf(
            "plannerBoundaryValidation",
            "executiveReadinessRemainsBoundedReadinessAuthority",
            "planAndCapabilityRemainUpstreamOfExecutiveReadiness",
            "executiveReadinessRemainsUpstreamOfExecution",
            "executiveReadinessCannotGrantAuthorizationOrSelectCapability",
            "readyStatusCannotBecomeExecutionRequestOrExecution",
            "executiveTraceAndResultInvariantsPreserved",
            "downstreamCapabilitiesCannotCreateOrReplaceExecutiveAuthority",
        ).forEach { required ->
            assertTrue(
                fieldNames.contains(required),
                "Missing Stage 289 evidence property: $required",
            )
        }
    }

    @Test
    fun `Stage 289 preserves Executive boundaries and stops before Stage 290`() {
        val source = stage289Source()

        listOf(
            "EXECUTIVE_BOUNDARY_VALIDATION != EXECUTIVE_READINESS.",
            "EXECUTIVE_BOUNDARY_VALIDATION != EXECUTION_REQUEST.",
            "EXECUTIVE_BOUNDARY_VALIDATION != EXECUTION.",
            "EXECUTIVE_BOUNDARY_VALIDATION != AUTHORIZATION.",
            "EXECUTIVE_BOUNDARY_VALIDATION != CAPABILITY_SELECTION.",
            "EXECUTIVE_BOUNDARY_VALIDATION != CONSTITUTIONAL_VERIFICATION.",
            "EXECUTIVE_BOUNDARY_VALIDATION != VERIFIED_OUTCOME.",
            "CAPABILITY_SELECTED != EXECUTIVE_READY.",
            "EXECUTIVE_READY != EXECUTION_REQUEST.",
            "EXECUTIVE_READY != EXECUTION.",
        ).forEach { boundary ->
            assertTrue(
                source.contains(boundary),
                "Missing Stage 289 boundary: $boundary",
            )
        }

        assertTrue(
            source.contains(
                "implement Stage 290 Security Authority Validation",
            ),
        )
    }

    @Test
    fun `Stage 289 introduces no operational Executive execution or future stage wiring`() {
        val source = stage289Source()

        listOf(
            "DefaultExecutiveReadinessAuthority(",
            "DefaultExecutionAuthority(",
            "UnifiedDevilRuntime(",
            "ExecutiveReadinessRequest(",
            "ExecutionRequest(",
            "Runtime.getRuntime(",
            "ProcessBuilder(",
            "HttpURLConnection",
            "OkHttpClient",
        ).forEach { forbidden ->
            assertTrue(
                !source.contains(forbidden),
                "Stage 289 must not introduce operational or future-stage wiring: $forbidden",
            )
        }
    }

    private fun stage289Source(): String {
        val candidates =
            listOf(
                File(
                    "src/main/kotlin/com/devil/app/constitutionalvalidation/DevilExecutiveBoundaryValidation.kt",
                ),
                File(
                    "app/src/main/kotlin/com/devil/app/constitutionalvalidation/DevilExecutiveBoundaryValidation.kt",
                ),
            )

        return candidates
            .firstOrNull { it.isFile }
            ?.readText()
            ?: error(
                "Unable to locate Stage 289 production source from: ${candidates.joinToString()}",
            )
    }
}
