package com.devil.core.model.task

/**
 * Stage 113 bounded lifecycle state for one existing CompoundWorkStep.
 *
 * This state describes compound-work bookkeeping only.
 *
 * It does not establish:
 *
 * - a constitutional TaskRecord;
 * - TaskState;
 * - a PlanRecord;
 * - authorization;
 * - capability selection;
 * - capability readiness;
 * - Executive readiness;
 * - execution approval;
 * - execution;
 * - Observation;
 * - Verification;
 * - Outcome;
 * - World Model mutation;
 * - Learning;
 * - Memory;
 * - Controlled Autonomy;
 * - or permission to continue automatically.
 *
 * PENDING means the bounded step has not yet reached a represented terminal
 * compound-work state.
 *
 * ACTIVE means the bounded step is represented as current compound-work
 * bookkeeping. ACTIVE does not mean execution is occurring.
 *
 * COMPLETED means the supplied governed evidence explicitly established that
 * this bounded step may be represented as completed for compound-work state
 * tracking.
 *
 * BLOCKED means the supplied governed evidence explicitly established that the
 * bounded step cannot currently progress.
 *
 * FAILED means the supplied governed evidence explicitly established a bounded
 * failed step state.
 *
 * STEP_STATE != TASK_STATE.
 * STEP_COMPLETED != TASK_COMPLETED.
 * STEP_FAILED != TASK_FAILED.
 * STEP_COMPLETED != VERIFIED_SUCCESS.
 * STEP_FAILED != VERIFIED_FAILURE.
 * STEP_COMPLETED != NEXT_STEP_AUTHORIZATION.
 * STEP_STATE != CONTROLLED_AUTONOMY.
 */
enum class CompoundWorkStepState {
    PENDING,
    ACTIVE,
    COMPLETED,
    BLOCKED,
    FAILED,
}
