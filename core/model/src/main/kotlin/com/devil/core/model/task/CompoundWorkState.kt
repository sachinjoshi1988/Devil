package com.devil.core.model.task

/**
 * Stage 113 aggregate state for one existing Stage 77 CompoundWorkRequest.
 *
 * This state summarizes bounded compound-work bookkeeping only.
 *
 * ACTIVE means at least one bounded step remains represented as pending or
 * active and no terminal whole-work conclusion is established here.
 *
 * BLOCKED means bounded compound work cannot currently progress.
 *
 * COMPLETED means every supplied bounded step state is COMPLETED.
 *
 * PARTIAL means supplied step states contain a mixture of completed and
 * non-completed terminal or blocked states.
 *
 * FAILED means supplied step-state evidence supports a bounded whole-work
 * failure representation under this Stage 113 contract.
 *
 * None of these values mutates TaskState or establishes constitutional Outcome.
 *
 * COMPOUND_WORK_STATE != TASK_STATE.
 * COMPOUND_WORK_COMPLETED != TASK_COMPLETED.
 * COMPOUND_WORK_FAILED != TASK_FAILED.
 * COMPOUND_WORK_STATE != OUTCOME_STATE.
 * PARTIAL != TOTAL_FAILURE.
 * COMPOUND_WORK_STATE != CONTROLLED_AUTONOMY.
 */
enum class CompoundWorkState {
    ACTIVE,
    BLOCKED,
    COMPLETED,
    PARTIAL,
    FAILED,
}
