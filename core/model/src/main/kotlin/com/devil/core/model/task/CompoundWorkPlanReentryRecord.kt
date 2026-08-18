package com.devil.core.model.task

/**
 * Immutable Stage 117 representation of bounded compound-work Plan re-entry
 * preparation.
 *
 * The exact CompoundWorkPlanReentryRequest remains attached so that Stage 116,
 * Stage 115, Stage 114, and Stage 77 provenance remain preserved transitively.
 *
 * This record establishes only that bounded prerequisites for approaching the
 * existing Plan Authority were explicitly satisfied.
 *
 * It does not:
 *
 * - create or mutate a TaskRecord;
 * - generate a TaskId;
 * - create a PlanRecord;
 * - generate a PlanId;
 * - invent planning strategy;
 * - invoke PlanAuthority;
 * - grant authorization;
 * - create or replace a Decision;
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
 * - change any CompoundWorkStep state;
 * - automatically continue compound work;
 * - grant Controlled Autonomy;
 * - invoke Android or another platform;
 * - or invoke a network.
 *
 * TASK_CREATED != PLAN_CREATED.
 * PLAN_REENTRY_PREPARED != PLAN_CREATED.
 * PLAN_REENTRY_PREPARED != PLAN_AUTHORITY_RESULT.
 * TASK_REENTRY != PLAN_CREATION.
 * PLAN_REENTRY != AUTOMATIC_CONTINUATION.
 * PLAN_REENTRY != CONTROLLED_AUTONOMY.
 */
@ConsistentCopyVisibility
data class CompoundWorkPlanReentryRecord private constructor(
    val request: CompoundWorkPlanReentryRequest,
) {
    companion object {

        fun create(
            request: CompoundWorkPlanReentryRequest,
        ): CompoundWorkPlanReentryRecord {
            return CompoundWorkPlanReentryRecord(
                request = request,
            )
        }
    }
}
