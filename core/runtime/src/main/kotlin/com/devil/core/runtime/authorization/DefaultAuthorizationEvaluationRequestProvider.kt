package com.devil.core.runtime.authorization

import com.devil.core.model.authorization.AuthorizationEvaluationRequest
import com.devil.core.model.context.ContextEnvelope
import com.devil.core.runtime.identity.IdentityResult
import com.devil.core.runtime.identity.IdentityStatus
import com.devil.core.runtime.trust.TrustResult
import com.devil.core.runtime.trust.TrustStatus

/**
 * Default Stage 4 authorization-evaluation request provider.
 *
 * A request becomes available only when:
 *
 * - identity resolution produced one resolved subject identity; and
 * - trust evaluation produced one genuine bounded TrustAssessment.
 *
 * Subject trust is consumed exactly as established upstream. ContextTrustLevel is
 * not converted into subject trust and no trust or authorization conclusion is
 * fabricated here.
 *
 * Identity or trust failures propagate their matching error.
 *
 * This provider performs no authorization evaluation, capability authorization,
 * Owner Mode entry, execution, observation, or verification.
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

        if (
            identity.status != IdentityStatus.RESOLVED ||
            trust.status != TrustStatus.EVALUATED ||
            trust.assessment == null
        ) {
            return AuthorizationEvaluationRequestResult.create(
                traceId = context.traceId,
                status = AuthorizationEvaluationRequestStatus.UNAVAILABLE,
            )
        }

        val identityId =
            requireNotNull(identity.identityId)

        require(
            trust.assessment.subjectIdentityId == identityId,
        ) {
            "Resolved identity and trust assessment must use the same subject identity."
        }

        return AuthorizationEvaluationRequestResult.create(
            traceId = context.traceId,
            status = AuthorizationEvaluationRequestStatus.AVAILABLE,
            request =
                AuthorizationEvaluationRequest.create(
                    context = context,
                    subjectIdentityId = identityId,
                    trustAssessment = trust.assessment,
                ),
        )
    }
}
