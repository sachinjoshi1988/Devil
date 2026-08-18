package com.devil.core.runtime.task

/**
 * Stage 117 operational status for bounded compound-work Plan re-entry
 * preparation.
 *
 * PREPARED means only that:
 *
 * - Stage 116 Task re-entry preparation is valid;
 * - the current trace remains the Stage 115 fresh trace;
 * - that trace remains distinct from the originating Stage 77 trace;
 * - the existing Task Authority produced one TaskRecord;
 * - that TaskRecord remains in CREATED state;
 * - and that exact TaskRecord preserves the Stage 115 fresh Decision.
 *
 * PREPARED does not mean:
 *
 * - Plan created;
 * - PlanId generated;
 * - planning strategy selected;
 * - PlanAuthority invoked;
 * - capability selected;
 * - Executive ready;
 * - ExecutionRequest created;
 * - executed;
 * - observed;
 * - verified;
 * - successful;
 * - automatically continued;
 * - or Controlled Autonomy granted.
 *
 * DEFERRED means the bounded prerequisites for Plan re-entry preparation are
 * not currently established.
 *
 * TASK_CREATED != PLAN_CREATED.
 * PLAN_REENTRY_PREPARED != PLAN_CREATED.
 * PLAN_REENTRY_PREPARED != PLAN_AUTHORITY_RESULT.
 * PLAN_REENTRY != AUTOMATIC_CONTINUATION.
 * PLAN_REENTRY != CONTROLLED_AUTONOMY.
 */
enum class CompoundWorkPlanReentryStatus {
    PREPARED,
    DEFERRED,
}
