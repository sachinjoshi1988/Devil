package com.devil.core.runtime.security

import com.devil.core.model.common.TraceId

/**
 * Default Stage 23 mapping from bounded session-validity evaluation into the
 * stable SessionValidityResult contract.
 *
 * VALID and INVALID evaluation results preserve the bounded
 * SessionValidityRequest.
 *
 * Evaluation unavailability becomes DEFERRED.
 *
 * Evaluation failure preserves its matching error.
 *
 * This mapper performs no session mutation, renewal, authentication, security
 * transition, Owner Mode entry, Android credential access, or execution.
 */
class DefaultSessionValidityResultMapper :
    SessionValidityResultMapper {

    override fun map(
        traceId: TraceId,
        evaluation: SessionValidityEvaluationResult,
    ): SessionValidityResult {
        require(evaluation.traceId == traceId) {
            "Session validity result mapper trace and evaluation result must use the same trace identity."
        }

        return when (evaluation.status) {
            SessionValidityEvaluationStatus.VALID ->
                SessionValidityResult.create(
                    traceId = traceId,
                    status = SessionValidityStatus.VALID,
                    request = requireNotNull(evaluation.request),
                )

            SessionValidityEvaluationStatus.INVALID ->
                SessionValidityResult.create(
                    traceId = traceId,
                    status = SessionValidityStatus.INVALID,
                    request = requireNotNull(evaluation.request),
                )

            SessionValidityEvaluationStatus.UNAVAILABLE ->
                SessionValidityResult.create(
                    traceId = traceId,
                    status = SessionValidityStatus.DEFERRED,
                )

            SessionValidityEvaluationStatus.FAILED ->
                SessionValidityResult.create(
                    traceId = traceId,
                    status = SessionValidityStatus.FAILED,
                    error = requireNotNull(evaluation.error),
                )
        }
    }
}
