package com.devil.core.runtime.task

import com.devil.core.model.common.TraceId
import com.devil.core.model.task.TaskRecord

/**
 * Default Stage 8 mapping from bounded TaskRecord values into the stable
 * TaskAuthorityResult contract.
 *
 * Every valid TaskRecord is mapped as CREATED. Its lifecycle state remains
 * unchanged inside the record and is not converted into operational deferral
 * or failure.
 *
 * This mapper performs no task creation, identity generation, planning,
 * capability authorization, execution, observation, or verification.
 */
class DefaultTaskCreationResultMapper :
    TaskCreationResultMapper {

    override fun map(
        traceId: TraceId,
        task: TaskRecord,
    ): TaskAuthorityResult {
        return TaskAuthorityResult.create(
            traceId = traceId,
            status = TaskAuthorityStatus.CREATED,
            task = task,
        )
    }
}
