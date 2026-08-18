package com.devil.core.runtime.task

import com.devil.core.model.common.TraceId
import com.devil.core.model.task.CompoundWorkRequest
import com.devil.core.model.task.CompoundWorkState
import com.devil.core.model.task.CompoundWorkStepStateRecord

/**
 * Stable Stage 113 result of bounded compound-work state assessment.
 *
 * The exact Stage 77 CompoundWorkRequest remains attached together with exactly
 * one state record for each exact CompoundWorkStep.
 *
 * This result does not:
 *
 * - mutate the Stage 77 request;
 * - mutate TaskState;
 * - create a Plan;
 * - authorize any step;
 * - approve execution;
 * - execute;
 * - establish Observation;
 * - establish Verification;
 * - establish Outcome;
 * - automatically select another step;
 * - automatically continue compound work;
 * - grant Controlled Autonomy;
 * - mutate World Model state;
 * - perform Learning;
 * - or operate Memory.
 *
 * STEP_COMPLETED != NEXT_STEP_AUTHORIZATION.
 * COMPOUND_WORK_STATE != AUTOMATIC_CONTINUATION.
 * COMPOUND_WORK_STATE != CONTROLLED_AUTONOMY.
 */
@ConsistentCopyVisibility
data class CompoundWorkStateResult private constructor(
    val traceId: TraceId,
    val request: CompoundWorkRequest,
    val stepStates: List<CompoundWorkStepStateRecord>,
    val state: CompoundWorkState,
) {
    companion object {

        fun create(
            traceId: TraceId,
            request: CompoundWorkRequest,
            stepStates: List<CompoundWorkStepStateRecord>,
            state: CompoundWorkState,
        ): CompoundWorkStateResult {
            require(
                request.decision
                    .understanding
                    .context
                    .traceId == traceId,
            ) {
                "Compound-work state result and request must use the same trace identity."
            }

            require(stepStates.size == request.steps.size) {
                "Compound-work state requires exactly one state record for every compound-work step."
            }

            require(
                stepStates.map { it.step }.toSet().size ==
                    stepStates.size,
            ) {
                "Compound-work state must not contain duplicate step records."
            }

            require(
                stepStates.all { record ->
                    request.steps.any { step ->
                        step === record.step
                    }
                },
            ) {
                "Compound-work state may contain only exact steps preserved by the supplied compound-work request."
            }

            require(
                request.steps.all { step ->
                    stepStates.any { record ->
                        record.step === step
                    }
                },
            ) {
                "Every compound-work step requires one exact state record."
            }

            return CompoundWorkStateResult(
                traceId = traceId,
                request = request,
                stepStates = stepStates.toList(),
                state = state,
            )
        }
    }
}
