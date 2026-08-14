package com.devil.core.runtime.goal

import com.devil.core.model.common.TraceId
import com.devil.core.model.goal.LongRunningGoalRecord

/**
 * Stable Stage 78 result of bounded long-running-goal preparation.
 *
 * PREPARED preserves one LongRunningGoalRecord.
 *
 * The result grants no persistence, scheduling, execution, Memory, or
 * authorization semantics.
 */
@ConsistentCopyVisibility
data class LongRunningGoalPreparationResult private constructor(
    val traceId: TraceId,
    val status: LongRunningGoalPreparationStatus,
    val goal: LongRunningGoalRecord?,
) {
    companion object {

        fun create(
            traceId: TraceId,
            status: LongRunningGoalPreparationStatus,
            goal: LongRunningGoalRecord? = null,
        ): LongRunningGoalPreparationResult {
            when (status) {
                LongRunningGoalPreparationStatus.PREPARED -> {
                    require(goal != null) {
                        "Prepared long-running goal results require one goal."
                    }
                }

                LongRunningGoalPreparationStatus.DEFERRED -> {
                    require(goal == null) {
                        "Deferred long-running goal results must not contain a goal."
                    }
                }
            }

            require(
                goal == null ||
                    goal.originatingDecision
                        .understanding
                        .context
                        .traceId == traceId,
            ) {
                "Long-running goal preparation result and goal must use the same originating trace identity."
            }

            return LongRunningGoalPreparationResult(
                traceId = traceId,
                status = status,
                goal = goal,
            )
        }
    }
}
