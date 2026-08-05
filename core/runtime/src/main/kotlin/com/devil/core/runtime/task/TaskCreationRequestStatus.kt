package com.devil.core.runtime.task

/**
 * Describes whether a bounded constitutional task-creation request is available.
 *
 * This status reports request availability only. It does not create tasks,
 * select plans, authorize capabilities, execute actions, observe results, or
 * verify outcomes.
 */
enum class TaskCreationRequestStatus {
    AVAILABLE,
    UNAVAILABLE,
    FAILED,
}
