package com.devil.core.runtime.learning

import com.devil.core.model.common.TraceId
import com.devil.core.model.learning.LearningRequest

/**
 * Evaluates one bounded constitutional learning request.
 *
 * An evaluator must not create learning or memory without approved
 * constitutional policy and genuine supporting evidence. It must not mutate
 * world state, change task or plan state, communicate externally, or produce a
 * runtime result.
 */
interface LearningEvaluator {

    fun evaluate(
        traceId: TraceId,
        request: LearningRequest,
    ): LearningEvaluationResult
}
