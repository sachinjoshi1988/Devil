package com.devil.core.runtime.execution

import com.devil.core.model.common.TraceId
import com.devil.core.model.execution.ExecutionRequest

/**
 * Default Stage 12 constitutional execution evaluator.
 *
 * No constitutional execution policy or platform execution implementation is
 * available yet. This evaluator therefore preserves trace continuity and
 * returns UNAVAILABLE rather than treating Executive readiness as permission to
 * perform an action or fabricating an execution attempt.
 *
 * It does not establish capability health, check operating-system permission,
 * activate capabilities, execute actions, observe execution, verify outcomes,
 * or report final success.
 */
class DefaultExecutionEvaluator : ExecutionEvaluator {

    override fun evaluate(
        traceId: TraceId,
        request: ExecutionRequest,
    ): ExecutionEvaluationResult {
        require(
            request.plan.task.decision.understanding.context.traceId ==
                traceId,
        ) {
            "Execution evaluator trace and request must use the same trace identity."
        }

        return ExecutionEvaluationResult.create(
            traceId = traceId,
            status = ExecutionEvaluationStatus.UNAVAILABLE,
        )
    }
}
