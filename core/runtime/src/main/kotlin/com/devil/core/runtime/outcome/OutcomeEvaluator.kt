package com.devil.core.runtime.outcome

import com.devil.core.model.common.TraceId
import com.devil.core.model.outcome.OutcomeRequest

/**
 * Evaluates one bounded constitutional outcome request against genuine
 * outcome-evidence state.
 *
 * An evaluator must not fabricate outcome evidence, infer final task or plan
 * completion, update world state, create memory or learning, communicate an
 * outcome, or produce the final runtime result.
 */
interface OutcomeEvaluator {

    fun evaluate(
        traceId: TraceId,
        request: OutcomeRequest,
        evidence: OutcomeEvidenceResult,
    ): OutcomeEvaluationResult
}
