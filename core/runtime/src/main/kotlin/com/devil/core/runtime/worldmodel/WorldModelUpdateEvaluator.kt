package com.devil.core.runtime.worldmodel

import com.devil.core.model.common.TraceId
import com.devil.core.model.worldmodel.WorldModelUpdateRequest

/**
 * Evaluates one bounded constitutional World Model update request.
 *
 * An evaluator must not mutate world state without approved constitutional
 * policy and genuine update evidence. It must not claim that world state
 * changed, change task or plan state, create memory or learning, communicate
 * externally, or produce a runtime result.
 */
interface WorldModelUpdateEvaluator {

    fun evaluate(
        traceId: TraceId,
        request: WorldModelUpdateRequest,
    ): WorldModelUpdateEvaluationResult
}
