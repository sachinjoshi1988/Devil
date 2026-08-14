package com.devil.core.model.goal

/**
 * Describes the bounded condition represented by one long-running-goal trigger.
 *
 * SCHEDULED_TIME represents a supplied absolute time condition.
 *
 * EXTERNAL_EVENT represents a supplied bounded event identity that may later
 * be compared with an independently observed event.
 *
 * Neither kind grants authorization or causes execution.
 */
enum class GoalTriggerKind {
    SCHEDULED_TIME,
    EXTERNAL_EVENT,
}
