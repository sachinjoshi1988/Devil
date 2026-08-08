package com.devil.core.runtime.security

import com.devil.core.model.common.TraceId
import com.devil.core.model.security.SessionValidityRequest

/**
 * Evaluates the current validity of one bounded constitutional security session.
 *
 * An evaluator may determine only whether the supplied session is valid at the
 * authoritative observation time represented by SessionValidityRequest.
 *
 * It must not mutate session state, extend a session, create a session,
 * authenticate a subject, prove owner identity, establish trust, grant
 * authorization, advance SecurityStage, enter Owner Mode, approve
 * high-security confirmation, grant Android permission, or permit execution.
 */
interface SessionValidityEvaluator {

    fun evaluate(
        traceId: TraceId,
        request: SessionValidityRequest,
    ): SessionValidityEvaluationResult
}
