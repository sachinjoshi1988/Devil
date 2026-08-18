package com.devil.core.model.task

/**
 * Immutable Stage 118 representation of bounded compound-work Capability
 * Selection re-entry preparation.
 *
 * The exact CompoundWorkCapabilityReentryRequest remains attached so that
 * Stage 117, Stage 116, Stage 115, Stage 114, and Stage 77 provenance remain
 * preserved transitively.
 *
 * This record establishes only that bounded prerequisites for approaching the
 * existing Capability Selection Authority were explicitly satisfied.
 *
 * It does not:
 *
 * - create or mutate a PlanRecord;
 * - generate a PlanId;
 * - create or mutate a TaskRecord;
 * - create or replace a Decision;
 * - select a CapabilityContract;
 * - invoke CapabilitySelectionAuthority;
 * - establish capability availability;
 * - establish capability health;
 * - establish operating-system permission;
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
 * PLAN_CREATED != CAPABILITY_SELECTED.
 * CAPABILITY_REENTRY_PREPARED != CAPABILITY_SELECTED.
 * CAPABILITY_REENTRY_PREPARED != CAPABILITY_SELECTION_RESULT.
 * PLAN_REENTRY != CAPABILITY_SELECTION.
 * CAPABILITY_REENTRY != EXECUTIVE_READINESS.
 * CAPABILITY_REENTRY != EXECUTION.
 * CAPABILITY_REENTRY != AUTOMATIC_CONTINUATION.
 * CAPABILITY_REENTRY != CONTROLLED_AUTONOMY.
 */
@ConsistentCopyVisibility
data class CompoundWorkCapabilityReentryRecord private constructor(
    val request: CompoundWorkCapabilityReentryRequest,
) {
    companion object {

        fun create(
            request: CompoundWorkCapabilityReentryRequest,
        ): CompoundWorkCapabilityReentryRecord {
            return CompoundWorkCapabilityReentryRecord(
                request = request,
            )
        }
    }
}
