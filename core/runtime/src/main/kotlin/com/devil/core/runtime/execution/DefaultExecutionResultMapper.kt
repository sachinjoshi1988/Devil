package com.devil.core.runtime.execution

import com.devil.core.model.common.TraceId

/**
 * Default Stage 12 mapping from bounded execution-evaluation results into the
 * stable ExecutionResult contract.
 *
 * Evaluation approval becomes operational APPROVED and preserves the bounded
 * ExecutionRequest. This state permits a future execution implementation to be
 * approached, but does not claim that an action was attempted or completed.
 *
 * Evaluation unavailability becomes DEFERRED. Evaluation failure preserves its
 * matching error.
 *
 * This mapper performs no capability activation, platform execution,
 * observation, verification, or outcome reporting.
 */
class DefaultExecutionResultMapper : ExecutionResultMapper {

    override fun map(
        traceId: TraceId,
        evaluation: ExecutionEvaluationResult,
    ): ExecutionResult {
        require(evaluation.traceId == traceId) {
            "Execution result mapper trace and evaluation result must use the same trace identity."
        }

        return when (evaluation.status) {
            ExecutionEvaluationStatus.APPROVED ->
                ExecutionResult.create(
                    traceId = traceId,
                    status = ExecutionStatus.APPROVED,
                    request = requireNotNull(evaluation.request),
                )

            ExecutionEvaluationStatus.UNAVAILABLE ->
                ExecutionResult.create(
                    traceId = traceId,
                    status = ExecutionStatus.DEFERRED,
                )

            ExecutionEvaluationStatus.FAILED ->
                ExecutionResult.create(
                    traceId = traceId,
                    status = ExecutionStatus.FAILED,
                    error = requireNotNull(evaluation.error),
                )
        }
    }
}
