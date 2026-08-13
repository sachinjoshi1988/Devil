package com.devil.core.runtime.observation

import com.devil.core.model.common.TraceId
import com.devil.core.model.observation.ObservationRequest

/**
 * Default constitutional observation evaluator.
 *
 * Genuine neutral observation evidence may establish OBSERVED only when trace
 * identity and capability identity both match the ObservationRequest.
 *
 * DEFERRED evidence remains unavailable.
 *
 * FAILED evidence preserves its matching operational error.
 *
 * This evaluator performs no platform observation and does not verify outcomes,
 * report success, update world state, or establish an Outcome.
 */
class DefaultObservationEvaluator : ObservationEvaluator {

    override fun evaluate(
        traceId: TraceId,
        request: ObservationRequest,
        evidence: ObservationEvidenceResult,
    ): ObservationEvaluationResult {
        require(
            request.execution.plan.task.decision.understanding.context.traceId ==
                traceId,
        ) {
            "Observation evaluator trace and request must use the same trace identity."
        }

        require(evidence.traceId == traceId) {
            "Observation evaluator trace and evidence result must use the same trace identity."
        }

        return when (evidence.status) {
            ObservationEvidenceStatus.OBSERVED -> {
                require(
                    evidence.capabilityId ==
                        request.execution.capability.capabilityId,
                ) {
                    "Observation evidence and request must refer to the same capability identity."
                }

                ObservationEvaluationResult.create(
                    traceId = traceId,
                    status = ObservationEvaluationStatus.OBSERVED,
                    request = request,
                )
            }

            ObservationEvidenceStatus.DEFERRED ->
                ObservationEvaluationResult.create(
                    traceId = traceId,
                    status = ObservationEvaluationStatus.UNAVAILABLE,
                )

            ObservationEvidenceStatus.FAILED ->
                ObservationEvaluationResult.create(
                    traceId = traceId,
                    status = ObservationEvaluationStatus.FAILED,
                    error = requireNotNull(evidence.error),
                )
        }
    }
}
