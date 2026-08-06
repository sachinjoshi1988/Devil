package com.devil.core.runtime.execution

import com.devil.core.model.common.TraceId

/**
 * Translates one bounded constitutional execution-evaluation result into the
 * stable operational ExecutionResult contract.
 *
 * This mapper does not activate capabilities, perform platform actions, create
 * execution attempts, observe execution, verify outcomes, or report final
 * success.
 */
interface ExecutionResultMapper {

    fun map(
        traceId: TraceId,
        evaluation: ExecutionEvaluationResult,
    ): ExecutionResult
}
