package com.devil.core.runtime.observation

import com.devil.core.model.observation.ObservationRequest
import com.devil.core.runtime.execution.ExecutionAttemptResult
import com.devil.core.runtime.execution.ExecutionAttemptStatus

/**
 * Constitutional observation-request provider.
 *
 * A request is available only when the execution-attempt boundary produced
 * ATTEMPTED and preserved the exact ExecutionRequest genuinely attempted.
 *
 * DEFERRED means no justified observation request exists.
 *
 * FAILED preserves its matching operational error.
 *
 * ATTEMPTED != OBSERVED.
 */
class DefaultObservationRequestProvider :
    ObservationRequestProvider {

    override fun provide(
        executionAttempt: ExecutionAttemptResult,
    ): ObservationRequestResult {
        return when (executionAttempt.status) {
            ExecutionAttemptStatus.ATTEMPTED ->
                ObservationRequestResult.create(
                    traceId = executionAttempt.traceId,
                    status = ObservationRequestStatus.AVAILABLE,
                    request =
                        ObservationRequest.create(
                            execution =
                                requireNotNull(
                                    executionAttempt.request,
                                ),
                        ),
                )

            ExecutionAttemptStatus.DEFERRED ->
                ObservationRequestResult.create(
                    traceId = executionAttempt.traceId,
                    status = ObservationRequestStatus.UNAVAILABLE,
                )

            ExecutionAttemptStatus.FAILED ->
                ObservationRequestResult.create(
                    traceId = executionAttempt.traceId,
                    status = ObservationRequestStatus.FAILED,
                    error =
                        requireNotNull(
                            executionAttempt.error,
                        ),
                )
        }
    }
}
