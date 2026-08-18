package com.devil.core.model.task

/**
 * Immutable Stage 115 representation of one bounded compound-work
 * reconsideration preparation.
 *
 * The exact CompoundWorkReconsiderationRequest remains attached so Stage 114
 * continuation provenance, the exact Stage 77 request and step, and the fresh
 * selected Decision remain structurally explicit.
 *
 * This record means only that the already-eligible exact step has been
 * explicitly related to a fresh selected constitutional Decision.
 *
 * It does not:
 *
 * - grant authorization;
 * - replace or bypass the Brain;
 * - create a Task;
 * - create a Plan;
 * - select or activate a capability;
 * - establish Executive readiness;
 * - create an ExecutionRequest;
 * - execute;
 * - establish Observation;
 * - establish Verification;
 * - establish Outcome;
 * - authorize the represented step;
 * - advance the represented step;
 * - continue compound work automatically;
 * - grant Controlled Autonomy;
 * - mutate World Model state;
 * - perform Learning;
 * - or operate Memory.
 *
 * RECONSIDERATION != AUTHORIZATION.
 * RECONSIDERATION != TASK_CREATION.
 * RECONSIDERATION != EXECUTION.
 * RECONSIDERATION != AUTOMATIC_CONTINUATION.
 * RECONSIDERATION != CONTROLLED_AUTONOMY.
 */
@ConsistentCopyVisibility
data class CompoundWorkReconsiderationRecord private constructor(
    val request: CompoundWorkReconsiderationRequest,
) {
    companion object {

        fun create(
            request: CompoundWorkReconsiderationRequest,
        ): CompoundWorkReconsiderationRecord {
            return CompoundWorkReconsiderationRecord(
                request = request,
            )
        }
    }
}
