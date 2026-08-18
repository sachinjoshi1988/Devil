package com.devil.core.runtime.task

import com.devil.core.model.common.TraceId
import com.devil.core.model.decision.DecisionRecord
import com.devil.core.model.decision.DecisionState
import com.devil.core.model.task.CompoundWorkReconsiderationRecord
import com.devil.core.model.task.CompoundWorkReconsiderationRequest

/**
 * Stage 115 platform-independent coordinator for bounded compound-work
 * reconsideration preparation.
 *
 * This coordinator consumes:
 *
 * - one current trace belonging to a fresh constitutional reasoning cycle;
 * - one exact Stage 114 CompoundWorkContinuationResult;
 * - one fresh Decision produced by the current reasoning cycle;
 * - and one explicitly supplied reconsideration relationship determination.
 *
 * Stage 114 eligibility remains only eligibility.
 *
 * Stage 115 does not infer exact-step relationship from Decision summary text.
 * The relationship must be explicitly supplied by the caller.
 *
 * Preparation requires:
 *
 * - Stage 114 ELIGIBLE_FOR_RECONSIDERATION;
 * - one preserved Stage 114 continuation record;
 * - fresh Decision trace equal to currentTraceId;
 * - fresh Decision state SELECTED;
 * - fresh trace distinct from the originating compound-work trace;
 * - and reconsiderationEstablished == true.
 *
 * It does not:
 *
 * - create another Brain;
 * - select or replace the fresh Decision;
 * - grant authorization;
 * - create a Task;
 * - create a Plan;
 * - select or activate a capability;
 * - establish capability availability or readiness;
 * - establish Executive readiness;
 * - create an ExecutionRequest;
 * - execute;
 * - establish Observation;
 * - establish Verification;
 * - establish Outcome;
 * - mutate Stage 113 compound-work state;
 * - mark a step ACTIVE or COMPLETED;
 * - authorize the eligible step;
 * - automatically advance the eligible step;
 * - continue work automatically;
 * - grant Controlled Autonomy;
 * - mutate World Model state;
 * - perform Learning;
 * - operate Memory;
 * - invoke UnifiedDevilRuntime;
 * - invoke a platform;
 * - or invoke a network.
 *
 * ELIGIBILITY != RECONSIDERATION.
 * RECONSIDERATION != AUTHORIZATION.
 * RECONSIDERATION != TASK_CREATION.
 * STEP_ELIGIBLE != STEP_AUTHORIZED.
 * RECONSIDERATION != AUTOMATIC_CONTINUATION.
 * RECONSIDERATION != CONTROLLED_AUTONOMY.
 */
class CompoundWorkReconsiderationCoordinator {

    fun evaluate(
        currentTraceId: TraceId,
        continuation: CompoundWorkContinuationResult,
        freshDecision: DecisionRecord,
        reconsiderationEstablished: Boolean,
    ): CompoundWorkReconsiderationResult {
        if (
            continuation.status !=
            CompoundWorkContinuationStatus.ELIGIBLE_FOR_RECONSIDERATION
        ) {
            return deferred(
                traceId = currentTraceId,
            )
        }

        val continuationRecord =
            continuation.record
                ?: return deferred(
                    traceId = currentTraceId,
                )

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
            continuationRecord
                .request
                .decision
                .understanding
                .context
                .traceId

        if (freshTraceId == originalTraceId) {
            return deferred(
                traceId = currentTraceId,
            )
        }

        if (!reconsiderationEstablished) {
            return deferred(
                traceId = currentTraceId,
            )
        }

        val request =
            CompoundWorkReconsiderationRequest.create(
                continuation = continuationRecord,
                freshDecision = freshDecision,
            )

        val record =
            CompoundWorkReconsiderationRecord.create(
                request = request,
            )

        return CompoundWorkReconsiderationResult.create(
            traceId = currentTraceId,
            status = CompoundWorkReconsiderationStatus.PREPARED,
            record = record,
        )
    }

    private fun deferred(
        traceId: TraceId,
    ): CompoundWorkReconsiderationResult {
        return CompoundWorkReconsiderationResult.create(
            traceId = traceId,
            status = CompoundWorkReconsiderationStatus.DEFERRED,
        )
    }
}
