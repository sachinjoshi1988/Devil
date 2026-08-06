package com.devil.core.runtime.learning

import com.devil.core.model.common.TraceId

/**
 * Translates one bounded constitutional learning-evaluation result into the
 * stable operational LearningResult contract.
 *
 * This mapper does not create learning evidence, create or commit memory,
 * mutate world state, change task or plan state, communicate externally, or
 * bypass unified runtime handling.
 */
interface LearningResultMapper {

    fun map(
        traceId: TraceId,
        evaluation: LearningEvaluationResult,
    ): LearningResult
}
