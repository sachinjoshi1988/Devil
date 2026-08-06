package com.devil.core.runtime.observation

import com.devil.core.model.common.TraceId

/**
 * Translates one bounded constitutional observation-evaluation result into the
 * stable operational ObservationResult contract.
 *
 * This mapper does not create observation evidence, verify outcomes, update
 * world state, report success, or produce a final outcome.
 */
interface ObservationResultMapper {

    fun map(
        traceId: TraceId,
        evaluation: ObservationEvaluationResult,
    ): ObservationResult
}
