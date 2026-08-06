package com.devil.core.runtime.worldmodel

import com.devil.core.model.common.TraceId
import com.devil.core.model.worldmodel.WorldModelUpdateRequest

/**
 * Default Stage 16 constitutional World Model update evaluator.
 *
 * No approved constitutional World Model update policy or genuine World Model
 * mutation mechanism exists yet. This evaluator therefore preserves trace
 * continuity and returns UNAVAILABLE rather than treating an established
 * outcome as permission to mutate world state or claiming that state changed.
 *
 * It does not change task or plan state, create memory or learning,
 * communicate externally, or produce a runtime result.
 */
class DefaultWorldModelUpdateEvaluator :
    WorldModelUpdateEvaluator {

    override fun evaluate(
        traceId: TraceId,
        request: WorldModelUpdateRequest,
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
            "World Model update evaluator trace and request must use the same trace identity."
        }

        return WorldModelUpdateEvaluationResult.create(
            traceId = traceId,
            status = WorldModelUpdateEvaluationStatus.UNAVAILABLE,
        )
    }
}
