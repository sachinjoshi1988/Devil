package com.devil.core.runtime.task

/**
 * Describes the operational result of the Task Authority.
 *
 * This status reports whether a TaskRecord was created. The task lifecycle
 * state belongs to TaskState inside that record.
 */
enum class TaskAuthorityStatus {
    CREATED,
    DEFERRED,
    FAILED,
}
