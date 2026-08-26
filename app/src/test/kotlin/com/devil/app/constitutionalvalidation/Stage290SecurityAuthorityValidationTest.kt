package com.devil.app.constitutionalvalidation

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Stage 290 Security Authority Validation governance tests.
 *
 * Stage 290 validates the existing constitutional Security Authority boundary only.
 *
 * It must not become Security Authority, authentication, authorization, execution,
 * constitutional Verification, or Stage 291 Memory Authority Validation.
 */
class Stage290SecurityAuthorityValidationTest {

    @Test
    fun `Stage 290 validation status remains architectural only`() {
        assertEquals(
            DevilSecurityAuthorityValidationStatus.VALIDATED,
            DevilSecurityAuthorityValidationStatus.valueOf("VALIDATED"),
        )

        assertEquals(
            DevilSecurityAuthorityValidationStatus.NOT_VALIDATED,
            DevilSecurityAuthorityValidationStatus.valueOf("NOT_VALIDATED"),
        )
    }

    @Test
    fun `Stage 290 evidence contract retains Stage 289 provenance field`() {
        val fieldNames =
            DevilSecurityAuthorityValidationEvidence::class.java
                .declaredFields
                .map { it.name }
                .toSet()

        listOf(
            "executiveBoundaryValidation",
            "securityTransitionAuthorityRemainsBoundedSecurityStateEvaluationAuthority",
            "sessionValidityAuthorityRemainsBoundedSessionValidityEvaluationAuthority",
            "securityStateAndSessionStateRemainDistinct",
            "securityAuthorityRemainsSeparateFromIdentityTrustAuthorizationAndOwnerSecurityModes",
            "securityTransitionAndSessionValidityCannotGrantExecutionAuthority",
            "securityTraceAndResultInvariantsPreserved",
            "downstreamCapabilitiesCannotCreateOrReplaceSecurityAuthority",
        ).forEach { required ->
            assertTrue(
                fieldNames.contains(required),
                "Missing Stage 290 evidence property: $required",
            )
        }
    }

    @Test
    fun `Stage 290 preserves Security Authority boundaries and stops before Stage 291`() {
        val source = stage290Source()

        listOf(
            "SECURITY_AUTHORITY_VALIDATION != SECURITY_TRANSITION.",
            "SECURITY_AUTHORITY_VALIDATION != SESSION_VALIDATION.",
            "SECURITY_AUTHORITY_VALIDATION != AUTHENTICATION.",
            "SECURITY_AUTHORITY_VALIDATION != TRUST.",
            "SECURITY_AUTHORITY_VALIDATION != AUTHORIZATION.",
            "SECURITY_AUTHORITY_VALIDATION != OWNER_MODE.",
            "SECURITY_AUTHORITY_VALIDATION != HIGH_SECURITY_CONFIRMATION.",
            "SECURITY_AUTHORITY_VALIDATION != EXECUTION.",
            "SECURITY_AUTHORITY_VALIDATION != CONSTITUTIONAL_VERIFICATION.",
            "SECURITY_AUTHORITY_VALIDATION != VERIFIED_OUTCOME.",
        ).forEach { boundary ->
            assertTrue(
                source.contains(boundary),
                "Missing Stage 290 boundary: $boundary",
            )
        }

        assertTrue(
            source.contains(
                "implement Stage 291 Memory Authority Validation",
            ),
        )
    }

    @Test
    fun `Stage 290 introduces no operational Security Authority session or execution wiring`() {
        val source = stage290Source()

        listOf(
            "DefaultSecurityTransitionAuthority(",
            "DefaultSessionValidityAuthority(",
            "DefaultExecutionAuthority(",
            "UnifiedDevilRuntime(",
            "ExecutionRequest(",
            "Runtime.getRuntime(",
            "ProcessBuilder(",
            "HttpURLConnection",
            "OkHttpClient",
        ).forEach { forbidden ->
            assertTrue(
                !source.contains(forbidden),
                "Stage 290 must not introduce operational or future-stage wiring: $forbidden",
            )
        }
    }

    private fun stage290Source(): String {
        val candidates =
            listOf(
                File(
                    "src/main/kotlin/com/devil/app/constitutionalvalidation/DevilSecurityAuthorityValidation.kt",
                ),
                File(
                    "app/src/main/kotlin/com/devil/app/constitutionalvalidation/DevilSecurityAuthorityValidation.kt",
                ),
            )

        return candidates
            .firstOrNull { it.isFile }
            ?.readText()
            ?: error(
                "Unable to locate Stage 290 production source from: ${candidates.joinToString()}",
            )
    }
}
