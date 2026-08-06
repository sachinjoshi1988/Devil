package com.devil.core.runtime.outcome

import com.devil.core.model.common.TraceId
import com.devil.core.model.outcome.OutcomeRequest

/**
 * Default Stage 15 constitutional outcome evaluator.
 *
 * No approved constitutional outcome policy or genuine outcome-determination
 * source exists yet. This evaluator therefore preserves trace continuity and
 * returns UNAVAILABLE rather than treating verification as proof of final task
 * success or failure or inventing an outcome.
 *
 * It does not update world state, change task or plan state, create memory or
 * learning, communicate an outcome, or produce the final runtime result.
 */
class DefaultOutcomeEvaluator : OutcomeEvaluator {

    override fun evaluate(
        traceId: TraceId,
        request: OutcomeRequest,
    ): OutcomeEvaluationResult {
        require(
            request.verification
                .observation
                .execution
                .plan
                .task
                .decision
                .understanding
                .context
                .traceId == traceId,
        ) {
            "Outcome evaluator trace and request must use the same trace identity."
        }

        return OutcomeEvaluationResult.create(
            traceId = traceId,
            status = OutcomeEvaluationStatus.UNAVAILABLE,
        )
    }
}
