package com.devil.core.runtime.task

import com.devil.core.model.task.TaskCreationRequest
import com.devil.core.runtime.decision.DecisionAuthorityResult
import com.devil.core.runtime.decision.DecisionAuthorityStatus
import com.devil.core.model.decision.DecisionState

/**
 * Default Stage 8 constitutional task-creation request provider.
 *
 * A request is available only when the Decision Authority produced one bounded
 * DecisionRecord whose DecisionState is SELECTED. Deferred, rejected, or
 * clarification decisions remain unavailable. Decision failure propagates its
 * matching error.
 *
 * This implementation does not create tasks, reinterpret decisions, select
 * plans, authorize capabilities, execute actions, observe results, or verify
 * outcomes.
 */
class DefaultTaskCreationRequestProvider :
    TaskCreationRequestProvider {

    override fun provide(
        decision: DecisionAuthorityResult,
    ): TaskCreationRequestResult {
        return when (decision.status) {
            DecisionAuthorityStatus.PRODUCED -> {
                val record = requireNotNull(decision.decision)

                if (record.state == DecisionState.SELECTED) {
                    TaskCreationRequestResult.create(
                        traceId = decision.traceId,
                        status = TaskCreationRequestStatus.AVAILABLE,
                        request = TaskCreationRequest.create(
                            decision = record,
                        ),
                    )
                } else {
                    TaskCreationRequestResult.create(
                        traceId = decision.traceId,
                        status = TaskCreationRequestStatus.UNAVAILABLE,
                    )
                }
            }

            DecisionAuthorityStatus.DEFERRED ->
                TaskCreationRequestResult.create(
                    traceId = decision.traceId,
                    status = TaskCreationRequestStatus.UNAVAILABLE,
                )

            DecisionAuthorityStatus.FAILED ->
                TaskCreationRequestResult.create(
                    traceId = decision.traceId,
                    status = TaskCreationRequestStatus.FAILED,
                    error = requireNotNull(decision.error),
                )
        }
    }
}
