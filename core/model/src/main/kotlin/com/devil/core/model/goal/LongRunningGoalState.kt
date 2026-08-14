package com.devil.core.model.goal

/**
 * Describes the lifecycle of one bounded long-running goal.
 *
 * ACTIVE means only that the goal remains intentionally open.
 *
 * ACTIVE does not mean that any Task, Plan, Capability, execution request,
 * authorization, session, or platform action remains active.
 *
 * WAITING means progress currently depends on a later constitutional cycle,
 * external condition, user input, or separately governed work.
 *
 * COMPLETED, CANCELLED, and FAILED describe the goal lifecycle only.
 */
enum class LongRunningGoalState {
    ACTIVE,
    WAITING,
    COMPLETED,
    CANCELLED,
    FAILED,
}
