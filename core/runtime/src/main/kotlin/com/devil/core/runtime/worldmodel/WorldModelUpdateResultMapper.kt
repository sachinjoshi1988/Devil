package com.devil.core.runtime.worldmodel

import com.devil.core.model.common.TraceId

/**
 * Translates one bounded constitutional World Model update-evaluation result
 * into the stable operational WorldModelUpdateResult contract.
 *
 * This mapper does not create update evidence, mutate world state, claim that
 * state changed, change task or plan state, create memory or learning,
 * communicate externally, or bypass unified runtime handling.
 */
interface WorldModelUpdateResultMapper {

    fun map(
        traceId: TraceId,
        evaluation: WorldModelUpdateEvaluationResult,
    ): WorldModelUpdateResult
}
