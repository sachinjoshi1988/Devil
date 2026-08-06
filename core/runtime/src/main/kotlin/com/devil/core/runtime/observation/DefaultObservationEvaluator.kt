package com.devil.core.runtime.observation

import com.devil.core.model.common.TraceId
import com.devil.core.model.observation.ObservationRequest

/**
 * Default Stage 13 constitutional observation evaluator.
 *
 * No genuine execution observation source or constitutional observation policy
 * exists yet. This evaluator therefore preserves trace continuity and returns
 * UNAVAILABLE rather than claiming that execution occurred or inventing
 * observation evidence.
 *
 * It does not verify outcomes, report success, update world state, or produce a
 * final outcome.
 */
class DefaultObservationEvaluator : ObservationEvaluator {

    override fun evaluate(
        traceId: TraceId,
        request: ObservationRequest,
    ): ObservationEvaluationResult {
        require(
            request.execution.plan.task.decision.understanding.context.traceId ==
                traceId,
        ) {
            "Observation evaluator trace and request must use the same trace identity."
        }

        return ObservationEvaluationResult.create(
            traceId = traceId,
            status = ObservationEvaluationStatus.UNAVAILABLE,
        )
    }
}
