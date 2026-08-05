package com.devil.core.runtime.task

import com.devil.core.model.common.TraceId
import com.devil.core.model.task.TaskRecord

/**
 * Translates one bounded constitutional TaskRecord into the stable operational
 * Task Authority result contract.
 *
 * Task lifecycle state remains represented by TaskState inside the record.
 * This mapper does not create tasks, generate identity, create plans, authorize
 * capabilities, execute actions, observe results, or verify outcomes.
 */
interface TaskCreationResultMapper {

    fun map(
        traceId: TraceId,
        task: TaskRecord,
    ): TaskAuthorityResult
}
