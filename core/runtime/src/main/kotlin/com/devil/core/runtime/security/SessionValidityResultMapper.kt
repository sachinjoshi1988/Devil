package com.devil.core.runtime.security

import com.devil.core.model.common.TraceId

/**
 * Translates one bounded SessionValidityEvaluationResult into the stable
 * operational SessionValidityResult contract.
 *
 * This mapper does not evaluate session policy, mutate session state, extend a
 * session, authenticate a subject, advance SecurityStage, enter Owner Mode,
 * approve high-security confirmation, grant Android permission, or permit
 * execution.
 */
interface SessionValidityResultMapper {

    fun map(
        traceId: TraceId,
        evaluation: SessionValidityEvaluationResult,
    ): SessionValidityResult
}
