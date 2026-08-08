package com.devil.core.runtime.security

import com.devil.core.model.common.TraceId
import com.devil.core.model.security.SecurityTransitionRequest

/**
 * Default Stage 23 constitutional security-transition evaluator.
 *
 * No approved constitutional security-transition policy, authentication
 * mechanism, session mechanism, Owner Mode policy, high-security confirmation
 * mechanism, or Android credential integration exists yet.
 *
 * This evaluator therefore preserves trace continuity and returns UNAVAILABLE
 * rather than treating a SecurityTransitionRequest as permission to advance
 * security state.
 *
 * It does not authenticate a subject, prove owner identity, establish trust,
 * grant authorization, create or validate a session, enter Owner Mode, approve
 * high-security confirmation, grant Android permission, or permit execution.
 */
class DefaultSecurityTransitionEvaluator :
    SecurityTransitionEvaluator {

    override fun evaluate(
        traceId: TraceId,
        request: SecurityTransitionRequest,
    ): SecurityTransitionEvaluationResult {
        require(request.context.traceId == traceId) {
            "Security transition evaluator trace and request must use the same trace identity."
        }

        return SecurityTransitionEvaluationResult.create(
            traceId = traceId,
            status =
                SecurityTransitionEvaluationStatus.UNAVAILABLE,
        )
    }
}
