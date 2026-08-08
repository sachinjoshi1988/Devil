package com.devil.core.runtime.security

import com.devil.core.model.common.TraceId
import com.devil.core.model.security.SessionState
import com.devil.core.model.security.SessionValidityRequest

/**
 * Default Stage 23 bounded session-validity evaluator.
 *
 * This evaluator applies only the explicit lifecycle and validity-window facts
 * already contained in SessionValidityRequest.
 *
 * A REVOKED or EXPIRED session is invalid.
 *
 * An ACTIVE session is valid only when the authoritative observation time is at
 * or after establishment and strictly before expiration.
 *
 * This evaluator does not mutate SessionRecord, extend or renew sessions,
 * authenticate a subject, advance SecurityStage, enter Owner Mode, approve
 * high-security confirmation, invoke Android credentials, or permit execution.
 */
class DefaultSessionValidityEvaluator : SessionValidityEvaluator {

    override fun evaluate(
        traceId: TraceId,
        request: SessionValidityRequest,
    ): SessionValidityEvaluationResult {
        require(request.context.traceId == traceId) {
            "Session validity evaluator trace and request must use the same trace identity."
        }

        val session = request.session
        val observedAt = request.observedAt.epochMilliseconds

        val status =
            when (session.state) {
                SessionState.EXPIRED,
                SessionState.REVOKED,
                -> SessionValidityEvaluationStatus.INVALID

                SessionState.ACTIVE -> {
                    val establishedAt =
                        session.establishedAt.epochMilliseconds
                    val expiresAt =
                        session.expiresAt.epochMilliseconds

                    if (
                        observedAt >= establishedAt &&
                        observedAt < expiresAt
                    ) {
                        SessionValidityEvaluationStatus.VALID
                    } else {
                        SessionValidityEvaluationStatus.INVALID
                    }
                }
            }

        return SessionValidityEvaluationResult.create(
            traceId = traceId,
            status = status,
            request = request,
        )
    }
}
