package com.devil.core.runtime.executive

import com.devil.core.model.common.TraceId
import com.devil.core.model.executive.ExecutiveReadinessRequest

/**
 * Evaluates one bounded constitutional Executive-readiness request.
 *
 * An evaluator must not invent readiness policy or treat capability selection
 * as execution permission. It does not authorize execution, execute actions,
 * observe results, verify outcomes, or report final outcomes.
 */
interface ExecutiveReadinessEvaluator {

    fun evaluate(
        traceId: TraceId,
        request: ExecutiveReadinessRequest,
    ): ExecutiveReadinessEvaluationResult
}
