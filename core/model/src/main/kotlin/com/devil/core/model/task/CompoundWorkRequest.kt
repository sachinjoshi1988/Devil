package com.devil.core.model.task

import com.devil.core.model.decision.DecisionRecord
import com.devil.core.model.decision.DecisionState

/**
 * Represents one bounded request to preserve ordered compound work beneath one
 * existing constitutional Decision.
 *
 * Stage 77 deliberately does not convert these steps directly into TaskRecord,
 * PlanRecord, ExecutionRequest, or platform actions.
 *
 * The originating Decision remains the single constitutional Decision that
 * established the goal for this reasoning cycle.
 *
 * Every future material action represented by these steps must still approach
 * the existing governed chain:
 *
 * Decision
 * -> Task
 * -> Plan
 * -> Capability
 * -> Execution
 * -> Observation
 * -> Verification
 * -> Outcome.
 *
 * Compound work therefore does not create:
 *
 * - another Brain;
 * - child Brain decisions;
 * - authorization;
 * - Android permission;
 * - capability readiness;
 * - execution;
 * - success;
 * - World Model mutation;
 * - Learning;
 * - or Memory.
 *
 * The caller must supply an already-bounded ordered decomposition. This type
 * does not parse user prose, infer extra goals, or alter owner intent.
 */
@ConsistentCopyVisibility
data class CompoundWorkRequest private constructor(
    val decision: DecisionRecord,
    val steps: List<CompoundWorkStep>,
) {
    companion object {

        fun create(
            decision: DecisionRecord,
            steps: List<CompoundWorkStep>,
        ): CompoundWorkRequest {
            require(decision.state == DecisionState.SELECTED) {
                "Compound work requires one selected constitutional Decision."
            }

            require(steps.size >= MINIMUM_COMPOUND_STEP_COUNT) {
                "Compound work requires at least two ordered steps."
            }

            steps.forEachIndexed { index, step ->
                val expectedPosition =
                    index + 1

                require(step.position == expectedPosition) {
                    "Compound work steps must use contiguous ordered positions beginning at one."
                }
            }

            return CompoundWorkRequest(
                decision = decision,
                steps = steps.toList(),
            )
        }

        private const val MINIMUM_COMPOUND_STEP_COUNT: Int = 2
    }
}
