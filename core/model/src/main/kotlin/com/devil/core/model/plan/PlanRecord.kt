package com.devil.core.model.plan

import com.devil.core.model.task.TaskRecord

/**
 * Represents one constitutional plan created for an existing task.
 *
 * This record captures plan identity, originating task, lifecycle state, and a
 * concise strategy summary. It does not contain capability bindings, execution
 * history, observations, verification, or final outcome.
 */
@ConsistentCopyVisibility
data class PlanRecord private constructor(
    val planId: PlanId,
    val task: TaskRecord,
    val state: PlanState,
    val summary: String,
) {
    companion object {
        fun create(
            planId: PlanId,
            task: TaskRecord,
            state: PlanState,
            summary: String,
        ): PlanRecord {
            val normalizedSummary = summary.trim()

            require(normalizedSummary.isNotEmpty()) {
                "Plan summary must not be blank."
            }

            return PlanRecord(
                planId = planId,
                task = task,
                state = state,
                summary = normalizedSummary,
            )
        }
    }
}
