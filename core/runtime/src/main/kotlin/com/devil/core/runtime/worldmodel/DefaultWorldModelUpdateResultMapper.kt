package com.devil.core.runtime.worldmodel

import com.devil.core.model.common.TraceId

/**
 * Maps bounded World Model update-evaluation results into the stable
 * WorldModelUpdateResult contract.
 *
 * APPLICABLE evaluation must preserve both:
 *
 * - the bounded WorldModelUpdateRequest; and
 * - the evidence-backed WorldModelRepresentation established during evaluation.
 *
 * The mapper never manufactures a representation from task text, plan text,
 * capability metadata, outcome, or any other non-evidence source. If an
 * APPLICABLE evaluation reaches this default production mapper without its
 * representation, mapping fails closed.
 *
 * Evaluation unavailability becomes DEFERRED. Evaluation failure preserves its
 * matching error.
 *
 * This mapper does not create World Model evidence, mutate world state, claim
 * that state changed, change task or plan state, create Learning or Memory,
 * communicate externally, or bypass unified runtime handling.
 *
 * WORLD_MODEL_UPDATE_EVIDENCE != WORLD_MODEL_REPRESENTATION.
 * WORLD_MODEL_REPRESENTATION != WORLD_STATE_MUTATION.
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
                    status =
                        WorldModelUpdateStatus.APPLICABLE,
                    request =
                        requireNotNull(
                            evaluation.request,
                        ),
                    representation =
                        requireNotNull(
                            evaluation.representation,
                        ) {
                            "Applicable World Model update evaluation must preserve one evidence-backed representation before stable World Model result mapping."
                        },
                )

            WorldModelUpdateEvaluationStatus.UNAVAILABLE ->
                WorldModelUpdateResult.create(
                    traceId = traceId,
                    status =
                        WorldModelUpdateStatus.DEFERRED,
                )

            WorldModelUpdateEvaluationStatus.FAILED ->
                WorldModelUpdateResult.create(
                    traceId = traceId,
                    status =
                        WorldModelUpdateStatus.FAILED,
                    error =
                        requireNotNull(
                            evaluation.error,
                        ),
                )
        }
    }
}
