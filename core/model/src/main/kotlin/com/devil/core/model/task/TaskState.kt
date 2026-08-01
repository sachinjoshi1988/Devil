package com.devil.core.model.task

/**
 * Describes the constitutional lifecycle state of a task.
 *
 * This state reflects the task lifecycle only. It does not indicate capability
 * execution, platform execution, or final outcome verification.
 */
enum class TaskState {
    CREATED,
    WAITING,
    ACTIVE,
    COMPLETED,
    CANCELLED,
    FAILED,
}
