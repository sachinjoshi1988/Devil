package com.devil.core.runtime.task

import com.devil.core.model.common.TraceId
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.task.TaskRecord

/**
 * Represents the structured operational result of task creation.
 *
 * A created result contains a TaskRecord. A deferred result contains neither
 * task nor error. A failed result contains a matching error.
 */
@ConsistentCopyVisibility
data class TaskAuthorityResult private constructor(
    val traceId: TraceId,
    val status: TaskAuthorityStatus,
    val task: TaskRecord?,
    val error: UniversalErrorRecord?,
) {
    companion object {
        fun create(
            traceId: TraceId,
            status: TaskAuthorityStatus,
            task: TaskRecord? = null,
            error: UniversalErrorRecord? = null,
        ): TaskAuthorityResult {
            when (status) {
                TaskAuthorityStatus.CREATED -> {
                    require(task != null && error == null) {
                        "Created task results require a task and must not contain an error."
                    }
                }

                TaskAuthorityStatus.DEFERRED -> {
                    require(task == null && error == null) {
                        "Deferred task results must not contain a task or error."
                    }
                }

                TaskAuthorityStatus.FAILED -> {
                    require(task == null && error != null) {
                        "Failed task results require an error and must not contain a task."
                    }
                }
            }

            require(
                task == null ||
                    task.decision.understanding.context.traceId == traceId,
            ) {
                "Task result and task must use the same trace identity."
            }

            require(error == null || error.traceId == traceId) {
                "Task result and error must use the same trace identity."
            }

            return TaskAuthorityResult(
                traceId = traceId,
                status = status,
                task = task,
                error = error,
            )
        }
    }
}
