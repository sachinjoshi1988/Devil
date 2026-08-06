package com.devil.core.runtime.observation

import com.devil.core.model.observation.ObservationRequest
import com.devil.core.runtime.execution.ExecutionResult
import com.devil.core.runtime.execution.ExecutionStatus

/**
 * Default Stage 13 constitutional observation-request provider.
 *
 * A request is available only when constitutional execution evaluation produced
 * an APPROVED ExecutionResult containing one bounded ExecutionRequest.
 *
 * Deferred execution remains unavailable. Execution failure preserves its
 * matching error.
 *
 * This implementation does not claim that an action was attempted, create
 * observation evidence, infer execution results, verify outcomes, or report
 * final success.
 */
class DefaultObservationRequestProvider :
    ObservationRequestProvider {

    override fun provide(
        execution: ExecutionResult,
    ): ObservationRequestResult {
        return when (execution.status) {
            ExecutionStatus.APPROVED ->
                ObservationRequestResult.create(
                    traceId = execution.traceId,
                    status = ObservationRequestStatus.AVAILABLE,
                    request = ObservationRequest.create(
                        execution =
                            requireNotNull(execution.request),
                    ),
                )

            ExecutionStatus.DEFERRED ->
                ObservationRequestResult.create(
                    traceId = execution.traceId,
                    status = ObservationRequestStatus.UNAVAILABLE,
                )

            ExecutionStatus.FAILED ->
                ObservationRequestResult.create(
                    traceId = execution.traceId,
                    status = ObservationRequestStatus.FAILED,
                    error = requireNotNull(execution.error),
                )
        }
    }
}
