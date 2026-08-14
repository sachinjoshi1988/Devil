package com.devil.core.runtime.worldmodel

import com.devil.core.model.common.TraceId
import com.devil.core.model.worldmodel.WorldModelRepresentation
import com.devil.core.model.worldmodel.WorldModelUpdateRequest

/**
 * Default constitutional World Model update evaluator.
 *
 * The evaluator remains fail-closed unless genuine World Model update evidence
 * has been established.
 *
 * ESTABLISHED evidence makes the bounded update request constitutionally
 * applicable for the next World Model authority/result step and preserves that
 * evidence as one immutable WorldModelRepresentation.
 *
 * The representation is created only from the already-established evidence
 * trace identity, capability identity, and description. The evaluator does not
 * invent or reinterpret world state.
 *
 * DEFERRED evidence remains unavailable.
 *
 * FAILED evidence preserves its matching operational error.
 */
class DefaultWorldModelUpdateEvaluator :
    WorldModelUpdateEvaluator {

    override fun evaluate(
        traceId: TraceId,
        request: WorldModelUpdateRequest,
        evidence: WorldModelUpdateEvidenceResult,
    ): WorldModelUpdateEvaluationResult {
        require(
            request.outcome
                .verification
                .observation
                .execution
                .plan
                .task
                .decision
                .understanding
                .context
                .traceId == traceId,
        ) {
            "World Model update request and evaluator must use the same trace identity."
        }

        require(evidence.traceId == traceId) {
            "World Model update evidence and evaluator must use the same trace identity."
        }

        return when (evidence.status) {
            WorldModelUpdateEvidenceStatus.ESTABLISHED -> {
                val capabilityId =
                    request.outcome
                        .verification
                        .observation
                        .execution
                        .capability
                        .capabilityId

                require(evidence.capabilityId == capabilityId) {
                    "World Model update request and evidence must refer to the same capability identity."
                }

                val representation =
                    WorldModelRepresentation.create(
                        traceId = evidence.traceId,
                        capabilityId =
                            requireNotNull(
                                evidence.capabilityId,
                            ),
                        description =
                            requireNotNull(
                                evidence.description,
                            ),
                    )

                WorldModelUpdateEvaluationResult.create(
                    traceId = traceId,
                    status =
                        WorldModelUpdateEvaluationStatus.APPLICABLE,
                    request = request,
                    representation = representation,
                )
            }

            WorldModelUpdateEvidenceStatus.DEFERRED ->
                WorldModelUpdateEvaluationResult.create(
                    traceId = traceId,
                    status =
                        WorldModelUpdateEvaluationStatus.UNAVAILABLE,
                )

            WorldModelUpdateEvidenceStatus.FAILED ->
                WorldModelUpdateEvaluationResult.create(
                    traceId = traceId,
                    status =
                        WorldModelUpdateEvaluationStatus.FAILED,
                    error = requireNotNull(evidence.error),
                )
        }
    }
}
