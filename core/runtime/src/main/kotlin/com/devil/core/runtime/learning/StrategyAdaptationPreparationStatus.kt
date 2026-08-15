package com.devil.core.runtime.learning

/**
 * Stage 94 bounded Strategy Adaptation preparation status.
 *
 * PREPARED means one structurally valid StrategyAdaptationRecord was created
 * from:
 *
 * - one existing Stage 93 FailureLearningRecord;
 * - and one explicitly supplied bounded adapted-strategy proposition.
 *
 * PREPARED does not mean:
 *
 * - the strategy was adopted;
 * - Planner strategy changed;
 * - an existing PlanRecord changed;
 * - a new PlanRecord exists;
 * - the established goal changed;
 * - a recovery retry was requested or performed;
 * - constitutional Learning occurred;
 * - World Model state changed;
 * - a Decision changed;
 * - authorization changed;
 * - execution occurred;
 * - Memory was proposed, approved, committed, or persisted;
 * - or Controlled Autonomy was granted.
 *
 * DEFERRED means no truthful bounded Strategy Adaptation record was produced.
 *
 * PREPARED != STRATEGY_ADOPTED.
 * PREPARED != PLAN_CHANGED.
 * PREPARED != RECOVERY_REQUESTED.
 * PREPARED != EXECUTED.
 * PREPARED != AUTONOMY_GRANTED.
 */
enum class StrategyAdaptationPreparationStatus {
    PREPARED,
    DEFERRED,
}
