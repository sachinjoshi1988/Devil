package com.devil.core.runtime.task

import com.devil.core.model.common.TraceId
import com.devil.core.model.task.TaskCreationRequest

/**
 * Default Stage 8 task identity provider.
 *
 * No constitutional task identity policy exists yet. Therefore this provider
 * preserves trace continuity and reports that no task identity is available
 * rather than fabricating one.
 *
 * This implementation does not generate task identities, create tasks, change
 * task lifecycle state, create plans, authorize capabilities, execute actions,
 * observe results, or verify outcomes.
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
            status = TaskIdentityProvisionStatus.UNAVAILABLE,
        )
    }
}
