package com.devil.core.runtime.task

import com.devil.core.model.common.TraceId
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.task.TaskId

/**
 * Represents the result of attempting to supply one genuine task identity.
 *
 * An available result contains one TaskId. An unavailable result contains
 * neither task identity nor error. A failed result contains a matching error.
 *
 * This result does not generate task identity, create tasks, change task
 * lifecycle state, create plans, authorize capabilities, execute actions,
 * observe results, or verify outcomes.
 */
@ConsistentCopyVisibility
data class TaskIdentityProvisionResult private constructor(
    val traceId: TraceId,
    val status: TaskIdentityProvisionStatus,
    val taskId: TaskId?,
    val error: UniversalErrorRecord?,
) {
    companion object {
        fun create(
            traceId: TraceId,
            status: TaskIdentityProvisionStatus,
            taskId: TaskId? = null,
            error: UniversalErrorRecord? = null,
        ): TaskIdentityProvisionResult {
            when (status) {
                TaskIdentityProvisionStatus.AVAILABLE -> {
                    require(taskId != null && error == null) {
                        "Available task identity results require a task identity and must not contain an error."
                    }
                }

                TaskIdentityProvisionStatus.UNAVAILABLE -> {
                    require(taskId == null && error == null) {
                        "Unavailable task identity results must not contain a task identity or error."
                    }
                }

                TaskIdentityProvisionStatus.FAILED -> {
                    require(taskId == null && error != null) {
                        "Failed task identity results require an error and must not contain a task identity."
                    }
                }
            }

            require(error == null || error.traceId == traceId) {
                "Task identity result and error must use the same trace identity."
            }

            return TaskIdentityProvisionResult(
                traceId = traceId,
                status = status,
                taskId = taskId,
                error = error,
            )
        }
    }
}
