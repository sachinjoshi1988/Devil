package com.devil.core.runtime.security

import com.devil.core.model.common.TraceId

/**
 * Translates one bounded SecurityTransitionEvaluationResult into the stable
 * operational SecurityTransitionResult contract.
 *
 * This mapper does not advance security state, authenticate a subject, prove owner
 * identity, establish trust, grant authorization, create or validate a session,
 * enter Owner Mode, approve high-security confirmation, grant Android permission,
 * or permit execution.
 */
interface SecurityTransitionResultMapper {

    fun map(
        traceId: TraceId,
        evaluation: SecurityTransitionEvaluationResult,
    ): SecurityTransitionResult
}
