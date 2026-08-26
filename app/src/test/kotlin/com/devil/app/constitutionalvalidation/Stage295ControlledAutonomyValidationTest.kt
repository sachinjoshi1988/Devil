package com.devil.app.constitutionalvalidation

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Stage 295 Controlled Autonomy Validation governance tests.
 *
 * Stage 295 validates the existing bounded Controlled Autonomy preparation
 * architecture only.
 *
 * It must not become an autonomy grant, Authorization Authority, Brain, Planner,
 * Executive, Execution Authority, Memory Authority, automatic continuation engine,
 * or Stage 296 Unit Test Completion.
 */
class Stage295ControlledAutonomyValidationTest {

    @Test
    fun `Stage 295 validation status remains architectural only`() {
        assertEquals(
            DevilControlledAutonomyValidationStatus.VALIDATED,
            DevilControlledAutonomyValidationStatus.valueOf("VALIDATED"),
        )

        assertEquals(
            DevilControlledAutonomyValidationStatus.NOT_VALIDATED,
            DevilControlledAutonomyValidationStatus.valueOf("NOT_VALIDATED"),
        )
    }

    @Test
    fun `Stage 295 evidence retains Stage 294 provenance and autonomy boundaries`() {
        val fieldNames =
            DevilControlledAutonomyValidationEvidence::class.java
                .declaredFields
                .map { it.name }
                .toSet()

        listOf(
            "worldModelLearningValidation",
            "controlledAutonomyRemainsPreparationOnly",
            "preparationRequiresExistingStrategyAdaptationProvenance",
            "preparedStatusCannotBecomeAutonomyGrant",
            "controlledAutonomyCannotGrantAuthorizationOrBrainDecision",
            "controlledAutonomyCannotPerformPlanningOrEstablishExecutiveReadiness",
            "controlledAutonomyCannotCreateExecutionRequestOrPerformExecution",
            "controlledAutonomyCannotOperateMemoryAuthorityOrPersistence",
            "controlledAutonomyCannotScheduleTriggerRetryOrAutomaticallyContinueWork",
            "controlledAutonomyTraceAndResultInvariantsPreserved",
        ).forEach { required ->
            assertTrue(
                fieldNames.contains(required),
                "Missing Stage 295 evidence property: $required",
            )
        }
    }

    @Test
    fun `Stage 295 preserves Controlled Autonomy separation and stops before Stage 296`() {
        val source = stage295Source()

        listOf(
            "CONTROLLED_AUTONOMY_VALIDATION != AUTONOMY_GRANT.",
            "CONTROLLED_AUTONOMY_VALIDATION != AUTHORIZATION.",
            "CONTROLLED_AUTONOMY_VALIDATION != BRAIN_DECISION.",
            "CONTROLLED_AUTONOMY_VALIDATION != PLANNING.",
            "CONTROLLED_AUTONOMY_VALIDATION != EXECUTIVE_READINESS.",
            "CONTROLLED_AUTONOMY_VALIDATION != EXECUTION.",
            "CONTROLLED_AUTONOMY_VALIDATION != MEMORY_AUTHORITY.",
            "STRATEGY_ADAPTATION != CONTROLLED_AUTONOMY.",
            "CONTROLLED_AUTONOMY_RECORD != AUTONOMY_GRANT.",
            "CONTROLLED_AUTONOMY_PREPARATION != AUTONOMY_GRANT.",
            "PREPARED != AUTHORIZED.",
            "PREPARED != READY.",
            "PREPARED != EXECUTED.",
        ).forEach { boundary ->
            assertTrue(
                source.contains(boundary),
                "Missing Stage 295 boundary: $boundary",
            )
        }

        assertTrue(
            source.contains("implement Stage 296 Unit Test Completion"),
        )
    }

    @Test
    fun `Stage 295 introduces no operational autonomy authority execution or memory wiring`() {
        val source = stage295Source()

        listOf(
            "ControlledAutonomyCoordinator(",
            "ControlledAutonomyRecord(",
            "DefaultAuthorizationAuthority(",
            "DefaultDecisionAuthority(",
            "DefaultPlanAuthority(",
            "DefaultExecutiveReadinessAuthority(",
            "DefaultExecutionAuthority(",
            "DefaultMemoryAuthority(",
            "DefaultMemoryCommitmentAuthority(",
            "DefaultMemoryPersistenceAuthority(",
            "UnifiedDevilRuntime(",
            "ExecutionRequest(",
            "Runtime.getRuntime(",
            "ProcessBuilder(",
            "HttpURLConnection",
            "OkHttpClient",
        ).forEach { forbidden ->
            assertTrue(
                !source.contains(forbidden),
                "Stage 295 must not introduce operational or future-stage wiring: $forbidden",
            )
        }
    }

    private fun stage295Source(): String {
        val candidates =
            listOf(
                File(
                    "src/main/kotlin/com/devil/app/constitutionalvalidation/DevilControlledAutonomyValidation.kt",
                ),
                File(
                    "app/src/main/kotlin/com/devil/app/constitutionalvalidation/DevilControlledAutonomyValidation.kt",
                ),
            )

        return candidates
            .firstOrNull { it.isFile }
            ?.readText()
            ?: error(
                "Unable to locate Stage 295 production source from: ${candidates.joinToString()}",
            )
    }
}
