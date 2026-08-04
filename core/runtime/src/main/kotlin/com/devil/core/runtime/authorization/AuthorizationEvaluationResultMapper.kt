package com.devil.core.runtime.authorization

import com.devil.core.model.authorization.AuthorizationAssessment
import com.devil.core.model.common.TraceId

/**
 * Translates a bounded authorization assessment into the stable runtime
 * authorization result contract.
 *
 * This mapper does not evaluate authorization, authorize a capability, grant
 * operating-system permission, enter Owner Mode, permit execution, or verify
 * an outcome.
 */
interface AuthorizationEvaluationResultMapper {

    fun map(
        traceId: TraceId,
        assessment: AuthorizationAssessment,
    ): AuthorizationResult
}
