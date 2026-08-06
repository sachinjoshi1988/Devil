package com.devil.core.runtime.worldmodel

import com.devil.core.model.worldmodel.WorldModelUpdateRequest
import com.devil.core.runtime.outcome.OutcomeResult
import com.devil.core.runtime.outcome.OutcomeStatus

/**
 * Default Stage 16 constitutional World Model update-request provider.
 *
 * A request is available only when constitutional outcome evaluation produced
 * an ESTABLISHED OutcomeResult containing one bounded OutcomeRequest.
 *
 * Deferred outcome evaluation remains unavailable. Outcome failure preserves
 * its matching error.
 *
 * This implementation does not mutate world state, claim that world state
 * changed, change task or plan state, create memory or learning, communicate
 * externally, or produce a runtime result.
 */
class DefaultWorldModelUpdateRequestProvider :
    WorldModelUpdateRequestProvider {

    override fun provide(
        outcome: OutcomeResult,
    ): WorldModelUpdateRequestResult {
        return when (outcome.status) {
            OutcomeStatus.ESTABLISHED ->
                WorldModelUpdateRequestResult.create(
                    traceId = outcome.traceId,
                    status = WorldModelUpdateRequestStatus.AVAILABLE,
                    request = WorldModelUpdateRequest.create(
                        outcome = requireNotNull(outcome.request),
                    ),
                )

            OutcomeStatus.DEFERRED ->
                WorldModelUpdateRequestResult.create(
                    traceId = outcome.traceId,
                    status = WorldModelUpdateRequestStatus.UNAVAILABLE,
                )

            OutcomeStatus.FAILED ->
                WorldModelUpdateRequestResult.create(
                    traceId = outcome.traceId,
                    status = WorldModelUpdateRequestStatus.FAILED,
                    error = requireNotNull(outcome.error),
                )
        }
    }
}
