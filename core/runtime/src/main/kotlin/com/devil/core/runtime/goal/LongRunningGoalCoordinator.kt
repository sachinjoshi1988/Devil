package com.devil.core.runtime.goal

import com.devil.core.model.common.TraceId
import com.devil.core.model.decision.DecisionRecord
import com.devil.core.model.decision.DecisionState
import com.devil.core.model.goal.LongRunningGoalId
import com.devil.core.model.goal.LongRunningGoalRecord
import com.devil.core.model.goal.LongRunningGoalState
import com.devil.core.model.task.CompoundWorkRequest

/**
 * Stage 78 bounded long-running-goal preparation coordinator.
 *
 * This coordinator may preserve an already-selected constitutional goal across
 * later reasoning cycles.
 *
 * It does not infer goals from raw user prose.
 *
 * It does not:
 *
 * - create another Brain;
 * - select a Decision;
 * - create Tasks or Plans;
 * - grant authorization;
 * - preserve Security sessions;
 * - bind capabilities;
 * - execute actions;
 * - schedule work;
 * - respond to future events;
 * - mutate World Model state;
 * - perform Learning;
 * - commit Memory;
 * - persist the goal;
 * - or automatically resume work.
 *
 * Any future material action associated with this goal must enter a fresh
 * constitutionally governed reasoning/execution path.
 */
class LongRunningGoalCoordinator {

    fun prepare(
        traceId: TraceId,
        decision: DecisionRecord,
        goalId: LongRunningGoalId,
        description: String,
        compoundWork: CompoundWorkRequest? = null,
    ): LongRunningGoalPreparationResult {
        require(
            decision.understanding.context.traceId ==
                traceId,
        ) {
            "Long-running goal preparation and Decision must use the same trace identity."
        }

        if (
            decision.state !=
            DecisionState.SELECTED
        ) {
            return LongRunningGoalPreparationResult.create(
                traceId = traceId,
                status =
                    LongRunningGoalPreparationStatus.DEFERRED,
            )
        }

        if (description.isBlank()) {
            return LongRunningGoalPreparationResult.create(
                traceId = traceId,
                status =
                    LongRunningGoalPreparationStatus.DEFERRED,
            )
        }

        val goal =
            LongRunningGoalRecord.create(
                goalId = goalId,
                originatingDecision = decision,
                state = LongRunningGoalState.ACTIVE,
                description = description,
                compoundWork = compoundWork,
            )

        return LongRunningGoalPreparationResult.create(
            traceId = traceId,
            status =
                LongRunningGoalPreparationStatus.PREPARED,
            goal = goal,
        )
    }
}
