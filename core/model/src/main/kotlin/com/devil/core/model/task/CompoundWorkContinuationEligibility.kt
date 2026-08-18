package com.devil.core.model.task

/**
 * Stage 114 bounded eligibility classification for one exact existing Stage 77
 * CompoundWorkStep preserved through Stage 113 compound-work state.
 *
 * ELIGIBLE_FOR_RECONSIDERATION means only that the exact represented step may
 * approach a fresh constitutional reasoning cycle.
 *
 * It does not mean:
 *
 * - authorization exists;
 * - a Brain Decision exists;
 * - a Task exists;
 * - a Plan exists;
 * - a capability was selected;
 * - capability readiness exists;
 * - Executive readiness exists;
 * - an ExecutionRequest exists;
 * - execution is approved;
 * - execution occurred;
 * - Observation exists;
 * - Verification exists;
 * - Outcome exists;
 * - Controlled Autonomy was granted;
 * - or compound work may continue automatically.
 *
 * DEFERRED means no justified next-step reconsideration eligibility is
 * established by Stage 114.
 *
 * ELIGIBLE_FOR_RECONSIDERATION != AUTHORIZED.
 * ELIGIBLE_FOR_RECONSIDERATION != BRAIN_DECISION.
 * ELIGIBLE_FOR_RECONSIDERATION != TASK_CREATED.
 * ELIGIBLE_FOR_RECONSIDERATION != PLAN_CREATED.
 * ELIGIBLE_FOR_RECONSIDERATION != CAPABILITY_SELECTED.
 * ELIGIBLE_FOR_RECONSIDERATION != EXECUTION_APPROVED.
 * STEP_COMPLETED != NEXT_STEP_AUTHORIZATION.
 * ELIGIBILITY != AUTOMATIC_CONTINUATION.
 * ELIGIBILITY != CONTROLLED_AUTONOMY.
 */
enum class CompoundWorkContinuationEligibility {
    ELIGIBLE_FOR_RECONSIDERATION,
    DEFERRED,
}
