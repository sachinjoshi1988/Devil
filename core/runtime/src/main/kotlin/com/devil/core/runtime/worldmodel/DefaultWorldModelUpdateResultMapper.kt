package com.devil.core.runtime.worldmodel

import com.devil.core.model.common.TraceId

/**
 * Default Stage 16 mapping from bounded World Model update-evaluation results
 * into the stable WorldModelUpdateResult contract.
 *
 * Genuine constitutional update evidence becomes operational APPLICABLE and
 * preserves the bounded WorldModelUpdateRequest. Evaluation unavailability
 * becomes DEFERRED. Evaluation failure preserves its matching error.
 *
 * This mapper does not mutate world state, claim that state changed, change
 * task or plan state, create memory or learning, communicate externally, or
 * bypass unified runtime handling.
 */
class DefaultWorldModelUpdateResultMapper :
    WorldModelUpdateResultMapper {

    override fun map(
        traceId: TraceId,
        evaluation: WorldModelUpdateEvaluationResult,
    ): WorldModelUpdateResult {
        require(evaluation.traceId == traceId) {
            "World Model update result mapper trace and evaluation result must use the same trace identity."
        }

        return when (evaluation.status) {
            WorldModelUpdateEvaluationStatus.APPLICABLE ->
                WorldModelUpdateResult.create(
                    traceId = traceId,
                    status = WorldModelUpdateStatus.APPLICABLE,
                    request = requireNotNull(evaluation.request),
                )

            WorldModelUpdateEvaluationStatus.UNAVAILABLE ->
                WorldModelUpdateResult.create(
                    traceId = traceId,
                    status = WorldModelUpdateStatus.DEFERRED,
                )

            WorldModelUpdateEvaluationStatus.FAILED ->
                WorldModelUpdateResult.create(
                    traceId = traceId,
                    status = WorldModelUpdateStatus.FAILED,
                    error = requireNotNull(evaluation.error),
                )
        }
    }
}
