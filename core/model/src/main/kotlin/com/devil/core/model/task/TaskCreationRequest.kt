package com.devil.core.model.task

import com.devil.core.model.decision.DecisionRecord

/**
 * Represents one structured request for bounded constitutional task creation.
 *
 * The request preserves one completed DecisionRecord without reinterpreting its
 * constitutional decision state or creating a TaskRecord.
 *
 * This request does not create task identity, change task lifecycle state,
 * create plans, bind or authorize capabilities, execute actions, observe
 * results, or verify outcomes.
 */
@ConsistentCopyVisibility
data class TaskCreationRequest private constructor(
    val decision: DecisionRecord,
) {
    companion object {
        fun create(
            decision: DecisionRecord,
        ): TaskCreationRequest {
            return TaskCreationRequest(
                decision = decision,
            )
        }
    }
}
