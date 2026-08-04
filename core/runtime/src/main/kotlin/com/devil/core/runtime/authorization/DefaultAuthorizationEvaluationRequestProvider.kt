package com.devil.core.runtime.authorization

import com.devil.core.model.context.ContextEnvelope
import com.devil.core.runtime.identity.IdentityResult
import com.devil.core.runtime.identity.IdentityStatus
import com.devil.core.runtime.trust.TrustResult
import com.devil.core.runtime.trust.TrustStatus

/**
 * Default Stage 4 authorization-evaluation request provider.
 *
 * The stable TrustResult contract does not yet expose the bounded
 * TrustAssessment required by AuthorizationEvaluationRequest. This provider
 * therefore returns UNAVAILABLE rather than reconstructing or fabricating
 * subject trust from ContextTrustLevel.
 *
 * Identity or trust failures propagate their matching error. This provider
 * performs no authorization evaluation, capability authorization, Owner Mode
 * entry, execution, observation, or verification.
 */
class DefaultAuthorizationEvaluationRequestProvider :
    AuthorizationEvaluationRequestProvider {

    override fun provide(
        context: ContextEnvelope,
        identity: IdentityResult,
        trust: TrustResult,
    ): AuthorizationEvaluationRequestResult {
        require(identity.traceId == context.traceId) {
            "Context and identity result must use the same trace identity."
        }

        require(trust.traceId == context.traceId) {
            "Context and trust result must use the same trace identity."
        }

        if (identity.status == IdentityStatus.FAILED) {
            return AuthorizationEvaluationRequestResult.create(
                traceId = context.traceId,
                status = AuthorizationEvaluationRequestStatus.FAILED,
                error = requireNotNull(identity.error),
            )
        }

        if (trust.status == TrustStatus.FAILED) {
            return AuthorizationEvaluationRequestResult.create(
                traceId = context.traceId,
                status = AuthorizationEvaluationRequestStatus.FAILED,
                error = requireNotNull(trust.error),
            )
        }

        return AuthorizationEvaluationRequestResult.create(
            traceId = context.traceId,
            status = AuthorizationEvaluationRequestStatus.UNAVAILABLE,
        )
    }
}
