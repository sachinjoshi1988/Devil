package com.devil.core.model.plan

import com.devil.core.model.task.TaskRecord

/**
 * Represents one structured request for bounded constitutional plan creation.
 *
 * The request preserves one existing TaskRecord without reinterpreting its
 * constitutional task state or creating a PlanRecord.
 *
 * This request does not create planning strategy, generate plan identity,
 * change task or plan lifecycle state, bind or authorize capabilities, execute
 * actions, observe results, verify outcomes, or report final outcomes.
 */
@ConsistentCopyVisibility
data class PlanCreationRequest private constructor(
    val task: TaskRecord,
) {
    companion object {
        fun create(
            task: TaskRecord,
        ): PlanCreationRequest {
            return PlanCreationRequest(
                task = task,
            )
        }
    }
}
