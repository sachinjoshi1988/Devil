package com.devil.core.runtime.executive

import com.devil.core.model.common.TraceId

/**
 * Default Stage 11 mapping from bounded Executive readiness evaluation results
 * into the stable ExecutiveReadinessResult contract.
 *
 * Affirmative readiness evidence is mapped as READY. Evaluation unavailability
 * becomes operational deferral. Evaluation failure preserves its matching
 * error.
 *
 * This mapper performs no readiness evaluation, authorization, capability
 * availability or health checks, operating-system permission checks,
 * execution, observation, verification, or outcome reporting.
 */
class DefaultExecutiveReadinessResultMapper :
    ExecutiveReadinessResultMapper {

    override fun map(
        traceId: TraceId,
        evaluation: ExecutiveReadinessEvaluationResult,
    ): ExecutiveReadinessResult {
        require(evaluation.traceId == traceId) {
            "Executive readiness mapper trace and evaluation result must use the same trace identity."
        }

        return when (evaluation.status) {
            ExecutiveReadinessEvaluationStatus.READY ->
                ExecutiveReadinessResult.create(
                    traceId = traceId,
                    status = ExecutiveReadinessStatus.READY,
                )

            ExecutiveReadinessEvaluationStatus.UNAVAILABLE ->
                ExecutiveReadinessResult.create(
                    traceId = traceId,
                    status = ExecutiveReadinessStatus.DEFERRED,
                )

            ExecutiveReadinessEvaluationStatus.FAILED ->
                ExecutiveReadinessResult.create(
                    traceId = traceId,
                    status = ExecutiveReadinessStatus.FAILED,
                    error = requireNotNull(evaluation.error),
                )
        }
    }
}
