package com.devil.core.model.authorization

import com.devil.core.model.context.ContextEnvelope
import com.devil.core.model.identity.IdentityId
import com.devil.core.model.trust.TrustAssessment

/**
 * Represents one structured request for constitutional authorization evaluation.
 *
 * The request binds constitutional context, a resolved subject identity, and a
 * bounded trust assessment. It does not grant authorization, authorize a
 * capability, grant operating-system permission, enter Owner Mode, permit
 * execution, or verify an outcome.
 */
@ConsistentCopyVisibility
data class AuthorizationEvaluationRequest private constructor(
    val context: ContextEnvelope,
    val subjectIdentityId: IdentityId,
    val trustAssessment: TrustAssessment,
) {
    companion object {
        fun create(
            context: ContextEnvelope,
            subjectIdentityId: IdentityId,
            trustAssessment: TrustAssessment,
        ): AuthorizationEvaluationRequest {
            require(
                trustAssessment.subjectIdentityId == subjectIdentityId,
            ) {
                "Authorization request subject and trust assessment must use the same identity."
            }

            return AuthorizationEvaluationRequest(
                context = context,
                subjectIdentityId = subjectIdentityId,
                trustAssessment = trustAssessment,
            )
        }
    }
}
