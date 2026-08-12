package com.devil.core.runtime.task

import com.devil.core.model.common.TraceId
import com.devil.core.model.task.TaskCreationRequest
import com.devil.core.model.task.TaskId

/**
 * Default bounded constitutional Task identity provider.
 *
 * Stage 58 establishes one deterministic Task identity for the bounded
 * task-creation flow represented by the authoritative trace.
 *
 * The Task identity is derived as:
 *
 * task:<trace-id>
 *
 * This policy preserves traceability while keeping TaskId distinct from
 * TraceId as a separate constitutional identity type.
 *
 * Providing a TaskId does not:
 * - create a task;
 * - create a plan;
 * - authorize a capability;
 * - establish Android permission;
 * - execute an action;
 * - observe execution;
 * - verify an effect;
 * - establish an Outcome.
 */
class DefaultTaskIdentityProvider : TaskIdentityProvider {

    override fun provide(
        traceId: TraceId,
        request: TaskCreationRequest,
    ): TaskIdentityProvisionResult {
        require(
            request.decision.understanding.context.traceId ==
                traceId,
        ) {
            "Task identity trace and task-creation request must use the same trace identity."
        }

        return TaskIdentityProvisionResult.create(
            traceId = traceId,
            status = TaskIdentityProvisionStatus.AVAILABLE,
            taskId = TaskId.from(
                "task:${traceId.value}",
            ),
        )
    }
}
