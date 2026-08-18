package com.devil.core.runtime.task

import com.devil.core.model.authorization.AuthorizationEvaluationState
import com.devil.core.model.common.TraceId
import com.devil.core.model.decision.DecisionState
import com.devil.core.model.task.CompoundWorkTaskReentryRecord
import com.devil.core.model.task.CompoundWorkTaskReentryRequest
import com.devil.core.runtime.authorization.AuthorizationResult
import com.devil.core.runtime.authorization.AuthorizationStatus

/**
 * Stage 116 platform-independent coordinator for bounded compound-work Task
 * re-entry preparation.
 *
 * This coordinator consumes:
 *
 * - one exact Stage 115 CompoundWorkReconsiderationResult;
 * - one current fresh constitutional trace;
 * - and one explicit current AuthorizationResult.
 *
 * Preparation requires:
 *
 * - Stage 115 status PREPARED;
 * - one exact Stage 115 reconsideration record;
 * - Stage 115 fresh Decision trace equal to currentTraceId;
 * - Stage 115 fresh Decision state SELECTED;
 * - currentTraceId distinct from the originating Stage 77 trace;
 * - AuthorizationResult trace equal to currentTraceId;
 * - and AuthorizationResult status AUTHORIZED.
 *
 * Only after the runtime authorization result passes those gates does this
 * coordinator map that established result into the model-layer
 * AuthorizationEvaluationState.AUTHORIZED representation.
 *
 * The coordinator preserves the exact Stage 115 reconsideration record.
 * Therefore the exact Stage 114 continuation record and exact Stage 77 request
 * and eligible step remain preserved transitively.
 *
 * Stage 116 does not create another Brain and does not:
 *
 * - resolve Identity;
 * - establish Trust;
 * - grant Authorization;
 * - replace Understanding;
 * - create or replace the fresh Decision;
 * - create a TaskRecord;
 * - generate a TaskId;
 * - invoke TaskAuthority;
 * - create a Plan;
 * - select or activate a capability;
 * - establish capability readiness;
 * - establish Executive readiness;
 * - create an ExecutionRequest;
 * - execute;
 * - establish Observation;
 * - establish Verification;
 * - establish Outcome;
 * - mutate compound-work state;
 * - mark a step ACTIVE, COMPLETED, BLOCKED, or FAILED;
 * - mutate World Model state;
 * - perform Learning;
 * - operate Memory;
 * - automatically continue compound work;
 * - grant Controlled Autonomy;
 * - invoke UnifiedDevilRuntime;
 * - invoke a platform;
 * - or invoke a network.
 *
 * RECONSIDERATION != AUTHORIZATION.
 * AUTHORIZATION != TASK_CREATED.
 * RECONSIDERATION != TASK_CREATED.
 * TASK_REENTRY_PREPARED != TASK_CREATED.
 * TASK_REENTRY_PREPARED != TASK_AUTHORITY_RESULT.
 * AUTHORIZATION_STATE != AUTHORIZATION_AUTHORITY.
 * TASK_REENTRY != PLAN_CREATION.
 * TASK_REENTRY != EXECUTION.
 * TASK_REENTRY != AUTOMATIC_CONTINUATION.
 * TASK_REENTRY != CONTROLLED_AUTONOMY.
 */
class CompoundWorkTaskReentryCoordinator {

    fun prepare(
        currentTraceId: TraceId,
        reconsideration: CompoundWorkReconsiderationResult,
        authorization: AuthorizationResult,
    ): CompoundWorkTaskReentryResult {
        if (
            reconsideration.status !=
            CompoundWorkReconsiderationStatus.PREPARED
        ) {
            return deferred(
                traceId = currentTraceId,
            )
        }

        val reconsiderationRecord =
            reconsideration.record
                ?: return deferred(
                    traceId = currentTraceId,
                )

        if (reconsideration.traceId != currentTraceId) {
            return deferred(
                traceId = currentTraceId,
            )
        }

        val freshDecision =
            reconsiderationRecord
                .request
                .freshDecision

        val freshTraceId =
            freshDecision
                .understanding
                .context
                .traceId

        if (freshTraceId != currentTraceId) {
            return deferred(
                traceId = currentTraceId,
            )
        }

        if (freshDecision.state != DecisionState.SELECTED) {
            return deferred(
                traceId = currentTraceId,
            )
        }

        val originalTraceId =
            reconsiderationRecord
                .request
                .continuation
                .request
                .decision
                .understanding
                .context
                .traceId

        if (currentTraceId == originalTraceId) {
            return deferred(
                traceId = currentTraceId,
            )
        }

        if (authorization.traceId != currentTraceId) {
            return deferred(
                traceId = currentTraceId,
            )
        }

        if (authorization.status != AuthorizationStatus.AUTHORIZED) {
            return deferred(
                traceId = currentTraceId,
            )
        }

        val request =
            CompoundWorkTaskReentryRequest.create(
                reconsideration = reconsiderationRecord,
                authorizationState =
                    AuthorizationEvaluationState.AUTHORIZED,
            )

        val record =
            CompoundWorkTaskReentryRecord.create(
                request = request,
            )

        return CompoundWorkTaskReentryResult.create(
            traceId = currentTraceId,
            status = CompoundWorkTaskReentryStatus.PREPARED,
            record = record,
        )
    }

    private fun deferred(
        traceId: TraceId,
    ): CompoundWorkTaskReentryResult {
        return CompoundWorkTaskReentryResult.create(
            traceId = traceId,
            status = CompoundWorkTaskReentryStatus.DEFERRED,
        )
    }
}
