package com.devil.core.model.task

import com.devil.core.model.outcome.OutcomeRecord

/**
 * Immutable Stage 113 state record for one exact Stage 77 CompoundWorkStep.
 *
 * The exact CompoundWorkStep remains attached so its ordered position and
 * bounded intention are preserved.
 *
 * An optional OutcomeRecord may be supplied only as already-established
 * constitutional evidence. This record does not manufacture an Outcome and
 * does not reinterpret OutcomeState as TaskState.
 *
 * Creating this record does not:
 *
 * - create or mutate a TaskRecord;
 * - mutate TaskState;
 * - create or mutate a PlanRecord;
 * - grant authorization;
 * - select or activate a capability;
 * - establish Executive readiness;
 * - create an ExecutionRequest;
 * - execute;
 * - establish Observation;
 * - establish Verification;
 * - establish a new Outcome;
 * - change an existing Outcome;
 * - authorize another compound-work step;
 * - continue compound work automatically;
 * - grant Controlled Autonomy;
 * - mutate World Model state;
 * - perform Learning;
 * - create or persist Memory;
 * - or claim total compound-work completion.
 *
 * STEP_COMPLETED != NEXT_STEP_AUTHORIZATION.
 * TASK_LIFECYCLE != OUTCOME_STATE.
 * VERIFIED_SUCCESS != TASK_COMPLETED.
 * VERIFIED_FAILURE != TASK_FAILED.
 */
@ConsistentCopyVisibility
data class CompoundWorkStepStateRecord private constructor(
    val step: CompoundWorkStep,
    val state: CompoundWorkStepState,
    val outcome: OutcomeRecord?,
) {
    companion object {

        fun create(
            step: CompoundWorkStep,
            state: CompoundWorkStepState,
            outcome: OutcomeRecord? = null,
        ): CompoundWorkStepStateRecord {
            return CompoundWorkStepStateRecord(
                step = step,
                state = state,
                outcome = outcome,
            )
        }
    }
}
