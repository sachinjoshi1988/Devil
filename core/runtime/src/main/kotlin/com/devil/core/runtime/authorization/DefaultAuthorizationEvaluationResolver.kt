package com.devil.core.runtime.authorization

import com.devil.core.model.authorization.AuthorizationAssessment
import com.devil.core.model.authorization.AuthorizationEvaluationRequest
import com.devil.core.model.authorization.AuthorizationEvaluationState

/**
 * Default Stage 4 constitutional authorization-evaluation resolver.
 *
 * No constitutional authorization policy is available yet. This implementation
 * therefore preserves the evaluated subject identity and returns DEFERRED rather
 * than granting or denying continuation without policy evidence.
 *
 * It performs no identity resolution, trust evaluation, capability
 * authorization, Owner Mode entry, execution, observation, or verification.
 */
class DefaultAuthorizationEvaluationResolver :
    AuthorizationEvaluationResolver {

    override fun evaluate(
        request: AuthorizationEvaluationRequest,
    ): AuthorizationAssessment {
        return AuthorizationAssessment.create(
            subjectIdentityId = request.subjectIdentityId,
            state = AuthorizationEvaluationState.DEFERRED,
            rationale = "No constitutional authorization policy is available.",
        )
    }
}
