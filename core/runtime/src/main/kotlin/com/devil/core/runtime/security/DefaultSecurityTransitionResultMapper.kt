package com.devil.core.runtime.security

import com.devil.core.model.common.TraceId

/**
 * Default Stage 23 mapping from bounded constitutional security-transition
 * evaluation into the stable SecurityTransitionResult contract.
 *
 * Genuine constitutional transition eligibility becomes operational APPROVED and
 * preserves one bounded SecurityTransitionRequest.
 *
 * Evaluation unavailability becomes DEFERRED.
 *
 * Evaluation failure preserves its matching error.
 *
 * This mapper does not advance security state, authenticate a subject, prove owner
 * identity, establish trust, grant authorization, create or validate a session,
 * enter Owner Mode, approve high-security confirmation, grant Android permission,
 * or permit execution.
 */
class DefaultSecurityTransitionResultMapper :
    SecurityTransitionResultMapper {

    override fun map(
        traceId: TraceId,
        evaluation: SecurityTransitionEvaluationResult,
    ): SecurityTransitionResult {
        require(evaluation.traceId == traceId) {
            "Security transition result mapper trace and evaluation result must use the same trace identity."
        }

        return when (evaluation.status) {
            SecurityTransitionEvaluationStatus.APPROVED ->
                SecurityTransitionResult.create(
                    traceId = traceId,
                    status = SecurityTransitionStatus.APPROVED,
                    request = requireNotNull(evaluation.request),
                )

            SecurityTransitionEvaluationStatus.UNAVAILABLE ->
                SecurityTransitionResult.create(
                    traceId = traceId,
                    status = SecurityTransitionStatus.DEFERRED,
                )

            SecurityTransitionEvaluationStatus.FAILED ->
                SecurityTransitionResult.create(
                    traceId = traceId,
                    status = SecurityTransitionStatus.FAILED,
                    error = requireNotNull(evaluation.error),
                )
        }
    }
}
