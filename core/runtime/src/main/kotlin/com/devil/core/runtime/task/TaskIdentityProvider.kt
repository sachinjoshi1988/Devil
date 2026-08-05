package com.devil.core.runtime.task

import com.devil.core.model.common.TraceId
import com.devil.core.model.task.TaskCreationRequest

/**
 * Supplies one genuine task identity for a bounded task-creation request when
 * an authorized identity policy is available.
 *
 * This provider must not fabricate task identities. It does not create tasks,
 * change task lifecycle state, create plans, authorize capabilities, execute
 * actions, observe results, or verify outcomes.
 */
interface TaskIdentityProvider {

    fun provide(
        traceId: TraceId,
        request: TaskCreationRequest,
    ): TaskIdentityProvisionResult
}
