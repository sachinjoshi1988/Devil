package com.devil.core.runtime.task

/**
 * Stage 114 operational status for bounded compound-work continuation
 * eligibility.
 *
 * ELIGIBLE_FOR_RECONSIDERATION means one exact pending Stage 77 compound-work
 * step satisfied the Stage 114 sequential eligibility rules.
 *
 * It means only that the represented step may approach a fresh constitutional
 * reasoning cycle.
 *
 * DEFERRED means Stage 114 established no justified continuation candidate.
 *
 * ELIGIBLE_FOR_RECONSIDERATION does not mean:
 *
 * - authorized;
 * - Brain-selected;
 * - Task-created;
 * - Plan-created;
 * - capability-selected;
 * - Executive-ready;
 * - execution-approved;
 * - executed;
 * - observed;
 * - verified;
 * - successful;
 * - autonomously continued;
 * - or Controlled Autonomy granted.
 *
 * ELIGIBLE != AUTHORIZED.
 * ELIGIBLE != EXECUTED.
 * ELIGIBILITY != AUTOMATIC_CONTINUATION.
 * ELIGIBILITY != CONTROLLED_AUTONOMY.
 */
enum class CompoundWorkContinuationStatus {
    ELIGIBLE_FOR_RECONSIDERATION,
    DEFERRED,
}
