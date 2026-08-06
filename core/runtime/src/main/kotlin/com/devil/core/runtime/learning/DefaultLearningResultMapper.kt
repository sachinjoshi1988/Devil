package com.devil.core.runtime.learning

import com.devil.core.model.common.TraceId

/**
 * Default Stage 17 mapping from bounded learning-evaluation results into the
 * stable LearningResult contract.
 *
 * Genuine constitutional learning evidence becomes operational LEARNABLE and
 * preserves the bounded LearningRequest. Evaluation unavailability becomes
 * DEFERRED. Evaluation failure preserves its matching error.
 *
 * This mapper does not create learning, create or commit memory, mutate world
 * state, change task or plan state, communicate externally, or bypass unified
 * runtime handling.
 */
class DefaultLearningResultMapper :
    LearningResultMapper {

    override fun map(
        traceId: TraceId,
        evaluation: LearningEvaluationResult,
    ): LearningResult {
        require(evaluation.traceId == traceId) {
            "Learning result mapper trace and evaluation result must use the same trace identity."
        }

        return when (evaluation.status) {
            LearningEvaluationStatus.LEARNABLE ->
                LearningResult.create(
                    traceId = traceId,
                    status = LearningStatus.LEARNABLE,
                    request = requireNotNull(evaluation.request),
                )

            LearningEvaluationStatus.UNAVAILABLE ->
                LearningResult.create(
                    traceId = traceId,
                    status = LearningStatus.DEFERRED,
                )

            LearningEvaluationStatus.FAILED ->
                LearningResult.create(
                    traceId = traceId,
                    status = LearningStatus.FAILED,
                    error = requireNotNull(evaluation.error),
                )
        }
    }
}
