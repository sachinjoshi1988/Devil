package com.devil.core.runtime.trust

import com.devil.core.model.trust.SubjectTrustLevel
import com.devil.core.model.trust.TrustAssessment
import com.devil.core.model.trust.TrustEvaluationRequest

/**
 * Default Stage 4 subject trust-evaluation resolver.
 *
 * No subject-trust policy is available yet. This implementation therefore
 * preserves the resolved subject identity and returns UNESTABLISHED rather than
 * copying ContextTrustLevel or inventing a trust conclusion.
 *
 * It performs no identity resolution, authentication, ownership determination,
 * authorization, planning, execution, observation, or verification.
 */
class DefaultTrustEvaluationResolver :
    TrustEvaluationResolver {

    override fun evaluate(
        request: TrustEvaluationRequest,
    ): TrustAssessment {
        return TrustAssessment.create(
            subjectIdentityId = request.subjectIdentityId,
            level = SubjectTrustLevel.UNESTABLISHED,
            rationale = "No subject trust evaluation policy is available.",
        )
    }
}
