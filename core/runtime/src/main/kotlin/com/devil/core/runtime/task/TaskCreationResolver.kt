package com.devil.core.runtime.task

import com.devil.core.model.task.TaskCreationRequest
import com.devil.core.model.task.TaskId
import com.devil.core.model.task.TaskRecord

/**
 * Creates one bounded constitutional TaskRecord from a structured task-creation
 * request and one genuine task identity.
 *
 * This resolver does not generate task identity, reinterpret decisions, create
 * plans, bind or authorize capabilities, execute actions, observe results, or
 * verify outcomes.
 */
interface TaskCreationResolver {

    fun create(
        request: TaskCreationRequest,
        taskId: TaskId,
    ): TaskRecord
}
