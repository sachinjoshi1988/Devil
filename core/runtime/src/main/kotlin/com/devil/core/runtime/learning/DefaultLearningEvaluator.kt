package com.devil.core.runtime.learning

import com.devil.core.model.common.TraceId
import com.devil.core.model.learning.LearningRequest

/**
 * Default Stage 17 constitutional learning evaluator.
 *
 * No approved constitutional learning policy, learning-evidence source, or
 * controlled learning mechanism exists yet. This evaluator therefore preserves
 * trace continuity and returns UNAVAILABLE rather than treating an applicable
 * World Model update as proof that learning should occur.
 *
 * It does not create learning, create or commit memory, mutate world state,
 * change task or plan state, communicate externally, or produce a runtime
 * result.
 */
class DefaultLearningEvaluator :
    LearningEvaluator {

    override fun evaluate(
        traceId: TraceId,
        request: LearningRequest,
    ): LearningEvaluationResult {
        require(
            request.worldModelUpdate
                .outcome
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
            "Learning evaluator trace and request must use the same trace identity."
        }

        return LearningEvaluationResult.create(
            traceId = traceId,
            status = LearningEvaluationStatus.UNAVAILABLE,
        )
    }
}
