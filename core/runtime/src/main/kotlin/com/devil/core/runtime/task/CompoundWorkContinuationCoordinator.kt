package com.devil.core.runtime.task

import com.devil.core.model.task.CompoundWorkContinuationRecord
import com.devil.core.model.task.CompoundWorkState
import com.devil.core.model.task.CompoundWorkStepState

/**
 * Stage 114 platform-independent coordinator for bounded compound-work
 * continuation eligibility.
 *
 * The coordinator consumes one exact Stage 113 CompoundWorkStateResult.
 *
 * It may identify at most one exact existing Stage 77 CompoundWorkStep as
 * eligible only for fresh constitutional reconsideration.
 *
 * Eligibility requires:
 *
 * - Stage 113 aggregate state ACTIVE;
 * - the candidate is the first non-completed ordered step;
 * - every preceding exact step is COMPLETED;
 * - the candidate exact step is PENDING;
 * - and no ACTIVE, BLOCKED, FAILED, PARTIAL, or terminal whole-work state is
 *   converted into continuation permission.
 *
 * The coordinator does not:
 *
 * - grant authorization;
 * - make or replace a Brain Decision;
 * - create a Task;
 * - create a Plan;
 * - select a capability;
 * - establish capability availability or readiness;
 * - establish Executive readiness;
 * - create an ExecutionRequest;
 * - execute;
 * - establish Observation;
 * - establish Verification;
 * - establish Outcome;
 * - automatically advance a step;
 * - mutate Stage 113 state;
 * - mutate World Model state;
 * - perform Learning;
 * - operate Memory;
 * - grant Controlled Autonomy;
 * - invoke a platform;
 * - invoke a network;
 * - or continue work automatically.
 *
 * STEP_COMPLETED != NEXT_STEP_AUTHORIZATION.
 * ELIGIBLE_FOR_RECONSIDERATION != AUTHORIZED.
 * ELIGIBLE_FOR_RECONSIDERATION != BRAIN_DECISION.
 * ELIGIBILITY != AUTOMATIC_CONTINUATION.
 * ELIGIBILITY != CONTROLLED_AUTONOMY.
 */
class CompoundWorkContinuationCoordinator {

    fun evaluate(
        stateResult: CompoundWorkStateResult,
    ): CompoundWorkContinuationResult {
        val traceId =
            stateResult.traceId

        if (stateResult.state != CompoundWorkState.ACTIVE) {
            return deferred(
                traceId = traceId,
            )
        }

        val orderedRecords =
            stateResult.request.steps.map { step ->
                stateResult.stepStates.single { stateRecord ->
                    stateRecord.step === step
                }
            }

        val candidateIndex =
            orderedRecords.indexOfFirst { record ->
                record.state != CompoundWorkStepState.COMPLETED
            }

        if (candidateIndex < 0) {
            return deferred(
                traceId = traceId,
            )
        }

        val precedingCompleted =
            orderedRecords
                .take(candidateIndex)
                .all { record ->
                    record.state == CompoundWorkStepState.COMPLETED
                }

        if (!precedingCompleted) {
            return deferred(
                traceId = traceId,
            )
        }

        val candidate =
            orderedRecords[candidateIndex]

        if (candidate.state != CompoundWorkStepState.PENDING) {
            return deferred(
                traceId = traceId,
            )
        }

        val record =
            CompoundWorkContinuationRecord.create(
                request = stateResult.request,
                step = candidate.step,
            )

        return CompoundWorkContinuationResult.create(
            traceId = traceId,
            status =
                CompoundWorkContinuationStatus
                    .ELIGIBLE_FOR_RECONSIDERATION,
            record = record,
        )
    }

    private fun deferred(
        traceId: com.devil.core.model.common.TraceId,
    ): CompoundWorkContinuationResult {
        return CompoundWorkContinuationResult.create(
            traceId = traceId,
            status = CompoundWorkContinuationStatus.DEFERRED,
        )
    }
}
