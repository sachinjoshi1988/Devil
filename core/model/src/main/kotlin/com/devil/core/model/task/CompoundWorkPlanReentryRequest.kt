package com.devil.core.model.task

import com.devil.core.model.decision.DecisionState

/**
 * Immutable Stage 117 request for bounded compound-work Plan re-entry.
 *
 * The request preserves:
 *
 * - one exact Stage 116 CompoundWorkTaskReentryRecord;
 * - one exact TaskRecord created by the existing Task Authority;
 * - the exact fresh selected Decision transitively;
 * - the exact Stage 115 reconsideration record transitively;
 * - the exact Stage 114 continuation record transitively;
 * - the exact Stage 77 CompoundWorkRequest transitively;
 * - and the exact eligible Stage 77 CompoundWorkStep transitively.
 *
 * The supplied TaskRecord must preserve the exact fresh Decision carried by
 * Stage 116.
 *
 * Creating this request does not:
 *
 * - create or replace a TaskRecord;
 * - generate a TaskId;
 * - invoke TaskAuthority;
 * - create a PlanRecord;
 * - generate a PlanId;
 * - invent a planning strategy;
 * - invoke PlanAuthority;
 * - resolve Identity;
 * - establish Trust;
 * - grant Authorization;
 * - replace Understanding;
 * - replace the fresh Decision;
 * - select or activate a capability;
 * - establish Executive readiness;
 * - create an ExecutionRequest;
 * - execute;
 * - establish Observation;
 * - establish Verification;
 * - establish Outcome;
 * - mutate compound-work state;
 * - mutate World Model state;
 * - perform Learning;
 * - operate Memory;
 * - automatically continue compound work;
 * - grant Controlled Autonomy;
 * - invoke a platform;
 * - or invoke a network.
 *
 * TASK_REENTRY_PREPARED != TASK_CREATED.
 * TASK_CREATED != PLAN_CREATED.
 * PLAN_REENTRY_REQUEST != PLAN_CREATED.
 * PLAN_REENTRY_REQUEST != PLAN_AUTHORITY_RESULT.
 * TASK_REENTRY != PLAN_CREATION.
 * PLAN_REENTRY != CAPABILITY_SELECTION.
 * PLAN_REENTRY != EXECUTION.
 * PLAN_REENTRY != AUTOMATIC_CONTINUATION.
 * PLAN_REENTRY != CONTROLLED_AUTONOMY.
 */
@ConsistentCopyVisibility
data class CompoundWorkPlanReentryRequest private constructor(
    val taskReentry: CompoundWorkTaskReentryRecord,
    val task: TaskRecord,
) {
    companion object {

        fun create(
            taskReentry: CompoundWorkTaskReentryRecord,
            task: TaskRecord,
        ): CompoundWorkPlanReentryRequest {
            val freshDecision =
                taskReentry
                    .request
                    .reconsideration
                    .request
                    .freshDecision

            require(freshDecision.state == DecisionState.SELECTED) {
                "Compound-work Plan re-entry requires the preserved fresh Decision to remain selected."
            }

            require(task.state == TaskState.CREATED) {
                "Compound-work Plan re-entry requires one Task created by the existing Task Authority."
            }

            require(task.decision === freshDecision) {
                "Compound-work Plan re-entry Task must preserve the exact Stage 115 fresh Decision."
            }

            return CompoundWorkPlanReentryRequest(
                taskReentry = taskReentry,
                task = task,
            )
        }
    }
}
