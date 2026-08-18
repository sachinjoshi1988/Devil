package com.devil.core.runtime.task

/**
 * Stage 116 operational status for bounded compound-work Task re-entry
 * preparation.
 *
 * PREPARED means only that:
 *
 * - Stage 115 produced a valid reconsideration record;
 * - the current trace remains the Stage 115 fresh trace;
 * - that trace remains distinct from the originating Stage 77 trace;
 * - the preserved fresh Decision remains SELECTED;
 * - and explicit current constitutional authorization is AUTHORIZED.
 *
 * PREPARED does not mean:
 *
 * - Task created;
 * - TaskId generated;
 * - TaskAuthority invoked;
 * - Plan created;
 * - capability selected;
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
 * DEFERRED means the bounded prerequisites for Task re-entry preparation are
 * not currently established.
 *
 * TASK_REENTRY_PREPARED != TASK_CREATED.
 * TASK_REENTRY_PREPARED != TASK_AUTHORITY_RESULT.
 * AUTHORIZATION != TASK_CREATED.
 * TASK_REENTRY != AUTOMATIC_CONTINUATION.
 * TASK_REENTRY != CONTROLLED_AUTONOMY.
 */
enum class CompoundWorkTaskReentryStatus {
    PREPARED,
    DEFERRED,
}
