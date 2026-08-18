package com.devil.core.model.task

/**
 * Immutable Stage 119 representation of bounded compound-work Executive
 * re-entry preparation.
 *
 * The exact CompoundWorkExecutiveReentryRequest remains attached so all
 * Stage 118 -> Stage 117 -> Stage 116 -> Stage 115 -> Stage 114 -> Stage 77
 * provenance remains preserved transitively.
 *
 * This record establishes only that bounded prerequisites for approaching the
 * existing Executive Readiness Authority were explicitly satisfied.
 *
 * It does not:
 *
 * - select or replace a capability;
 * - invoke CapabilitySelectionAuthority;
 * - establish Executive readiness;
 * - invoke ExecutiveReadinessAuthority;
 * - create an ExecutionRequest;
 * - invoke ExecutionAuthority;
 * - execute;
 * - create or mutate a PlanRecord;
 * - create or mutate a TaskRecord;
 * - create or replace a Decision;
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
 * CAPABILITY_SELECTED != EXECUTIVE_READY.
 * EXECUTIVE_REENTRY_PREPARED != EXECUTIVE_READY.
 * EXECUTIVE_REENTRY_PREPARED != EXECUTIVE_READINESS_RESULT.
 * CAPABILITY_REENTRY != EXECUTIVE_READINESS.
 * EXECUTIVE_REENTRY != EXECUTION_REQUEST.
 * EXECUTIVE_REENTRY != EXECUTION.
 * EXECUTIVE_REENTRY != AUTOMATIC_CONTINUATION.
 * EXECUTIVE_REENTRY != CONTROLLED_AUTONOMY.
 */
@ConsistentCopyVisibility
data class CompoundWorkExecutiveReentryRecord private constructor(
    val request: CompoundWorkExecutiveReentryRequest,
) {
    companion object {

        fun create(
            request: CompoundWorkExecutiveReentryRequest,
        ): CompoundWorkExecutiveReentryRecord {
            return CompoundWorkExecutiveReentryRecord(
                request = request,
            )
        }
    }
}
