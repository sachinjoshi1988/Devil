package com.devil.core.runtime.executive

import com.devil.core.model.common.TraceId

/**
 * Translates one bounded Executive readiness evaluation result into the stable
 * operational ExecutiveReadinessResult contract.
 *
 * This mapper does not evaluate readiness, authorize execution, check
 * capability availability or health, evaluate operating-system permission,
 * execute actions, observe results, verify outcomes, or report final outcomes.
 */
interface ExecutiveReadinessResultMapper {

    fun map(
        traceId: TraceId,
        evaluation: ExecutiveReadinessEvaluationResult,
    ): ExecutiveReadinessResult
}
