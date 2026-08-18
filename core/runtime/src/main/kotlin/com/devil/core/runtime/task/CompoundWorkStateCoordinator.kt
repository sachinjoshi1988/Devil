package com.devil.core.runtime.task

import com.devil.core.model.task.CompoundWorkRequest
import com.devil.core.model.task.CompoundWorkState
import com.devil.core.model.task.CompoundWorkStepState
import com.devil.core.model.task.CompoundWorkStepStateRecord

/**
 * Stage 113 platform-independent coordinator for bounded compound-work state.
 *
 * The coordinator accepts:
 *
 * - one exact existing Stage 77 CompoundWorkRequest; and
 * - exactly one explicitly supplied state record for every exact Stage 77 step.
 *
 * It derives only the bounded aggregate CompoundWorkState from already-supplied
 * step-state records.
 *
 * It does not inspect platform state, execute work, infer Outcome, mutate
 * TaskState, create Plans, authorize the next step, continue automatically, or
 * grant Controlled Autonomy.
 *
 * Aggregate rules are deliberately conservative:
 *
 * - all COMPLETED -> COMPLETED;
 * - any ACTIVE or PENDING -> ACTIVE unless a BLOCKED state is represented;
 * - any BLOCKED with no active/pending state -> BLOCKED unless completion plus
 *   another terminal non-completed state requires PARTIAL;
 * - completed plus FAILED/BLOCKED -> PARTIAL;
 * - all FAILED -> FAILED.
 *
 * These are compound-work bookkeeping semantics only.
 *
 * TASK_LIFECYCLE != OUTCOME_STATE.
 * VERIFIED_SUCCESS != TASK_COMPLETED.
 * VERIFIED_FAILURE != TASK_FAILED.
 * PARTIAL != TOTAL_FAILURE.
 * STEP_COMPLETED != NEXT_STEP_AUTHORIZATION.
 * COMPOUND_WORK_STATE != AUTOMATIC_CONTINUATION.
 * COMPOUND_WORK_STATE != CONTROLLED_AUTONOMY.
 */
class CompoundWorkStateCoordinator {

    fun assess(
        request: CompoundWorkRequest,
        stepStates: List<CompoundWorkStepStateRecord>,
    ): CompoundWorkStateResult {
        val traceId =
            request.decision
                .understanding
                .context
                .traceId

        require(stepStates.size == request.steps.size) {
            "Compound-work state assessment requires exactly one state record for every step."
        }

        require(
            request.steps.all { step ->
                stepStates.count { record ->
                    record.step === step
                } == 1
            },
        ) {
            "Compound-work state assessment requires one exact state record for every exact request step."
        }

        require(
            stepStates.all { record ->
                request.steps.any { step ->
                    step === record.step
                }
            },
        ) {
            "Compound-work state assessment must not introduce steps outside the supplied request."
        }

        val states =
            stepStates.map { record ->
                record.state
            }

        val state =
            when {
                states.all {
                    it == CompoundWorkStepState.COMPLETED
                } ->
                    CompoundWorkState.COMPLETED

                states.all {
                    it == CompoundWorkStepState.FAILED
                } ->
                    CompoundWorkState.FAILED

                states.any {
                    it == CompoundWorkStepState.COMPLETED
                } &&
                    states.any {
                        it == CompoundWorkStepState.FAILED ||
                            it == CompoundWorkStepState.BLOCKED
                    } ->
                    CompoundWorkState.PARTIAL

                states.any {
                    it == CompoundWorkStepState.BLOCKED
                } &&
                    states.none {
                        it == CompoundWorkStepState.ACTIVE ||
                            it == CompoundWorkStepState.PENDING
                    } ->
                    CompoundWorkState.BLOCKED

                else ->
                    CompoundWorkState.ACTIVE
            }

        return CompoundWorkStateResult.create(
            traceId = traceId,
            request = request,
            stepStates = stepStates,
            state = state,
        )
    }
}
