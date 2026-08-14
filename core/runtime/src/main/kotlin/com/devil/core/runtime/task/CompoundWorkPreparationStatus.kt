package com.devil.core.runtime.task

/**
 * Stage 77 operational status for bounded compound-work preparation.
 *
 * PREPARED means one structurally valid CompoundWorkRequest was preserved for a
 * selected constitutional Decision.
 *
 * DEFERRED means no justified compound work was prepared.
 *
 * PREPARED does not mean:
 *
 * - authorized;
 * - planned for execution;
 * - capability bound;
 * - ready;
 * - executed;
 * - observed;
 * - verified;
 * - completed;
 * - or successful.
 */
enum class CompoundWorkPreparationStatus {
    PREPARED,
    DEFERRED,
}
