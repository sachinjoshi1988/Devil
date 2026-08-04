package com.devil.core.model.authorization

import com.devil.core.model.identity.IdentityId

/**
 * Records one bounded constitutional authorization assessment for a subject.
 *
 * The assessment preserves the evaluated subject, established state, and
 * concise rationale. It does not authenticate the subject, prove ownership,
 * establish trust, authorize a specific capability, grant operating-system
 * permission, enter Owner Mode, permit execution, or claim an outcome.
 */
@ConsistentCopyVisibility
data class AuthorizationAssessment private constructor(
    val subjectIdentityId: IdentityId,
    val state: AuthorizationEvaluationState,
    val rationale: String,
) {
    companion object {
        fun create(
            subjectIdentityId: IdentityId,
            state: AuthorizationEvaluationState,
            rationale: String,
        ): AuthorizationAssessment {
            val normalizedRationale = rationale.trim()

            require(normalizedRationale.isNotEmpty()) {
                "Authorization assessment rationale must not be blank."
            }

            return AuthorizationAssessment(
                subjectIdentityId = subjectIdentityId,
                state = state,
                rationale = normalizedRationale,
            )
        }
    }
}
