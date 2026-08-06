package com.devil.core.runtime.execution

import com.devil.core.model.common.TraceId
import com.devil.core.model.execution.ExecutionRequest

/**
 * Evaluates one bounded constitutional execution request.
 *
 * An evaluator must not invent execution policy or treat Executive readiness as
 * proof that a platform action may be performed. It does not activate
 * capabilities, execute actions, observe execution, verify outcomes, or report
 * final success.
 */
interface ExecutionEvaluator {

    fun evaluate(
        traceId: TraceId,
        request: ExecutionRequest,
    ): ExecutionEvaluationResult
}
