package com.devil.core.model.task

/**
 * Immutable Stage 114 representation of one exact compound-work step that has
 * become eligible only for fresh constitutional reconsideration.
 *
 * The exact Stage 113 CompoundWorkStateResult provenance is represented by the
 * supplied exact Stage 77 CompoundWorkRequest and exact candidate
 * CompoundWorkStep.
 *
 * The candidate must already belong to the supplied request.
 *
 * This record does not:
 *
 * - alter the Stage 77 request;
 * - alter any Stage 113 step state;
 * - grant authorization;
 * - create or select a Brain Decision;
 * - create a Task;
 * - create a Plan;
 * - select or activate a capability;
 * - establish capability readiness;
 * - establish Executive readiness;
 * - create an ExecutionRequest;
 * - execute;
 * - establish Observation;
 * - establish Verification;
 * - establish Outcome;
 * - mutate World Model state;
 * - perform Learning;
 * - operate Memory;
 * - grant Controlled Autonomy;
 * - or continue compound work automatically.
 *
 * CONTINUATION_RECORD != CONTINUATION_AUTHORITY.
 * ELIGIBILITY != AUTHORIZATION.
 * STEP_COMPLETED != NEXT_STEP_AUTHORIZATION.
 * ELIGIBILITY != AUTOMATIC_CONTINUATION.
 */
@ConsistentCopyVisibility
data class CompoundWorkContinuationRecord private constructor(
    val request: CompoundWorkRequest,
    val step: CompoundWorkStep,
) {
    companion object {

        fun create(
            request: CompoundWorkRequest,
            step: CompoundWorkStep,
        ): CompoundWorkContinuationRecord {
            require(
                request.steps.any { requestStep ->
                    requestStep === step
                },
            ) {
                "Compound-work continuation candidate must be one exact step preserved by the supplied request."
            }

            return CompoundWorkContinuationRecord(
                request = request,
                step = step,
            )
        }
    }
}
