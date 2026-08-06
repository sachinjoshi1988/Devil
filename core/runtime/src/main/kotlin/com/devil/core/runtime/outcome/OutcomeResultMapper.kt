package com.devil.core.runtime.outcome

import com.devil.core.model.common.TraceId

/**
 * Translates one bounded constitutional outcome-evaluation result into the
 * stable operational OutcomeResult contract.
 *
 * This mapper does not create outcome evidence, update world state, change task
 * or plan state, create memory or learning, communicate externally, or bypass
 * unified runtime handling.
 */
interface OutcomeResultMapper {

    fun map(
        traceId: TraceId,
        evaluation: OutcomeEvaluationResult,
    ): OutcomeResult
}
