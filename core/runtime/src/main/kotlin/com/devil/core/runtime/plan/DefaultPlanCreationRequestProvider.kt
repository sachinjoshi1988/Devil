package com.devil.core.runtime.plan

import com.devil.core.model.plan.PlanCreationRequest
import com.devil.core.model.task.TaskState
import com.devil.core.runtime.task.TaskAuthorityResult
import com.devil.core.runtime.task.TaskAuthorityStatus

/**
 * Default Stage 9 constitutional plan-creation request provider.
 *
 * A request is available only when the Task Authority created one bounded
 * TaskRecord whose TaskState is CREATED. Other task lifecycle states remain
 * unavailable. Task failure propagates its matching error.
 *
 * This implementation does not create planning strategy, generate plan
 * identity, create plans, bind or authorize capabilities, execute actions,
 * observe results, verify outcomes, or report final outcomes.
 */
class DefaultPlanCreationRequestProvider :
    PlanCreationRequestProvider {

    override fun provide(
        task: TaskAuthorityResult,
    ): PlanCreationRequestResult {
        return when (task.status) {
            TaskAuthorityStatus.CREATED -> {
                val record = requireNotNull(task.task)

                if (record.state == TaskState.CREATED) {
                    PlanCreationRequestResult.create(
                        traceId = task.traceId,
                        status = PlanCreationRequestStatus.AVAILABLE,
                        request = PlanCreationRequest.create(
                            task = record,
                        ),
                    )
                } else {
                    PlanCreationRequestResult.create(
                        traceId = task.traceId,
                        status = PlanCreationRequestStatus.UNAVAILABLE,
                    )
                }
            }

            TaskAuthorityStatus.DEFERRED ->
                PlanCreationRequestResult.create(
                    traceId = task.traceId,
                    status = PlanCreationRequestStatus.UNAVAILABLE,
                )

            TaskAuthorityStatus.FAILED ->
                PlanCreationRequestResult.create(
                    traceId = task.traceId,
                    status = PlanCreationRequestStatus.FAILED,
                    error = requireNotNull(task.error),
                )
        }
    }
}
