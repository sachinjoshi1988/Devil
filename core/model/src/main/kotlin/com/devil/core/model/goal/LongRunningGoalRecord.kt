package com.devil.core.model.goal

import com.devil.core.model.decision.DecisionRecord
import com.devil.core.model.decision.DecisionState
import com.devil.core.model.task.CompoundWorkRequest

/**
 * Immutable representation of one bounded long-running goal.
 *
 * A long-running goal preserves stable user-directed objective continuity across
 * separate constitutional reasoning cycles.
 *
 * The originating DecisionRecord remains the constitutional decision that
 * established this goal.
 *
 * An optional CompoundWorkRequest may preserve already-governed ordered work
 * beneath that same decision.
 *
 * This record deliberately contains no:
 *
 * - AuthorizationResult;
 * - security session;
 * - TaskRecord;
 * - PlanRecord;
 * - capability binding;
 * - ExecutionRequest;
 * - execution attempt;
 * - Observation;
 * - Verification;
 * - Outcome;
 * - Memory commitment;
 * - persistence approval;
 * - or automatic continuation authority.
 *
 * Preserving a goal therefore never permits a future material action to bypass:
 *
 * Constitution
 * -> Identity
 * -> Trust
 * -> Authorization
 * -> Understanding
 * -> Decision
 * -> Task
 * -> Plan
 * -> Capability
 * -> Execution
 * -> Observation
 * -> Verification
 * -> Outcome.
 *
 * LONG_RUNNING_GOAL != PERMANENT_AUTHORIZATION.
 * GOAL_CONTINUITY != EXECUTION_CONTINUITY.
 */
@ConsistentCopyVisibility
data class LongRunningGoalRecord private constructor(
    val goalId: LongRunningGoalId,
    val originatingDecision: DecisionRecord,
    val state: LongRunningGoalState,
    val description: String,
    val compoundWork: CompoundWorkRequest?,
) {
    companion object {

        fun create(
            goalId: LongRunningGoalId,
            originatingDecision: DecisionRecord,
            state: LongRunningGoalState,
            description: String,
            compoundWork: CompoundWorkRequest? = null,
        ): LongRunningGoalRecord {
            val normalizedDescription =
                description.trim()

            require(
                originatingDecision.state ==
                    DecisionState.SELECTED,
            ) {
                "Long-running goals require one selected constitutional Decision."
            }

            require(normalizedDescription.isNotEmpty()) {
                "Long-running goal description must not be blank."
            }

            require(
                compoundWork == null ||
                    compoundWork.decision ===
                    originatingDecision,
            ) {
                "Long-running goal compound work must preserve the originating constitutional Decision."
            }

            return LongRunningGoalRecord(
                goalId = goalId,
                originatingDecision = originatingDecision,
                state = state,
                description = normalizedDescription,
                compoundWork = compoundWork,
            )
        }
    }
}
