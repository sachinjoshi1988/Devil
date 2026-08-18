package com.devil.core.runtime.task

/**
 * Stage 119 operational status for bounded compound-work Executive re-entry
 * preparation.
 *
 * PREPARED means only that:
 *
 * - Stage 118 Capability re-entry was PREPARED;
 * - the current trace remains the fresh constitutional trace;
 * - that trace remains distinct from the originating Stage 77 trace;
 * - one current CapabilitySelectionResult is SELECTED;
 * - and one CapabilityContract exists.
 *
 * PREPARED does not mean:
 *
 * - Executive ready;
 * - ExecutiveReadinessAuthority invoked;
 * - ExecutionRequest created;
 * - execution approved;
 * - executed;
 * - observed;
 * - verified;
 * - successful;
 * - automatically continued;
 * - or Controlled Autonomy granted.
 *
 * CAPABILITY_SELECTED != EXECUTIVE_READY.
 * EXECUTIVE_REENTRY_PREPARED != EXECUTIVE_READY.
 * EXECUTIVE_REENTRY_PREPARED != EXECUTIVE_READINESS_RESULT.
 * EXECUTIVE_REENTRY != EXECUTION_REQUEST.
 * EXECUTIVE_REENTRY != EXECUTION.
 * EXECUTIVE_REENTRY != AUTOMATIC_CONTINUATION.
 * EXECUTIVE_REENTRY != CONTROLLED_AUTONOMY.
 */
enum class CompoundWorkExecutiveReentryStatus {
    PREPARED,
    DEFERRED,
}
