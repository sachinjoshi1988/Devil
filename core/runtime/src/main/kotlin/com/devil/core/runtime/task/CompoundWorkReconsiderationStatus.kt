package com.devil.core.runtime.task

/**
 * Stage 115 operational status for bounded compound-work reconsideration.
 *
 * PREPARED means:
 *
 * - Stage 114 established one exact step as eligible for reconsideration;
 * - one fresh selected constitutional Decision exists on a distinct trace;
 * - and the caller explicitly established that the fresh Decision applies to
 *   reconsideration of that exact eligible step.
 *
 * PREPARED does not mean:
 *
 * - authorized;
 * - Task-created;
 * - Plan-created;
 * - capability-selected;
 * - Executive-ready;
 * - execution-approved;
 * - executed;
 * - observed;
 * - verified;
 * - successful;
 * - automatically continued;
 * - or Controlled Autonomy granted.
 *
 * DEFERRED means no justified Stage 115 reconsideration record was prepared.
 *
 * PREPARED != AUTHORIZED.
 * PREPARED != TASK_CREATED.
 * PREPARED != EXECUTED.
 * RECONSIDERATION != AUTOMATIC_CONTINUATION.
 * RECONSIDERATION != CONTROLLED_AUTONOMY.
 */
enum class CompoundWorkReconsiderationStatus {
    PREPARED,
    DEFERRED,
}
