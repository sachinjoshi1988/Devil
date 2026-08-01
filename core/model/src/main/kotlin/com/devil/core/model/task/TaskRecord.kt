package com.devil.core.model.task

import com.devil.core.model.decision.DecisionRecord

/**
 * Represents one constitutional task created from a selected decision.
 *
 * This record captures task identity, origin, lifecycle state, and a concise
 * task summary. It does not contain execution history, capability bindings,
 * observations, verification, retries, or final outcome.
 */
@ConsistentCopyVisibility
data class TaskRecord private constructor(
    val taskId: TaskId,
    val decision: DecisionRecord,
    val state: TaskState,
    val summary: String,
) {
    companion object {
        fun create(
            taskId: TaskId,
            decision: DecisionRecord,
            state: TaskState,
            summary: String,
        ): TaskRecord {
            val normalizedSummary = summary.trim()

            require(normalizedSummary.isNotEmpty()) {
                "Task summary must not be blank."
            }

            return TaskRecord(
                taskId = taskId,
                decision = decision,
                state = state,
                summary = normalizedSummary,
            )
        }
    }
}
