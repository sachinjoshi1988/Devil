package com.devil.core.model.learning

/**
 * Immutable Stage 94 representation of one bounded Strategy Adaptation
 * preparation grounded in one existing Stage 93 FailureLearningRecord.
 *
 * The exact FailureLearningRecord remains attached so the evidence-backed
 * Stage 92 -> Stage 93 provenance remains explicit.
 *
 * adaptedStrategy preserves one explicitly supplied bounded proposition
 * describing how a future constitutionally governed planning strategy may
 * be reconsidered.
 *
 * Creating this record does not adopt, apply, or execute that strategy.
 *
 * It does not:
 *
 * - create another Devil intelligence;
 * - create another Brain;
 * - create another Constitution;
 * - create another Planner;
 * - replace PlanAuthority;
 * - replace PlanningStrategyProvider;
 * - mutate an existing PlanRecord;
 * - create a PlanRecord;
 * - create a Task;
 * - create or alter a constitutional Decision;
 * - change the established goal;
 * - create a RecoveryRequest;
 * - consume a RecoveryAttemptBudget;
 * - perform a recovery attempt;
 * - retry an operation;
 * - establish new Observation;
 * - establish Verification;
 * - establish Outcome;
 * - fabricate evidence;
 * - mutate World Model state;
 * - perform constitutional Learning by itself;
 * - create a Memory Proposal;
 * - invoke Memory Authority;
 * - commit or persist Memory;
 * - authorize a capability;
 * - create an ExecutionRequest;
 * - execute an action;
 * - or grant Controlled Autonomy.
 *
 * FAILURE_LEARNING != STRATEGY_ADAPTATION.
 * STRATEGY_ADAPTATION_RECORD != PLAN.
 * STRATEGY_ADAPTATION_RECORD != PLANNER_DECISION.
 * STRATEGY_ADAPTATION != RECOVERY_RETRY.
 * STRATEGY_ADAPTATION != AUTHORIZATION.
 * STRATEGY_ADAPTATION != EXECUTION.
 * STRATEGY_ADAPTATION != CONTROLLED_AUTONOMY.
 */
@ConsistentCopyVisibility
data class StrategyAdaptationRecord private constructor(
    val failureLearning: FailureLearningRecord,
    val adaptedStrategy: String,
) {
    companion object {

        fun create(
            failureLearning: FailureLearningRecord,
            adaptedStrategy: String,
        ): StrategyAdaptationRecord {
            val normalizedStrategy =
                adaptedStrategy.trim()

            require(normalizedStrategy.isNotEmpty()) {
                "Strategy Adaptation proposition must not be blank."
            }

            return StrategyAdaptationRecord(
                failureLearning = failureLearning,
                adaptedStrategy = normalizedStrategy,
            )
        }
    }
}
