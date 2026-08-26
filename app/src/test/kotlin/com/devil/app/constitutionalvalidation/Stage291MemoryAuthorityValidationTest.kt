package com.devil.app.constitutionalvalidation

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Stage 291 Memory Authority Validation governance tests.
 *
 * Stage 291 validates the existing single constitutional Memory Authority boundary
 * only.
 *
 * It must not become Memory Authority, Memory Commitment, Memory Persistence,
 * memory recall, execution, or constitutional Verification.
 */
class Stage291MemoryAuthorityValidationTest {

    @Test
    fun `Stage 291 validation status remains architectural only`() {
        assertEquals(
            DevilMemoryAuthorityValidationStatus.VALIDATED,
            DevilMemoryAuthorityValidationStatus.valueOf("VALIDATED"),
        )

        assertEquals(
            DevilMemoryAuthorityValidationStatus.NOT_VALIDATED,
            DevilMemoryAuthorityValidationStatus.valueOf("NOT_VALIDATED"),
        )
    }

    @Test
    fun `Stage 291 evidence contract retains Stage 290 provenance and Memory boundaries`() {
        val fieldNames =
            DevilMemoryAuthorityValidationEvidence::class.java
                .declaredFields
                .map { it.name }
                .toSet()

        listOf(
            "securityAuthorityValidation",
            "singleConstitutionalMemoryAuthorityPreserved",
            "learningAndMemoryProposalRemainUpstreamOfMemoryAuthority",
            "committableMeansEligibilityOnlyNotCommitmentOrPersistence",
            "memoryAuthorityRemainsSeparateFromCommitmentAndPersistence",
            "memoryCommitmentRemainsUpstreamOfMemoryPersistence",
            "memoryAuthorityCannotGrantAuthorizationOrPerformExecution",
            "memoryTraceAndResultInvariantsPreserved",
            "downstreamMemoryCapabilitiesCannotCreateOrReplaceMemoryAuthority",
        ).forEach { required ->
            assertTrue(
                fieldNames.contains(required),
                "Missing Stage 291 evidence property: $required",
            )
        }
    }

    @Test
    fun `Stage 291 preserves Memory Authority constitutional boundaries`() {
        val source = stage291Source()

        listOf(
            "MEMORY_AUTHORITY_VALIDATION != MEMORY_AUTHORITY_APPROVAL.",
            "MEMORY_AUTHORITY_VALIDATION != MEMORY_COMMITMENT.",
            "MEMORY_AUTHORITY_VALIDATION != MEMORY_PERSISTENCE.",
            "MEMORY_AUTHORITY_VALIDATION != MEMORY_RECALL.",
            "MEMORY_AUTHORITY_VALIDATION != AUTHORIZATION.",
            "MEMORY_AUTHORITY_VALIDATION != EXECUTION.",
            "MEMORY_AUTHORITY_VALIDATION != CONSTITUTIONAL_VERIFICATION.",
            "MEMORY_AUTHORITY_VALIDATION != VERIFIED_OUTCOME.",
            "MEMORY_PROPOSAL != MEMORY_AUTHORITY_APPROVAL.",
            "MEMORY_AUTHORITY_APPROVAL != MEMORY_COMMITMENT.",
            "MEMORY_COMMITMENT != MEMORY_PERSISTENCE.",
        ).forEach { boundary ->
            assertTrue(
                source.contains(boundary),
                "Missing Stage 291 boundary: $boundary",
            )
        }
    }

    @Test
    fun `Stage 291 introduces no operational Memory Authority storage or execution wiring`() {
        val source = stage291Source()

        listOf(
            "DefaultMemoryAuthority(",
            "DefaultMemoryCommitmentAuthority(",
            "DefaultMemoryPersistenceAuthority(",
            "UnifiedDevilRuntime(",
            "MemoryAuthorityRequest(",
            "MemoryCommitmentRequest(",
            "MemoryPersistenceRequest(",
            "ExecutionRequest(",
            "Runtime.getRuntime(",
            "ProcessBuilder(",
            "HttpURLConnection",
            "OkHttpClient",
        ).forEach { forbidden ->
            assertTrue(
                !source.contains(forbidden),
                "Stage 291 must not introduce operational Memory, storage, execution, or future-stage wiring: $forbidden",
            )
        }
    }

    private fun stage291Source(): String {
        val candidates =
            listOf(
                File(
                    "src/main/kotlin/com/devil/app/constitutionalvalidation/DevilMemoryAuthorityValidation.kt",
                ),
                File(
                    "app/src/main/kotlin/com/devil/app/constitutionalvalidation/DevilMemoryAuthorityValidation.kt",
                ),
            )

        return candidates
            .firstOrNull { it.isFile }
            ?.readText()
            ?: error(
                "Unable to locate Stage 291 production source from: ${candidates.joinToString()}",
            )
    }
}
