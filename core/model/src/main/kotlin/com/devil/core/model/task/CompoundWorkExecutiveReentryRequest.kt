package com.devil.core.model.task

import com.devil.core.model.capability.CapabilityContract
import com.devil.core.model.decision.DecisionState
import com.devil.core.model.plan.PlanState

/**
 * Immutable Stage 119 request for bounded compound-work Executive re-entry.
 *
 * The request preserves:
 *
 * - one exact Stage 118 CompoundWorkCapabilityReentryRecord;
 * - one exact CapabilityContract selected by the existing Capability Selection Authority;
 * - the exact Stage 118 PlanRecord transitively;
 * - the exact Stage 117 TaskRecord transitively;
 * - the exact Stage 115 fresh selected Decision transitively;
 * - the exact Stage 117 Plan re-entry record transitively;
 * - the exact Stage 116 Task re-entry record transitively;
 * - the exact Stage 115 reconsideration record transitively;
 * - the exact Stage 114 continuation record transitively;
 * - the exact Stage 77 CompoundWorkRequest transitively;
 * - and the exact eligible Stage 77 CompoundWorkStep transitively.
 *
 * Creating this request does not:
 *
 * - select or replace a CapabilityContract;
 * - invoke CapabilitySelectionAuthority;
 * - establish Executive readiness;
 * - invoke ExecutiveReadinessAuthority;
 * - create an ExecutionRequest;
 * - invoke ExecutionAuthority;
 * - execute;
 * - create or replace a PlanRecord;
 * - create or replace a TaskRecord;
 * - create or replace a Decision;
 * - resolve Identity;
 * - establish Trust;
 * - grant Authorization;
 * - replace Understanding;
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
 * CAPABILITY_REENTRY_PREPARED != CAPABILITY_SELECTED.
 * CAPABILITY_SELECTED != EXECUTIVE_READY.
 * EXECUTIVE_REENTRY_REQUEST != EXECUTIVE_READY.
 * EXECUTIVE_REENTRY_REQUEST != EXECUTIVE_READINESS_RESULT.
 * CAPABILITY_REENTRY != EXECUTIVE_READINESS.
 * EXECUTIVE_REENTRY != EXECUTION_REQUEST.
 * EXECUTIVE_REENTRY != EXECUTION.
 * EXECUTIVE_REENTRY != AUTOMATIC_CONTINUATION.
 * EXECUTIVE_REENTRY != CONTROLLED_AUTONOMY.
 */
@ConsistentCopyVisibility
data class CompoundWorkExecutiveReentryRequest private constructor(
    val capabilityReentry: CompoundWorkCapabilityReentryRecord,
    val capability: CapabilityContract,
) {
    companion object {

        fun create(
            capabilityReentry: CompoundWorkCapabilityReentryRecord,
            capability: CapabilityContract,
        ): CompoundWorkExecutiveReentryRequest {
            val plan =
                capabilityReentry
                    .request
                    .plan

            val task =
                plan
                    .task

            val freshDecision =
                capabilityReentry
                    .request
                    .planReentry
                    .request
                    .taskReentry
                    .request
                    .reconsideration
                    .request
                    .freshDecision

            require(freshDecision.state == DecisionState.SELECTED) {
                "Compound-work Executive re-entry requires the preserved fresh Decision to remain selected."
            }

            require(task.decision === freshDecision) {
                "Compound-work Executive re-entry requires the preserved Task to retain the exact fresh Decision."
            }

            require(plan.state == PlanState.CREATED) {
                "Compound-work Executive re-entry requires the preserved Plan to remain CREATED."
            }

            require(plan.task === task) {
                "Compound-work Executive re-entry requires the preserved Plan to retain the exact Task."
            }

            return CompoundWorkExecutiveReentryRequest(
                capabilityReentry = capabilityReentry,
                capability = capability,
            )
        }
    }
}
