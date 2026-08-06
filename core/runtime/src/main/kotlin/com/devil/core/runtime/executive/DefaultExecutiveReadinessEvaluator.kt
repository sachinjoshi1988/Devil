package com.devil.core.runtime.executive

import com.devil.core.model.common.TraceId
import com.devil.core.model.executive.ExecutiveReadinessRequest

/**
 * Default Stage 11 constitutional Executive readiness evaluator.
 *
 * No constitutional Executive-readiness policy exists yet. This evaluator
 * therefore preserves trace continuity and returns UNAVAILABLE rather than
 * claiming that a selected capability is ready to approach execution.
 *
 * It does not authorize execution, evaluate operating-system permission,
 * establish capability availability or health, execute actions, observe
 * results, verify outcomes, or report final outcomes.
 */
class DefaultExecutiveReadinessEvaluator :
    ExecutiveReadinessEvaluator {

    override fun evaluate(
        traceId: TraceId,
        request: ExecutiveReadinessRequest,
    ): ExecutiveReadinessEvaluationResult {
        require(
            request.plan.task.decision.understanding.context.traceId ==
                traceId,
        ) {
            "Executive readiness evaluator trace and request must use the same trace identity."
        }

        return ExecutiveReadinessEvaluationResult.create(
            traceId = traceId,
            status = ExecutiveReadinessEvaluationStatus.UNAVAILABLE,
        )
    }
}
