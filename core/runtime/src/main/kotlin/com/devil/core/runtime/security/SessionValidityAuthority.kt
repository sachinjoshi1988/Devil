package com.devil.core.runtime.security

import com.devil.core.model.security.SessionValidityRequest

/**
 * Coordinates one bounded constitutional session-validity evaluation.
 *
 * The Session Validity Authority delegates session-validity evaluation to one
 * SessionValidityEvaluator and maps that bounded evaluation through one
 * SessionValidityResultMapper.
 *
 * It does not mutate session state, extend or renew a session, authenticate a
 * subject, prove owner identity, establish trust, grant authorization, advance
 * SecurityStage, enter Owner Mode, approve high-security confirmation, grant
 * Android permission, or permit execution.
 *
 * VALID and INVALID results represent only the bounded session-validity
 * determination produced from the supplied request.
 */
interface SessionValidityAuthority {

    fun evaluateValidity(
        request: SessionValidityRequest,
    ): SessionValidityResult
}
