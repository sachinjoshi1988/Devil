package com.devil.core.runtime.observation

import com.devil.core.model.common.TraceId
import com.devil.core.model.observation.ObservationRequest

/**
 * Evaluates one bounded constitutional observation request against genuine
 * observation-evidence state.
 *
 * An evaluator must not fabricate an execution attempt, invent observation
 * evidence, verify outcomes, report success, update world state, or produce a
 * final outcome.
 */
interface ObservationEvaluator {

    fun evaluate(
        traceId: TraceId,
        request: ObservationRequest,
        evidence: ObservationEvidenceResult,
    ): ObservationEvaluationResult
}
