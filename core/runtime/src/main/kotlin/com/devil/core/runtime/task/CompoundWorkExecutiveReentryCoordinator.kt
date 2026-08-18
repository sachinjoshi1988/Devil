package com.devil.core.runtime.task

import com.devil.core.model.common.TraceId
import com.devil.core.model.decision.DecisionState
import com.devil.core.model.plan.PlanState
import com.devil.core.model.task.CompoundWorkExecutiveReentryRecord
import com.devil.core.model.task.CompoundWorkExecutiveReentryRequest
import com.devil.core.runtime.capability.CapabilitySelectionResult
import com.devil.core.runtime.capability.CapabilitySelectionStatus

/**
 * Stage 119 platform-independent coordinator for bounded compound-work
 * Executive re-entry preparation.
 *
 * This coordinator consumes:
 *
 * - one exact Stage 118 CompoundWorkCapabilityReentryResult;
 * - one current fresh constitutional trace;
 * - and one exact current CapabilitySelectionResult.
 *
 * Preparation requires:
 *
 * - Stage 118 status PREPARED;
 * - one exact Stage 118 Capability re-entry record;
 * - Stage 118 trace equal to currentTraceId;
 * - fresh Decision trace equal to currentTraceId;
 * - currentTraceId distinct from the originating Stage 77 trace;
 * - CapabilitySelectionResult trace equal to currentTraceId;
 * - CapabilitySelectionResult status SELECTED;
 * - one CapabilityContract exists;
 * - preserved Plan remains CREATED;
 * - preserved Plan retains the exact Task;
 * - preserved Task retains the exact fresh selected Decision.
 *
 * Stage 119 does not:
 *
 * - create another Brain;
 * - resolve Identity;
 * - establish Trust;
 * - grant Authorization;
 * - replace Understanding;
 * - create or replace Decision;
 * - create or replace Task;
 * - create or replace Plan;
 * - select or replace a CapabilityContract;
 * - invoke CapabilitySelectionAuthority;
 * - establish Executive readiness;
 * - invoke ExecutiveReadinessAuthority;
 * - create an ExecutionRequest;
 * - invoke ExecutionAuthority;
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
 * CAPABILITY_SELECTED != EXECUTIVE_READY.
 * EXECUTIVE_REENTRY_PREPARED != EXECUTIVE_READY.
 * EXECUTIVE_REENTRY_PREPARED != EXECUTIVE_READINESS_RESULT.
 * CAPABILITY_REENTRY != EXECUTIVE_READINESS.
 * EXECUTIVE_REENTRY != EXECUTION_REQUEST.
 * EXECUTIVE_REENTRY != EXECUTION.
 * EXECUTIVE_REENTRY != AUTOMATIC_CONTINUATION.
 * EXECUTIVE_REENTRY != CONTROLLED_AUTONOMY.
 */
class CompoundWorkExecutiveReentryCoordinator {

    fun prepare(
        currentTraceId: TraceId,
        capabilityReentry: CompoundWorkCapabilityReentryResult,
        capability: CapabilitySelectionResult,
    ): CompoundWorkExecutiveReentryResult {
        if (
            capabilityReentry.status !=
            CompoundWorkCapabilityReentryStatus.PREPARED
        ) {
            return deferred(currentTraceId)
        }

        val capabilityReentryRecord =
            capabilityReentry.record
                ?: return deferred(currentTraceId)

        if (capabilityReentry.traceId != currentTraceId) {
            return deferred(currentTraceId)
        }

        val plan =
            capabilityReentryRecord
                .request
                .plan

        val task =
            plan
                .task

        val freshDecision =
            capabilityReentryRecord
                .request
                .planReentry
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

        if (plan.state != PlanState.CREATED) {
            return deferred(currentTraceId)
        }

        if (plan.task !== task) {
            return deferred(currentTraceId)
        }

        val originalTraceId =
            capabilityReentryRecord
                .request
                .planReentry
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

        if (capability.traceId != currentTraceId) {
            return deferred(currentTraceId)
        }

        if (capability.status != CapabilitySelectionStatus.SELECTED) {
            return deferred(currentTraceId)
        }

        val capabilityContract =
            capability.capability
                ?: return deferred(currentTraceId)

        val request =
            CompoundWorkExecutiveReentryRequest.create(
                capabilityReentry = capabilityReentryRecord,
                capability = capabilityContract,
            )

        val record =
            CompoundWorkExecutiveReentryRecord.create(
                request = request,
            )

        return CompoundWorkExecutiveReentryResult.create(
            traceId = currentTraceId,
            status = CompoundWorkExecutiveReentryStatus.PREPARED,
            record = record,
        )
    }

    private fun deferred(
        traceId: TraceId,
    ): CompoundWorkExecutiveReentryResult {
        return CompoundWorkExecutiveReentryResult.create(
            traceId = traceId,
            status = CompoundWorkExecutiveReentryStatus.DEFERRED,
        )
    }
}
