package com.devil.core.runtime.outcome

import com.devil.core.model.common.TraceId
import com.devil.core.model.outcome.OutcomeRequest

/**
 * Evaluates one bounded constitutional outcome request.
 *
 * An evaluator must not infer final success or failure merely from verification,
 * update world state, change task or plan state, create memory or learning,
 * communicate an outcome, or produce the final runtime result.
 */
interface OutcomeEvaluator {

    fun evaluate(
        traceId: TraceId,
        request: OutcomeRequest,
    ): OutcomeEvaluationResult
}
