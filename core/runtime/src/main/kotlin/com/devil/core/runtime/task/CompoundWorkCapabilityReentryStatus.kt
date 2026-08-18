package com.devil.core.runtime.task

/**
 * Stage 118 operational status for bounded compound-work Capability Selection
 * re-entry preparation.
 *
 * PREPARED means only that:
 *
 * - Stage 117 produced a valid Plan re-entry record;
 * - the current trace remains the fresh reconsideration trace;
 * - that trace remains distinct from the originating Stage 77 trace;
 * - one current PlanAuthorityResult is CREATED;
 * - one PlanRecord exists in CREATED state;
 * - and that Plan preserves the exact Stage 117 Task.
 *
 * PREPARED does not mean:
 *
 * - capability selected;
 * - capability available;
 * - capability healthy;
 * - operating-system permission granted;
 * - Executive ready;
 * - ExecutionRequest created;
 * - execution approved;
 * - executed;
 * - observed;
 * - verified;
 * - successful;
 * - automatically continued;
 * - or Controlled Autonomy granted.
 *
 * DEFERRED means the bounded prerequisites for Capability Selection re-entry
 * preparation are not currently established.
 *
 * PLAN_CREATED != CAPABILITY_SELECTED.
 * CAPABILITY_REENTRY_PREPARED != CAPABILITY_SELECTED.
 * CAPABILITY_REENTRY_PREPARED != CAPABILITY_SELECTION_RESULT.
 * CAPABILITY_REENTRY != EXECUTIVE_READINESS.
 * CAPABILITY_REENTRY != EXECUTION.
 * CAPABILITY_REENTRY != AUTOMATIC_CONTINUATION.
 * CAPABILITY_REENTRY != CONTROLLED_AUTONOMY.
 */
enum class CompoundWorkCapabilityReentryStatus {
    PREPARED,
    DEFERRED,
}
