package com.devil.core.runtime.goal

/**
 * Stage 78 operational status for bounded long-running-goal preparation.
 *
 * PREPARED means one valid long-running goal has been represented for later
 * governed lifecycle handling.
 *
 * PREPARED does not mean persisted, scheduled, authorized, executable, active
 * on a device, or automatically resumable.
 *
 * DEFERRED means no justified long-running goal was prepared.
 */
enum class LongRunningGoalPreparationStatus {
    PREPARED,
    DEFERRED,
}
