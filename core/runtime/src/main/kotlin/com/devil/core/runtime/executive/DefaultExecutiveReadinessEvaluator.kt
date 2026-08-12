package com.devil.core.runtime.executive

import com.devil.core.model.common.TraceId
import com.devil.core.model.executive.ExecutiveReadinessRequest

/**
 * Default bounded constitutional Executive readiness evaluator.
 *
 * Stage 61 establishes a deliberately narrow readiness policy over one
 * structured ExecutiveReadinessRequest produced only after a PlanRecord exists
 * and one registered capability has been selected.
 *
 * READY means only that the bounded constitutional pipeline is ready to
 * approach the separate Execution Authority.
 *
 * READY does not:
 * - establish capability availability;
 * - establish capability health;
 * - establish operating-system permission;
 * - authorize execution;
 * - activate a capability;
 * - execute an action;
 * - observe execution;
 * - verify an effect;
 * - establish an Outcome.
 *
 * The downstream Execution Authority remains independently responsible for its
 * own constitutional evaluation.
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
            status = ExecutiveReadinessEvaluationStatus.READY,
            request = request,
        )
    }
}
