package com.devil.core.runtime.integration

import com.devil.core.model.common.TraceId
import com.devil.core.model.outcome.OutcomeState
import com.devil.core.model.worldmodel.WorldModelRepresentation
import com.devil.core.runtime.autonomy.ControlledAutonomyCoordinator
import com.devil.core.runtime.autonomy.ControlledAutonomyPreparationResult
import com.devil.core.runtime.learning.EvidenceBasedLearningCoordinator
import com.devil.core.runtime.learning.EvidenceBasedLearningPreparationResult
import com.devil.core.runtime.learning.EvidenceBasedLearningPreparationStatus
import com.devil.core.runtime.learning.FailureLearningCoordinator
import com.devil.core.runtime.learning.FailureLearningPreparationResult
import com.devil.core.runtime.learning.FailureLearningPreparationStatus
import com.devil.core.runtime.learning.StrategyAdaptationCoordinator
import com.devil.core.runtime.learning.StrategyAdaptationPreparationResult
import com.devil.core.runtime.learning.StrategyAdaptationPreparationStatus

/**
 * Stage 97 — Unified Runtime Integration Foundation.
 *
 * Coordinates the bounded Stage 92 -> Stage 95 preparation foundations without
 * absorbing any constitutional authority.
 *
 * All semantic inputs that cannot be derived constitutionally are supplied
 * explicitly by the caller:
 *
 * - proposition for Evidence-Based Learning;
 * - established OutcomeState for Failure Learning;
 * - lesson for Failure Learning;
 * - adaptedStrategy for Strategy Adaptation;
 * - scope for Controlled Autonomy.
 *
 * This coordinator never invents those values.
 *
 * Each later preparation is attempted only when the immediately preceding
 * preparation produced a record. Missing prerequisites therefore stop the
 * integration chain rather than being fabricated or bypassed.
 *
 * This coordinator does not:
 *
 * - create another Devil intelligence;
 * - create another Brain;
 * - replace UnifiedDevilRuntime;
 * - replace DefaultUnifiedDevilRuntime;
 * - become Constitution, Identity, Trust, Authorization, Understanding,
 *   Decision, Task, Plan, Capability, Executive, Execution, Observation,
 *   Verification, Outcome, World Model, Learning, or Memory Authority;
 * - establish evidence;
 * - establish OutcomeState;
 * - perform constitutional Learning;
 * - adopt an adapted strategy;
 * - grant Controlled Autonomy;
 * - create or alter a Decision, Task, or Plan;
 * - authorize a capability;
 * - establish Executive readiness;
 * - create an ExecutionRequest;
 * - execute an action;
 * - create a Memory Proposal;
 * - approve, commit, or persist Memory;
 * - or continue work autonomously.
 *
 * PREPARATION != AUTHORITY.
 * PREPARATION != AUTHORIZATION.
 * PREPARATION != DECISION.
 * PREPARATION != EXECUTION.
 * PREPARATION != AUTONOMY_GRANT.
 *
 * STAGE_97_INTEGRATION_FOUNDATION != CONSTITUTIONAL_CHAIN_REPLACEMENT.
 */
class UnifiedRuntimeIntegrationCoordinator(
    private val evidenceBasedLearningCoordinator:
        EvidenceBasedLearningCoordinator =
        EvidenceBasedLearningCoordinator(),
    private val failureLearningCoordinator:
        FailureLearningCoordinator =
        FailureLearningCoordinator(),
    private val strategyAdaptationCoordinator:
        StrategyAdaptationCoordinator =
        StrategyAdaptationCoordinator(),
    private val controlledAutonomyCoordinator:
        ControlledAutonomyCoordinator =
        ControlledAutonomyCoordinator(),
) {

    fun prepare(
        traceId: TraceId,
        worldModelRepresentation: WorldModelRepresentation,
        proposition: String,
        outcomeState: OutcomeState,
        lesson: String,
        adaptedStrategy: String,
        scope: String,
    ): UnifiedRuntimeIntegrationResult {
        val evidenceBasedLearning =
            evidenceBasedLearningCoordinator.prepare(
                traceId = traceId,
                worldModelRepresentation = worldModelRepresentation,
                proposition = proposition,
            )

        if (
            evidenceBasedLearning.status !=
            EvidenceBasedLearningPreparationStatus.PREPARED
        ) {
            return UnifiedRuntimeIntegrationResult(
                evidenceBasedLearning = evidenceBasedLearning,
            )
        }

        val evidenceBasedLearningRecord =
            requireNotNull(evidenceBasedLearning.record)

        val failureLearning =
            failureLearningCoordinator.prepare(
                traceId = traceId,
                evidenceBasedLearning = evidenceBasedLearningRecord,
                outcomeState = outcomeState,
                lesson = lesson,
            )

        if (
            failureLearning.status !=
            FailureLearningPreparationStatus.PREPARED
        ) {
            return UnifiedRuntimeIntegrationResult(
                evidenceBasedLearning = evidenceBasedLearning,
                failureLearning = failureLearning,
            )
        }

        val failureLearningRecord =
            requireNotNull(failureLearning.record)

        val strategyAdaptation =
            strategyAdaptationCoordinator.prepare(
                traceId = traceId,
                failureLearning = failureLearningRecord,
                adaptedStrategy = adaptedStrategy,
            )

        if (
            strategyAdaptation.status !=
            StrategyAdaptationPreparationStatus.PREPARED
        ) {
            return UnifiedRuntimeIntegrationResult(
                evidenceBasedLearning = evidenceBasedLearning,
                failureLearning = failureLearning,
                strategyAdaptation = strategyAdaptation,
            )
        }

        val strategyAdaptationRecord =
            requireNotNull(strategyAdaptation.record)

        val controlledAutonomy =
            controlledAutonomyCoordinator.prepare(
                traceId = traceId,
                strategyAdaptation = strategyAdaptationRecord,
                scope = scope,
            )

        return UnifiedRuntimeIntegrationResult(
            evidenceBasedLearning = evidenceBasedLearning,
            failureLearning = failureLearning,
            strategyAdaptation = strategyAdaptation,
            controlledAutonomy = controlledAutonomy,
        )
    }
}

/**
 * Immutable Stage 97 composition result.
 *
 * Nullable later results mean that their constitutional prerequisite was not
 * prepared, so the integration chain stopped before that stage.
 *
 * Presence of any preparation result does not grant authority or establish
 * that the represented semantic proposition is true.
 */
data class UnifiedRuntimeIntegrationResult(
    val evidenceBasedLearning: EvidenceBasedLearningPreparationResult,
    val failureLearning: FailureLearningPreparationResult? = null,
    val strategyAdaptation: StrategyAdaptationPreparationResult? = null,
    val controlledAutonomy: ControlledAutonomyPreparationResult? = null,
)
