package com.devil.app.constitutionalvalidation

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Stage 294 World Model & Learning Validation governance tests.
 *
 * Stage 294 validates existing constitutional World Model and Learning boundaries only.
 *
 * It must not mutate World Model state, perform Learning, operate Memory, grant
 * Controlled Autonomy, or implement Stage 295 Controlled Autonomy Validation.
 */
class Stage294WorldModelLearningValidationTest {

    @Test
    fun `Stage 294 validation status remains architectural only`() {
        assertEquals(
            DevilWorldModelLearningValidationStatus.VALIDATED,
            DevilWorldModelLearningValidationStatus.valueOf("VALIDATED"),
        )

        assertEquals(
            DevilWorldModelLearningValidationStatus.NOT_VALIDATED,
            DevilWorldModelLearningValidationStatus.valueOf("NOT_VALIDATED"),
        )
    }

    @Test
    fun `Stage 294 evidence retains Stage 293 provenance and World Model Learning boundaries`() {
        val fieldNames =
            DevilWorldModelLearningValidationEvidence::class.java
                .declaredFields
                .map { it.name }
                .toSet()

        listOf(
            "observationVerificationValidation",
            "worldModelUpdateRequiresEstablishedOutcomeAndEvidence",
            "worldModelUpdateEvidenceCannotBecomeWorldStateMutation",
            "worldModelUpdateRemainsSeparateFromLearningEvidence",
            "learningRequiresWorldModelUpdateAndIndependentLearningEvidence",
            "learningCannotBecomeMemoryProposalOrMemoryAuthorityApproval",
            "worldModelAndLearningTraceAndResultInvariantsPreserved",
            "downstreamCapabilitiesCannotCreateOrReplaceWorldModelOrLearningAuthority",
            "worldModelLearningCannotGrantControlledAutonomy",
        ).forEach { required ->
            assertTrue(
                fieldNames.contains(required),
                "Missing Stage 294 evidence property: $required",
            )
        }
    }

    @Test
    fun `Stage 294 preserves World Model Learning separation and stops before Stage 295`() {
        val source = stage294Source()

        listOf(
            "WORLD_MODEL_LEARNING_VALIDATION != WORLD_MODEL_UPDATE.",
            "WORLD_MODEL_LEARNING_VALIDATION != WORLD_STATE_MUTATION.",
            "WORLD_MODEL_LEARNING_VALIDATION != LEARNING.",
            "WORLD_MODEL_LEARNING_VALIDATION != MEMORY_PROPOSAL.",
            "WORLD_MODEL_LEARNING_VALIDATION != MEMORY_AUTHORITY_APPROVAL.",
            "WORLD_MODEL_LEARNING_VALIDATION != CONTROLLED_AUTONOMY.",
            "OUTCOME != WORLD_MODEL_UPDATE_EVIDENCE.",
            "WORLD_MODEL_UPDATE_EVIDENCE != WORLD_MODEL_UPDATE.",
            "WORLD_MODEL_UPDATE != WORLD_STATE_CHANGED.",
            "WORLD_MODEL_UPDATE != LEARNING_EVIDENCE.",
            "LEARNING_EVIDENCE != LEARNING.",
            "LEARNING != MEMORY_PROPOSAL.",
        ).forEach { boundary ->
            assertTrue(
                source.contains(boundary),
                "Missing Stage 294 boundary: $boundary",
            )
        }

        assertTrue(
            source.contains(
                "implement Stage 295 Controlled Autonomy Validation",
            ),
        )
    }

    @Test
    fun `Stage 294 introduces no operational World Model Learning Memory or autonomy wiring`() {
        val source = stage294Source()

        listOf(
            "DefaultWorldModelUpdateAuthority(",
            "DefaultWorldModelUpdateEvidencePort(",
            "DefaultLearningAuthority(",
            "DefaultLearningEvidencePort(",
            "ControlledAutonomyCoordinator(",
            "DefaultMemoryAuthority(",
            "DefaultMemoryCommitmentAuthority(",
            "DefaultMemoryPersistenceAuthority(",
            "UnifiedDevilRuntime(",
            "WorldModelUpdateRequest(",
            "LearningRequest(",
            "MemoryProposalRequest(",
            "Runtime.getRuntime(",
            "ProcessBuilder(",
            "HttpURLConnection",
            "OkHttpClient",
        ).forEach { forbidden ->
            assertTrue(
                !source.contains(forbidden),
                "Stage 294 must not introduce operational or future-stage wiring: $forbidden",
            )
        }
    }

    private fun stage294Source(): String {
        val candidates =
            listOf(
                File(
                    "src/main/kotlin/com/devil/app/constitutionalvalidation/DevilWorldModelLearningValidation.kt",
                ),
                File(
                    "app/src/main/kotlin/com/devil/app/constitutionalvalidation/DevilWorldModelLearningValidation.kt",
                ),
            )

        return candidates
            .firstOrNull { it.isFile }
            ?.readText()
            ?: error(
                "Unable to locate Stage 294 production source from: ${candidates.joinToString()}",
            )
    }
}
