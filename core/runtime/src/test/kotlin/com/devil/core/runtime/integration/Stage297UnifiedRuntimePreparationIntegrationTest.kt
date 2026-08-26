package com.devil.core.runtime.integration

import com.devil.core.model.capability.CapabilityId
import com.devil.core.model.common.TraceId
import com.devil.core.model.outcome.OutcomeState
import com.devil.core.model.worldmodel.WorldModelRepresentation
import com.devil.core.runtime.autonomy.ControlledAutonomyPreparationStatus
import com.devil.core.runtime.learning.EvidenceBasedLearningPreparationStatus
import com.devil.core.runtime.learning.FailureLearningPreparationStatus
import com.devil.core.runtime.learning.StrategyAdaptationPreparationStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

/**
 * Stage 297 Integration Test Completion coverage for the established
 * bounded Unified Runtime preparation integration chain.
 *
 * This test surface validates existing Stage 92 -> 95 integration behavior
 * and provenance only.
 *
 * PREPARED != AUTHORIZED.
 * PREPARED != EXECUTED.
 * PREPARED != VERIFIED.
 *
 * Stage 297 does not modify UnifiedDevilRuntime, Stage 49 runtime ordering,
 * constitutional authorities, or implement Stage 298 End-to-End
 * Constitutional Tests.
 */
class Stage297UnifiedRuntimePreparationIntegrationTest {

    @Test
    fun `existing stage 92 through 95 preparation chain integrates with exact provenance`() {
        val traceId =
            TraceId.from(
                "trace-stage297-runtime-integration",
            )

        val representation =
            WorldModelRepresentation.create(
                traceId = traceId,
                capabilityId =
                    CapabilityId.from(
                        "capability-stage297-integration-source",
                    ),
                description =
                    "Stage 297 integration provenance.",
            )

        val result =
            UnifiedRuntimeIntegrationCoordinator().prepare(
                traceId = traceId,
                worldModelRepresentation = representation,
                proposition =
                    "Evidence-backed material may enter bounded constitutional Learning.",
                outcomeState =
                    OutcomeState.VERIFIED_FAILURE,
                lesson =
                    "The verified attempted path did not achieve its intended outcome.",
                adaptedStrategy =
                    "Consider another constitutionally governed bounded strategy.",
                scope =
                    "Permit later constitutional reconsideration only.",
            )

        assertEquals(
            EvidenceBasedLearningPreparationStatus.PREPARED,
            result.evidenceBasedLearning.status,
        )
        assertEquals(
            FailureLearningPreparationStatus.PREPARED,
            requireNotNull(result.failureLearning).status,
        )
        assertEquals(
            StrategyAdaptationPreparationStatus.PREPARED,
            requireNotNull(result.strategyAdaptation).status,
        )
        assertEquals(
            ControlledAutonomyPreparationStatus.PREPARED,
            requireNotNull(result.controlledAutonomy).status,
        )

        val evidenceBasedLearning =
            requireNotNull(result.evidenceBasedLearning.record)
        val failureLearning =
            requireNotNull(
                requireNotNull(result.failureLearning).record,
            )
        val strategyAdaptation =
            requireNotNull(
                requireNotNull(result.strategyAdaptation).record,
            )
        val controlledAutonomy =
            requireNotNull(
                requireNotNull(result.controlledAutonomy).record,
            )

        assertSame(
            representation,
            evidenceBasedLearning.worldModelRepresentation,
        )
        assertSame(
            evidenceBasedLearning,
            failureLearning.evidenceBasedLearning,
        )
        assertSame(
            failureLearning,
            strategyAdaptation.failureLearning,
        )
        assertSame(
            strategyAdaptation,
            controlledAutonomy.strategyAdaptation,
        )
    }
}
