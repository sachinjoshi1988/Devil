package com.devil.core.model.task

import com.devil.core.model.decision.DecisionState
import com.devil.core.model.plan.PlanRecord
import com.devil.core.model.plan.PlanState

/**
 * Immutable Stage 118 request for bounded compound-work Capability Selection
 * re-entry.
 *
 * The request preserves:
 *
 * - one exact Stage 117 CompoundWorkPlanReentryRecord;
 * - one exact PlanRecord created by the existing Plan Authority;
 * - the exact Stage 117 TaskRecord transitively;
 * - the exact Stage 115 fresh selected Decision transitively;
 * - the exact Stage 116 Task re-entry record transitively;
 * - the exact Stage 115 reconsideration record transitively;
 * - the exact Stage 114 continuation record transitively;
 * - the exact Stage 77 CompoundWorkRequest transitively;
 * - and the exact eligible Stage 77 CompoundWorkStep transitively.
 *
 * The supplied PlanRecord must preserve the exact TaskRecord carried by
 * Stage 117.
 *
 * Creating this request does not:
 *
 * - create or replace a PlanRecord;
 * - generate a PlanId;
 * - invent planning strategy;
 * - invoke PlanAuthority;
 * - create or replace a TaskRecord;
 * - create or replace a Decision;
 * - select a CapabilityContract;
 * - invoke CapabilitySelectionAuthority;
 * - establish capability availability;
 * - establish capability health;
 * - establish operating-system permission;
 * - resolve Identity;
 * - establish Trust;
 * - grant Authorization;
 * - replace Understanding;
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
 * PLAN_REENTRY_PREPARED != PLAN_CREATED.
 * PLAN_CREATED != CAPABILITY_SELECTED.
 * CAPABILITY_REENTRY_REQUEST != CAPABILITY_SELECTED.
 * CAPABILITY_REENTRY_REQUEST != CAPABILITY_SELECTION_RESULT.
 * PLAN_REENTRY != CAPABILITY_SELECTION.
 * CAPABILITY_REENTRY != EXECUTIVE_READINESS.
 * CAPABILITY_REENTRY != EXECUTION.
 * CAPABILITY_REENTRY != AUTOMATIC_CONTINUATION.
 * CAPABILITY_REENTRY != CONTROLLED_AUTONOMY.
 */
@ConsistentCopyVisibility
data class CompoundWorkCapabilityReentryRequest private constructor(
    val planReentry: CompoundWorkPlanReentryRecord,
    val plan: PlanRecord,
) {
    companion object {

        fun create(
            planReentry: CompoundWorkPlanReentryRecord,
            plan: PlanRecord,
        ): CompoundWorkCapabilityReentryRequest {
            val task =
                planReentry
                    .request
                    .task

            val freshDecision =
                planReentry
                    .request
                    .taskReentry
                    .request
                    .reconsideration
                    .request
                    .freshDecision

            require(freshDecision.state == DecisionState.SELECTED) {
                "Compound-work Capability re-entry requires the preserved fresh Decision to remain selected."
            }

            require(task.decision === freshDecision) {
                "Compound-work Capability re-entry requires the Stage 117 Task to preserve the exact Stage 115 fresh Decision."
            }

            require(plan.state == PlanState.CREATED) {
                "Compound-work Capability re-entry requires one Plan created by the existing Plan Authority."
            }

            require(plan.task === task) {
                "Compound-work Capability re-entry Plan must preserve the exact Stage 117 Task."
            }

            return CompoundWorkCapabilityReentryRequest(
                planReentry = planReentry,
                plan = plan,
            )
        }
    }
}
