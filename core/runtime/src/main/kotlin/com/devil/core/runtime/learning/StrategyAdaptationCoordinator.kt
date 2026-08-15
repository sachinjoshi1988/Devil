package com.devil.core.runtime.learning

import com.devil.core.model.common.TraceId
import com.devil.core.model.learning.FailureLearningRecord
import com.devil.core.model.learning.StrategyAdaptationRecord

/**
 * Stage 94 bounded Strategy Adaptation Foundation coordinator.
 *
 * This coordinator accepts:
 *
 * - one constitutional TraceId;
 * - one existing Stage 93 FailureLearningRecord;
 * - and one explicitly supplied bounded adapted-strategy proposition.
 *
 * It prepares information that may later be considered by the existing
 * constitutional Planner. It does not itself perform planning.
 *
 * This coordinator does not:
 *
 * - create another Devil intelligence;
 * - create another Brain;
 * - create another Constitution;
 * - create another Planner;
 * - replace or invoke PlanAuthority;
 * - replace or invoke PlanningStrategyProvider;
 * - mutate an existing PlanRecord;
 * - create a PlanRecord;
 * - create or change Tasks;
 * - alter a constitutional Decision;
 * - change the established goal;
 * - create a RecoveryRequest;
 * - consume RecoveryAttemptBudget;
 * - perform RecoveryStrategy;
 * - retry an operation;
 * - establish Observation;
 * - establish Verification;
 * - establish Outcome;
 * - fabricate evidence;
 * - mutate World Model state;
 * - claim constitutional Learning occurred;
 * - create a LearningRequest;
 * - create a Memory Proposal;
 * - invoke Memory Authority;
 * - commit or persist Memory;
 * - grant authorization;
 * - create ExecutionRequests;
 * - execute actions;
 * - or grant Controlled Autonomy.
 *
 * FAILURE_LEARNING != STRATEGY_ADAPTATION.
 * STRATEGY_ADAPTATION_PREPARATION != PLAN.
 * STRATEGY_ADAPTATION_PREPARATION != PLANNER_DECISION.
 * STRATEGY_ADAPTATION != RECOVERY_RETRY.
 * STRATEGY_ADAPTATION != AUTHORIZATION.
 * STRATEGY_ADAPTATION != EXECUTION.
 * STRATEGY_ADAPTATION != CONTROLLED_AUTONOMY.
 */
class StrategyAdaptationCoordinator {

    fun prepare(
        traceId: TraceId,
        failureLearning: FailureLearningRecord,
        adaptedStrategy: String,
    ): StrategyAdaptationPreparationResult {
        if (
            failureLearning
                .evidenceBasedLearning
                .worldModelRepresentation
                .traceId != traceId ||
            adaptedStrategy.isBlank()
        ) {
            return deferred(
                traceId = traceId,
            )
        }

        val record =
            StrategyAdaptationRecord.create(
                failureLearning = failureLearning,
                adaptedStrategy = adaptedStrategy,
            )

        return StrategyAdaptationPreparationResult.create(
            traceId = traceId,
            status =
                StrategyAdaptationPreparationStatus.PREPARED,
            record = record,
        )
    }

    private fun deferred(
        traceId: TraceId,
    ): StrategyAdaptationPreparationResult {
        return StrategyAdaptationPreparationResult.create(
            traceId = traceId,
            status =
                StrategyAdaptationPreparationStatus.DEFERRED,
        )
    }
}
