package com.devil.core.runtime.security

import com.devil.core.model.common.TraceId
import com.devil.core.model.security.SecurityTransitionRequest

/**
 * Evaluates one bounded constitutional security-stage transition request.
 *
 * An evaluator must not invent security-transition policy or treat a requested
 * stage as proof that the transition is valid, authenticated, authorized,
 * session-safe, owner-approved, or permitted.
 *
 * It does not authenticate a subject, prove owner identity, establish trust,
 * grant authorization, create or validate a session, enter Owner Mode, approve
 * high-security confirmation, grant Android permission, or permit execution.
 */
interface SecurityTransitionEvaluator {

    fun evaluate(
        traceId: TraceId,
        request: SecurityTransitionRequest,
    ): SecurityTransitionEvaluationResult
}
