package com.devil.core.runtime.trust

import com.devil.core.model.context.ContextEnvelope
import com.devil.core.model.trust.TrustEvaluationRequest
import com.devil.core.runtime.identity.IdentityResult
import com.devil.core.runtime.identity.IdentityStatus

/**
 * Default Stage 4 trust-evaluation request provider.
 *
 * A request is available only when identity resolution produced one resolved
 * subject identity. Unresolved identity yields an unavailable request. Failed
 * identity resolution propagates its matching error without fabricating trust.
 *
 * This implementation performs no trust evaluation, ownership determination,
 * authorization, planning, execution, observation, or verification.
 */
class DefaultTrustEvaluationRequestProvider :
    TrustEvaluationRequestProvider {

    override fun provide(
        context: ContextEnvelope,
        identity: IdentityResult,
    ): TrustEvaluationRequestResult {
        require(identity.traceId == context.traceId) {
            "Context and identity result must use the same trace identity."
        }

        return when (identity.status) {
            IdentityStatus.RESOLVED ->
                TrustEvaluationRequestResult.create(
                    traceId = context.traceId,
                    status = TrustEvaluationRequestStatus.AVAILABLE,
                    request = TrustEvaluationRequest.create(
                        context = context,
                        subjectIdentityId = requireNotNull(identity.identityId),
                    ),
                )

            IdentityStatus.UNRESOLVED ->
                TrustEvaluationRequestResult.create(
                    traceId = context.traceId,
                    status = TrustEvaluationRequestStatus.UNAVAILABLE,
                )

            IdentityStatus.FAILED ->
                TrustEvaluationRequestResult.create(
                    traceId = context.traceId,
                    status = TrustEvaluationRequestStatus.FAILED,
                    error = requireNotNull(identity.error),
                )
        }
    }
}
