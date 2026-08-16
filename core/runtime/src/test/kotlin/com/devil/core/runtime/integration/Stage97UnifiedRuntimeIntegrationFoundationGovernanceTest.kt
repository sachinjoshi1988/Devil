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
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage97UnifiedRuntimeIntegrationFoundationGovernanceTest {

    @Test
    fun `valid bounded inputs compose stage92 through stage95 preparation`() {
        val traceId =
            TraceId.from(
                "trace-stage97-integration-001",
            )

        val representation =
            representation(
                traceId = traceId,
            )

        val result =
            coordinator().prepare(
                traceId = traceId,
                worldModelRepresentation = representation,
                proposition =
                    "Evidence-backed information may be considered by constitutional Learning.",
                outcomeState =
                    OutcomeState.VERIFIED_FAILURE,
                lesson =
                    "The verified attempted path did not achieve its intended outcome.",
                adaptedStrategy =
                    "Consider a constitutionally governed alternative capability path.",
                scope =
                    "Permit later constitutional reconsideration of the bounded failed approach.",
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
    }

    @Test
    fun `integration preserves exact world model provenance through complete preparation chain`() {
        val traceId =
            TraceId.from(
                "trace-stage97-integration-002",
            )

        val representation =
            representation(
                traceId = traceId,
            )

        val result =
            coordinator().prepare(
                traceId = traceId,
                worldModelRepresentation = representation,
                proposition =
                    "Preserve exact evidence-backed provenance.",
                outcomeState =
                    OutcomeState.VERIFIED_FAILURE,
                lesson =
                    "Preserve the verified failure provenance.",
                adaptedStrategy =
                    "Consider another bounded planning approach.",
                scope =
                    "Bounded constitutional reconsideration only.",
            )

        val evidenceBasedLearning =
            requireNotNull(
                result.evidenceBasedLearning.record,
            )

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

        assertSame(
            representation,
            controlledAutonomy
                .strategyAdaptation
                .failureLearning
                .evidenceBasedLearning
                .worldModelRepresentation,
        )
    }

    @Test
    fun `stage92 deferral stops all later integration preparation`() {
        val traceId =
            TraceId.from(
                "trace-stage97-integration-003",
            )

        val result =
            coordinator().prepare(
                traceId = traceId,
                worldModelRepresentation =
                    representation(traceId),
                proposition = "   ",
                outcomeState =
                    OutcomeState.VERIFIED_FAILURE,
                lesson =
                    "This must never reach Failure Learning.",
                adaptedStrategy =
                    "This must never reach Strategy Adaptation.",
                scope =
                    "This must never reach Controlled Autonomy.",
            )

        assertEquals(
            EvidenceBasedLearningPreparationStatus.DEFERRED,
            result.evidenceBasedLearning.status,
        )

        assertNull(
            result.evidenceBasedLearning.record,
        )

        assertNull(result.failureLearning)
        assertNull(result.strategyAdaptation)
        assertNull(result.controlledAutonomy)
    }

    @Test
    fun `cross trace world model provenance fails closed at stage92 boundary`() {
        val result =
            coordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage97-integration-004",
                    ),
                worldModelRepresentation =
                    representation(
                        TraceId.from(
                            "trace-stage97-integration-other",
                        ),
                    ),
                proposition =
                    "Cross-trace evidence must not enter integration.",
                outcomeState =
                    OutcomeState.VERIFIED_FAILURE,
                lesson =
                    "This must not be prepared.",
                adaptedStrategy =
                    "This must not be prepared.",
                scope =
                    "This must not be prepared.",
            )

        assertEquals(
            EvidenceBasedLearningPreparationStatus.DEFERRED,
            result.evidenceBasedLearning.status,
        )

        assertNull(
            result.evidenceBasedLearning.record,
        )

        assertNull(result.failureLearning)
        assertNull(result.strategyAdaptation)
        assertNull(result.controlledAutonomy)
    }

    @Test
    fun `non failure outcome stops integration at stage93 boundary`() {
        val traceId =
            TraceId.from(
                "trace-stage97-integration-005",
            )

        val result =
            coordinator().prepare(
                traceId = traceId,
                worldModelRepresentation =
                    representation(traceId),
                proposition =
                    "Evidence-backed information may be considered.",
                outcomeState =
                    OutcomeState.VERIFIED_SUCCESS,
                lesson =
                    "Success must not be reclassified as Failure Learning.",
                adaptedStrategy =
                    "This must never reach Strategy Adaptation.",
                scope =
                    "This must never reach Controlled Autonomy.",
            )

        assertEquals(
            EvidenceBasedLearningPreparationStatus.PREPARED,
            result.evidenceBasedLearning.status,
        )

        val failureLearning =
            requireNotNull(result.failureLearning)

        assertEquals(
            FailureLearningPreparationStatus.DEFERRED,
            failureLearning.status,
        )

        assertNull(failureLearning.record)
        assertNull(result.strategyAdaptation)
        assertNull(result.controlledAutonomy)
    }

    @Test
    fun `blank failure lesson stops integration at stage93 boundary`() {
        val traceId =
            TraceId.from(
                "trace-stage97-integration-006",
            )

        val result =
            coordinator().prepare(
                traceId = traceId,
                worldModelRepresentation =
                    representation(traceId),
                proposition =
                    "Evidence-backed information may be considered.",
                outcomeState =
                    OutcomeState.VERIFIED_FAILURE,
                lesson = "   ",
                adaptedStrategy =
                    "This must never reach Strategy Adaptation.",
                scope =
                    "This must never reach Controlled Autonomy.",
            )

        assertEquals(
            EvidenceBasedLearningPreparationStatus.PREPARED,
            result.evidenceBasedLearning.status,
        )

        val failureLearning =
            requireNotNull(result.failureLearning)

        assertEquals(
            FailureLearningPreparationStatus.DEFERRED,
            failureLearning.status,
        )

        assertNull(failureLearning.record)
        assertNull(result.strategyAdaptation)
        assertNull(result.controlledAutonomy)
    }

    @Test
    fun `blank adapted strategy stops integration at stage94 boundary`() {
        val traceId =
            TraceId.from(
                "trace-stage97-integration-007",
            )

        val result =
            coordinator().prepare(
                traceId = traceId,
                worldModelRepresentation =
                    representation(traceId),
                proposition =
                    "Evidence-backed information may be considered.",
                outcomeState =
                    OutcomeState.VERIFIED_FAILURE,
                lesson =
                    "The verified attempted path did not achieve its intended outcome.",
                adaptedStrategy = "   ",
                scope =
                    "This must never reach Controlled Autonomy.",
            )

        assertEquals(
            EvidenceBasedLearningPreparationStatus.PREPARED,
            result.evidenceBasedLearning.status,
        )

        assertEquals(
            FailureLearningPreparationStatus.PREPARED,
            requireNotNull(result.failureLearning).status,
        )

        val strategyAdaptation =
            requireNotNull(result.strategyAdaptation)

        assertEquals(
            StrategyAdaptationPreparationStatus.DEFERRED,
            strategyAdaptation.status,
        )

        assertNull(strategyAdaptation.record)
        assertNull(result.controlledAutonomy)
    }

    @Test
    fun `blank autonomy scope remains deferred without fabricating autonomy`() {
        val traceId =
            TraceId.from(
                "trace-stage97-integration-008",
            )

        val result =
            coordinator().prepare(
                traceId = traceId,
                worldModelRepresentation =
                    representation(traceId),
                proposition =
                    "Evidence-backed information may be considered.",
                outcomeState =
                    OutcomeState.VERIFIED_FAILURE,
                lesson =
                    "The verified attempted path did not achieve its intended outcome.",
                adaptedStrategy =
                    "Consider a bounded alternative planning approach.",
                scope = "   ",
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

        val controlledAutonomy =
            requireNotNull(result.controlledAutonomy)

        assertEquals(
            ControlledAutonomyPreparationStatus.DEFERRED,
            controlledAutonomy.status,
        )

        assertNull(controlledAutonomy.record)
    }

    private fun coordinator():
        UnifiedRuntimeIntegrationCoordinator {
        return UnifiedRuntimeIntegrationCoordinator()
    }

    private fun representation(
        traceId: TraceId,
    ): WorldModelRepresentation {
        return WorldModelRepresentation.create(
            traceId = traceId,
            capabilityId =
                CapabilityId.from(
                    "capability-stage97-integration-source",
                ),
            description =
                "Evidence-backed World Model representation preserved for Stage 97 integration.",
        )
    }
}
