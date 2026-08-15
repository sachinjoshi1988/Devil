package com.devil.core.model.autonomy

import com.devil.core.model.learning.StrategyAdaptationRecord

/**
 * Immutable Stage 95 representation of one bounded Controlled Autonomy
 * preparation grounded in one existing Stage 94 StrategyAdaptationRecord.
 *
 * The exact StrategyAdaptationRecord remains attached so the complete
 * evidence-backed Stage 92 -> Stage 93 -> Stage 94 provenance remains
 * constitutionally explicit.
 *
 * scope describes only one explicitly supplied bounded area of work that may
 * later be reconsidered through Devil's existing constitutional chain.
 *
 * Creating this record does not grant autonomy.
 *
 * It does not:
 *
 * - create another Devil intelligence;
 * - create another Brain;
 * - create another Constitution;
 * - create another Executive;
 * - create another Planner;
 * - create another Security Authority;
 * - create another Authorization Authority;
 * - create another Memory Authority;
 * - make a Brain Decision;
 * - change owner intent;
 * - change an established goal;
 * - grant authorization;
 * - authenticate a subject;
 * - establish or extend a security session;
 * - enter Owner Mode;
 * - satisfy high-security confirmation;
 * - create or alter a Task;
 * - create or alter a Plan;
 * - adopt Strategy Adaptation as Planner strategy;
 * - select or activate a capability;
 * - establish capability availability;
 * - establish capability readiness;
 * - establish Executive readiness;
 * - create an ExecutionRequest;
 * - execute an action;
 * - create a scheduled trigger;
 * - create an external-event trigger;
 * - create Proactive Assistance;
 * - establish Observation;
 * - establish Verification;
 * - establish Outcome;
 * - mutate World Model state;
 * - perform constitutional Learning;
 * - create a Memory Proposal;
 * - invoke Memory Authority;
 * - commit or persist Memory;
 * - retry failed work;
 * - or continue work automatically.
 *
 * STRATEGY_ADAPTATION != CONTROLLED_AUTONOMY.
 * CONTROLLED_AUTONOMY_RECORD != AUTONOMY_GRANT.
 * CONTROLLED_AUTONOMY != AUTHORIZATION.
 * CONTROLLED_AUTONOMY != BRAIN_DECISION.
 * CONTROLLED_AUTONOMY != PLANNING.
 * CONTROLLED_AUTONOMY != EXECUTIVE_READINESS.
 * CONTROLLED_AUTONOMY != EXECUTION.
 * CONTROLLED_AUTONOMY != MEMORY_AUTHORITY.
 */
@ConsistentCopyVisibility
data class ControlledAutonomyRecord private constructor(
    val strategyAdaptation: StrategyAdaptationRecord,
    val scope: String,
) {
    companion object {

        fun create(
            strategyAdaptation: StrategyAdaptationRecord,
            scope: String,
        ): ControlledAutonomyRecord {
            val normalizedScope =
                scope.trim()

            require(normalizedScope.isNotEmpty()) {
                "Controlled Autonomy scope must not be blank."
            }

            return ControlledAutonomyRecord(
                strategyAdaptation = strategyAdaptation,
                scope = normalizedScope,
            )
        }
    }
}
