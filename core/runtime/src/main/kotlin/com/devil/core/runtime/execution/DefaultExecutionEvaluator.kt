package com.devil.core.runtime.execution

import com.devil.core.model.common.TraceId
import com.devil.core.model.execution.ExecutionRequest

/**
 * Default bounded constitutional execution evaluator.
 *
 * Stage 62 establishes a deliberately narrow execution-approval policy over one
 * structured ExecutionRequest that can exist only after:
 *
 * - a bounded PlanRecord was created;
 * - one registered capability was selected; and
 * - Executive readiness was affirmatively established.
 *
 * APPROVED means only that constitutional execution evaluation permits the
 * bounded request to approach a separate execution implementation.
 *
 * APPROVED does not:
 * - establish capability availability;
 * - establish capability health;
 * - establish operating-system permission;
 * - activate a capability;
 * - invoke Android or another platform API;
 * - attempt an action;
 * - claim an action completed;
 * - observe execution;
 * - verify an effect;
 * - establish an Outcome.
 *
 * Platform execution remains outside the core runtime evaluator. Genuine
 * execution evidence must still be established downstream before Observation,
 * Verification, or Outcome can make stronger claims.
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
            status = ExecutionEvaluationStatus.APPROVED,
            request = request,
        )
    }
}
