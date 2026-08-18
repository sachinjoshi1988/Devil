package com.devil.core.model.task

/**
 * Immutable Stage 116 representation of bounded compound-work Task re-entry
 * preparation.
 *
 * The exact CompoundWorkTaskReentryRequest remains attached so that the
 * complete Stage 115 -> Stage 114 -> Stage 77 provenance remains preserved.
 *
 * This record establishes only that bounded constitutional prerequisites for
 * approaching the existing Task Authority were explicitly satisfied.
 *
 * It does not:
 *
 * - create a TaskRecord;
 * - generate a TaskId;
 * - invoke TaskAuthority;
 * - grant authorization;
 * - create a Decision;
 * - create a Plan;
 * - select a capability;
 * - establish Executive readiness;
 * - create an ExecutionRequest;
 * - execute;
 * - observe;
 * - verify;
 * - establish Outcome;
 * - mutate World Model state;
 * - perform Learning;
 * - operate Memory;
 * - advance any CompoundWorkStep state;
 * - automatically continue compound work;
 * - grant Controlled Autonomy;
 * - invoke Android or another platform;
 * - or invoke a network.
 *
 * TASK_REENTRY_PREPARED != TASK_CREATED.
 * TASK_REENTRY_PREPARED != TASK_AUTHORITY_RESULT.
 * AUTHORIZATION != TASK_CREATED.
 * RECONSIDERATION != TASK_CREATED.
 * TASK_REENTRY != AUTOMATIC_CONTINUATION.
 * TASK_REENTRY != CONTROLLED_AUTONOMY.
 */
@ConsistentCopyVisibility
data class CompoundWorkTaskReentryRecord private constructor(
    val request: CompoundWorkTaskReentryRequest,
) {
    companion object {

        fun create(
            request: CompoundWorkTaskReentryRequest,
        ): CompoundWorkTaskReentryRecord {
            return CompoundWorkTaskReentryRecord(
                request = request,
            )
        }
    }
}
