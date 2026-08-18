package com.devil.core.runtime.task

import com.devil.core.model.common.TraceId
import com.devil.core.model.decision.DecisionState
import com.devil.core.model.plan.PlanState
import com.devil.core.model.task.CompoundWorkCapabilityReentryRecord
import com.devil.core.model.task.CompoundWorkCapabilityReentryRequest
import com.devil.core.runtime.plan.PlanAuthorityResult
import com.devil.core.runtime.plan.PlanAuthorityStatus

/**
 * Stage 118 platform-independent coordinator for bounded compound-work
 * Capability Selection re-entry preparation.
 *
 * This coordinator consumes:
 *
 * - one exact Stage 117 CompoundWorkPlanReentryResult;
 * - one current fresh constitutional trace;
 * - and one exact current PlanAuthorityResult.
 *
 * Preparation requires:
 *
 * - Stage 117 status PREPARED;
 * - one exact Stage 117 Plan re-entry record;
 * - Stage 117 fresh trace equal to currentTraceId;
 * - currentTraceId distinct from the originating Stage 77 trace;
 * - PlanAuthorityResult trace equal to currentTraceId;
 * - PlanAuthorityResult status CREATED;
 * - one PlanRecord exists;
 * - that PlanRecord remains CREATED;
 * - the preserved Stage 115 fresh Decision remains SELECTED;
 * - the Stage 117 Task preserves that exact fresh Decision;
 * - and the created Plan preserves the exact Stage 117 Task.
 *
 * The coordinator preserves the exact Stage 117 record and exact created Plan.
 * Stage 116, Stage 115, Stage 114, and Stage 77 provenance therefore remain
 * preserved transitively.
 *
 * Stage 118 does not:
 *
 * - create another Brain;
 * - resolve Identity;
 * - establish Trust;
 * - grant Authorization;
 * - replace Understanding;
 * - create or replace the fresh Decision;
 * - create or replace a TaskRecord;
 * - create or replace a PlanRecord;
 * - generate a PlanId;
 * - invoke PlanAuthority;
 * - select a CapabilityContract;
 * - invoke CapabilitySelectionAuthority;
 * - establish capability availability;
 * - establish capability health;
 * - establish operating-system permission;
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
 * PLAN_REENTRY_PREPARED != PLAN_CREATED.
 * PLAN_CREATED != CAPABILITY_SELECTED.
 * CAPABILITY_REENTRY_PREPARED != CAPABILITY_SELECTED.
 * CAPABILITY_REENTRY_PREPARED != CAPABILITY_SELECTION_RESULT.
 * PLAN_REENTRY != CAPABILITY_SELECTION.
 * CAPABILITY_REENTRY != EXECUTIVE_READINESS.
 * CAPABILITY_REENTRY != EXECUTION.
 * CAPABILITY_REENTRY != AUTOMATIC_CONTINUATION.
 * CAPABILITY_REENTRY != CONTROLLED_AUTONOMY.
 */
class CompoundWorkCapabilityReentryCoordinator {

    fun prepare(
        currentTraceId: TraceId,
        planReentry: CompoundWorkPlanReentryResult,
        plan: PlanAuthorityResult,
    ): CompoundWorkCapabilityReentryResult {
        if (planReentry.status != CompoundWorkPlanReentryStatus.PREPARED) {
            return deferred(currentTraceId)
        }

        val planReentryRecord =
            planReentry.record
                ?: return deferred(currentTraceId)

        if (planReentry.traceId != currentTraceId) {
            return deferred(currentTraceId)
        }

        val task =
            planReentryRecord
                .request
                .task

        val freshDecision =
            planReentryRecord
                .request
                .taskReentry
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

        if (task.decision !== freshDecision) {
            return deferred(currentTraceId)
        }

        val originalTraceId =
            planReentryRecord
                .request
                .taskReentry
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

        if (plan.traceId != currentTraceId) {
            return deferred(currentTraceId)
        }

        if (plan.status != PlanAuthorityStatus.CREATED) {
            return deferred(currentTraceId)
        }

        val planRecord =
            plan.plan
                ?: return deferred(currentTraceId)

        if (planRecord.state != PlanState.CREATED) {
            return deferred(currentTraceId)
        }

        if (planRecord.task !== task) {
            return deferred(currentTraceId)
        }

        val request =
            CompoundWorkCapabilityReentryRequest.create(
                planReentry = planReentryRecord,
                plan = planRecord,
            )

        val record =
            CompoundWorkCapabilityReentryRecord.create(
                request = request,
            )

        return CompoundWorkCapabilityReentryResult.create(
            traceId = currentTraceId,
            status = CompoundWorkCapabilityReentryStatus.PREPARED,
            record = record,
        )
    }

    private fun deferred(
        traceId: TraceId,
    ): CompoundWorkCapabilityReentryResult {
        return CompoundWorkCapabilityReentryResult.create(
            traceId = traceId,
            status = CompoundWorkCapabilityReentryStatus.DEFERRED,
        )
    }
}
