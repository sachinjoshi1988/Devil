package com.devil.core.runtime.task

import com.devil.core.model.common.TraceId
import com.devil.core.model.decision.DecisionState
import com.devil.core.model.task.CompoundWorkPlanReentryRecord
import com.devil.core.model.task.CompoundWorkPlanReentryRequest
import com.devil.core.model.task.TaskState

/**
 * Stage 117 platform-independent coordinator for bounded compound-work Plan
 * re-entry preparation.
 *
 * This coordinator consumes:
 *
 * - one exact Stage 116 CompoundWorkTaskReentryResult;
 * - one current fresh constitutional trace;
 * - and one exact current TaskAuthorityResult.
 *
 * Preparation requires:
 *
 * - Stage 116 status PREPARED;
 * - one exact Stage 116 Task re-entry record;
 * - Stage 116 fresh trace equal to currentTraceId;
 * - currentTraceId distinct from the originating Stage 77 trace;
 * - TaskAuthorityResult trace equal to currentTraceId;
 * - TaskAuthorityResult status CREATED;
 * - one TaskRecord exists;
 * - that TaskRecord remains CREATED;
 * - the preserved Stage 115 fresh Decision remains SELECTED;
 * - and the created Task preserves that exact fresh Decision.
 *
 * The coordinator preserves the exact Stage 116 record and exact created Task.
 * Stage 115, Stage 114, and Stage 77 provenance therefore remain preserved
 * transitively.
 *
 * Stage 117 does not:
 *
 * - create another Brain;
 * - resolve Identity;
 * - establish Trust;
 * - grant Authorization;
 * - replace Understanding;
 * - create or replace the fresh Decision;
 * - create or replace a TaskRecord;
 * - generate a TaskId;
 * - invoke TaskAuthority;
 * - create a PlanRecord;
 * - generate a PlanId;
 * - invent planning strategy;
 * - invoke PlanAuthority;
 * - select or activate a capability;
 * - establish capability readiness;
 * - establish Executive readiness;
 * - create an ExecutionRequest;
 * - execute;
 * - establish Observation;
 * - establish Verification;
 * - establish Outcome;
 * - mutate compound-work state;
 * - change any CompoundWorkStep state;
 * - mutate World Model state;
 * - perform Learning;
 * - operate Memory;
 * - automatically continue compound work;
 * - grant Controlled Autonomy;
 * - invoke UnifiedDevilRuntime;
 * - invoke a platform;
 * - or invoke a network.
 *
 * TASK_REENTRY_PREPARED != TASK_CREATED.
 * TASK_CREATED != PLAN_CREATED.
 * PLAN_REENTRY_PREPARED != PLAN_CREATED.
 * PLAN_REENTRY_PREPARED != PLAN_AUTHORITY_RESULT.
 * TASK_REENTRY != PLAN_CREATION.
 * PLAN_REENTRY != CAPABILITY_SELECTION.
 * PLAN_REENTRY != EXECUTION.
 * PLAN_REENTRY != AUTOMATIC_CONTINUATION.
 * PLAN_REENTRY != CONTROLLED_AUTONOMY.
 */
class CompoundWorkPlanReentryCoordinator {

    fun prepare(
        currentTraceId: TraceId,
        taskReentry: CompoundWorkTaskReentryResult,
        task: TaskAuthorityResult,
    ): CompoundWorkPlanReentryResult {
        if (taskReentry.status != CompoundWorkTaskReentryStatus.PREPARED) {
            return deferred(currentTraceId)
        }

        val taskReentryRecord =
            taskReentry.record
                ?: return deferred(currentTraceId)

        if (taskReentry.traceId != currentTraceId) {
            return deferred(currentTraceId)
        }

        val freshDecision =
            taskReentryRecord
                .request
                .reconsideration
                .request
                .freshDecision

        val freshTraceId =
            freshDecision
                .understanding
                .context
                .traceId

        if (freshTraceId != currentTraceId) {
            return deferred(currentTraceId)
        }

        if (freshDecision.state != DecisionState.SELECTED) {
            return deferred(currentTraceId)
        }

        val originalTraceId =
            taskReentryRecord
                .request
                .reconsideration
                .request
                .continuation
                .request
                .decision
                .understanding
                .context
                .traceId

        if (currentTraceId == originalTraceId) {
            return deferred(currentTraceId)
        }

        if (task.traceId != currentTraceId) {
            return deferred(currentTraceId)
        }

        if (task.status != TaskAuthorityStatus.CREATED) {
            return deferred(currentTraceId)
        }

        val taskRecord =
            task.task
                ?: return deferred(currentTraceId)

        if (taskRecord.state != TaskState.CREATED) {
            return deferred(currentTraceId)
        }

        if (taskRecord.decision !== freshDecision) {
            return deferred(currentTraceId)
        }

        val request =
            CompoundWorkPlanReentryRequest.create(
                taskReentry = taskReentryRecord,
                task = taskRecord,
            )

        val record =
            CompoundWorkPlanReentryRecord.create(
                request = request,
            )

        return CompoundWorkPlanReentryResult.create(
            traceId = currentTraceId,
            status = CompoundWorkPlanReentryStatus.PREPARED,
            record = record,
        )
    }

    private fun deferred(
        traceId: TraceId,
    ): CompoundWorkPlanReentryResult {
        return CompoundWorkPlanReentryResult.create(
            traceId = traceId,
            status = CompoundWorkPlanReentryStatus.DEFERRED,
        )
    }
}
