package com.devil.app.constitutionalvalidation

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Stage 288 Planner Boundary Validation governance tests.
 *
 * Stage 288 validates the existing constitutional Planner / Plan Authority
 * boundary only.
 *
 * It must not become Planner, Plan Authority, Brain, Executive, execution,
 * constitutional Verification, or Stage 289 Executive Boundary Validation.
 */
class Stage288PlannerBoundaryValidationTest {

    @Test
    fun `Stage 288 validation status remains architectural only`() {
        assertEquals(
            DevilPlannerBoundaryValidationStatus.VALIDATED,
            DevilPlannerBoundaryValidationStatus.valueOf("VALIDATED"),
        )

        assertEquals(
            DevilPlannerBoundaryValidationStatus.NOT_VALIDATED,
            DevilPlannerBoundaryValidationStatus.valueOf("NOT_VALIDATED"),
        )
    }

    @Test
    fun `Stage 288 evidence contract retains Stage 287 provenance field`() {
        val fieldNames =
            DevilPlannerBoundaryValidationEvidence::class.java
                .declaredFields
                .map { it.name }
                .toSet()

        listOf(
            "brainAuthorityValidation",
            "planAuthorityRemainsBoundedPlanCreationAuthority",
            "decisionAndTaskRemainUpstreamOfPlanning",
            "planningRemainsUpstreamOfCapabilitySelectionAndExecutive",
            "plannerCannotChangeEstablishedGoalOrOwnerIntent",
            "planAuthorityCannotGrantAuthorizationOrBecomeBrainDecisionAuthority",
            "planTraceAndResultInvariantsPreserved",
            "downstreamCapabilitiesCannotCreateOrReplacePlannerAuthority",
        ).forEach { required ->
            assertTrue(
                fieldNames.contains(required),
                "Missing Stage 288 evidence property: $required",
            )
        }
    }

    @Test
    fun `Stage 288 preserves Planner boundaries and stops before Stage 289`() {
        val source = stage288Source()

        listOf(
            "PLANNER_BOUNDARY_VALIDATION != PLANNING.",
            "PLANNER_BOUNDARY_VALIDATION != PLAN_AUTHORITY.",
            "PLANNER_BOUNDARY_VALIDATION != BRAIN_DECISION.",
            "PLANNER_BOUNDARY_VALIDATION != AUTHORIZATION.",
            "PLANNER_BOUNDARY_VALIDATION != CAPABILITY_SELECTION.",
            "PLANNER_BOUNDARY_VALIDATION != EXECUTION.",
            "PLANNER_BOUNDARY_VALIDATION != CONSTITUTIONAL_VERIFICATION.",
            "PLANNER_BOUNDARY_VALIDATION != VERIFIED_OUTCOME.",
            "PLANNER != BRAIN.",
            "PLAN != EXECUTION.",
        ).forEach { boundary ->
            assertTrue(
                source.contains(boundary),
                "Missing Stage 288 boundary: $boundary",
            )
        }

        assertTrue(
            source.contains(
                "implement Stage 289 Executive Boundary Validation",
            ),
        )
    }

    @Test
    fun `Stage 288 introduces no operational Planner Executive or execution wiring`() {
        val source = stage288Source()

        listOf(
            "DefaultPlanAuthority(",
            "DefaultExecutiveReadinessAuthority(",
            "UnifiedDevilRuntime(",
            "PlanRecord(",
            "ExecutionRequest(",
            "Runtime.getRuntime(",
            "ProcessBuilder(",
            "HttpURLConnection",
            "OkHttpClient",
        ).forEach { forbidden ->
            assertTrue(
                !source.contains(forbidden),
                "Stage 288 must not introduce operational or future-stage wiring: $forbidden",
            )
        }
    }

    private fun stage288Source(): String {
        val candidates =
            listOf(
                File(
                    "src/main/kotlin/com/devil/app/constitutionalvalidation/DevilPlannerBoundaryValidation.kt",
                ),
                File(
                    "app/src/main/kotlin/com/devil/app/constitutionalvalidation/DevilPlannerBoundaryValidation.kt",
                ),
            )

        return candidates
            .firstOrNull { it.isFile }
            ?.readText()
            ?: error(
                "Unable to locate Stage 288 production source from: ${candidates.joinToString()}",
            )
    }
}
