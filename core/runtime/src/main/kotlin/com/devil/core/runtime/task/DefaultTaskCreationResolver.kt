package com.devil.core.runtime.task

import com.devil.core.model.task.TaskCreationRequest
import com.devil.core.model.task.TaskId
import com.devil.core.model.task.TaskRecord
import com.devil.core.model.task.TaskState

/**
 * Default Stage 8 constitutional task-creation resolver.
 *
 * This resolver preserves the selected DecisionRecord, applies the supplied
 * genuine TaskId, and creates one TaskRecord in the CREATED lifecycle state.
 *
 * It does not generate task identity, reinterpret decisions, create plans,
 * bind or authorize capabilities, execute actions, observe results, or verify
 * outcomes.
 */
class DefaultTaskCreationResolver : TaskCreationResolver {

    override fun create(
        request: TaskCreationRequest,
        taskId: TaskId,
    ): TaskRecord {
        return TaskRecord.create(
            taskId = taskId,
            decision = request.decision,
            state = TaskState.CREATED,
            summary = request.decision.summary,
        )
    }
}
