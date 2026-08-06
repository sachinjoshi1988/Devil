package com.devil.core.runtime.outcome

import com.devil.core.model.common.TraceId

/**
 * Default Stage 15 mapping from bounded outcome-evaluation results into the
 * stable OutcomeResult contract.
 *
 * Genuine constitutional outcome evidence becomes operational ESTABLISHED and
 * preserves the bounded OutcomeRequest. Evaluation unavailability becomes
 * DEFERRED. Evaluation failure preserves its matching error.
 *
 * This mapper does not update world state, change task or plan state, create
 * memory or learning, communicate externally, or bypass unified runtime
 * handling.
 */
class DefaultOutcomeResultMapper :
    OutcomeResultMapper {

    override fun map(
        traceId: TraceId,
        evaluation: OutcomeEvaluationResult,
    ): OutcomeResult {
        require(evaluation.traceId == traceId) {
            "Outcome result mapper trace and evaluation result must use the same trace identity."
        }

        return when (evaluation.status) {
            OutcomeEvaluationStatus.ESTABLISHED ->
                OutcomeResult.create(
                    traceId = traceId,
                    status = OutcomeStatus.ESTABLISHED,
                    request = requireNotNull(evaluation.request),
                )

            OutcomeEvaluationStatus.UNAVAILABLE ->
                OutcomeResult.create(
                    traceId = traceId,
                    status = OutcomeStatus.DEFERRED,
                )

            OutcomeEvaluationStatus.FAILED ->
                OutcomeResult.create(
                    traceId = traceId,
                    status = OutcomeStatus.FAILED,
                    error = requireNotNull(evaluation.error),
                )
        }
    }
}
