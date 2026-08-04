package com.devil.core.runtime.authorization

import com.devil.core.model.authorization.AuthorizationAssessment
import com.devil.core.model.authorization.AuthorizationEvaluationRequest

/**
 * Produces a bounded constitutional authorization assessment from a structured
 * authorization-evaluation request.
 *
 * This resolver does not resolve identity, evaluate trust, authorize a specific
 * capability, grant operating-system permission, enter Owner Mode, execute
 * actions, or verify outcomes.
 */
interface AuthorizationEvaluationResolver {

    fun evaluate(
        request: AuthorizationEvaluationRequest,
    ): AuthorizationAssessment
}
