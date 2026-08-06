package com.devil.core.runtime.observation

import com.devil.core.model.common.TraceId

/**
 * Default Stage 13 mapping from bounded observation-evaluation results into the
 * stable ObservationResult contract.
 *
 * Genuine observation evidence becomes operational OBSERVED and preserves the
 * bounded ObservationRequest. Evaluation unavailability becomes DEFERRED.
 * Evaluation failure preserves its matching error.
 *
 * This mapper does not verify outcomes, report success, update world state, or
 * produce a final outcome.
 */
class DefaultObservationResultMapper :
    ObservationResultMapper {

    override fun map(
        traceId: TraceId,
        evaluation: ObservationEvaluationResult,
    ): ObservationResult {
        require(evaluation.traceId == traceId) {
            "Observation result mapper trace and evaluation result must use the same trace identity."
        }

        return when (evaluation.status) {
            ObservationEvaluationStatus.OBSERVED ->
                ObservationResult.create(
                    traceId = traceId,
                    status = ObservationStatus.OBSERVED,
                    request = requireNotNull(evaluation.request),
                )

            ObservationEvaluationStatus.UNAVAILABLE ->
                ObservationResult.create(
                    traceId = traceId,
                    status = ObservationStatus.DEFERRED,
                )

            ObservationEvaluationStatus.FAILED ->
                ObservationResult.create(
                    traceId = traceId,
                    status = ObservationStatus.FAILED,
                    error = requireNotNull(evaluation.error),
                )
        }
    }
}
