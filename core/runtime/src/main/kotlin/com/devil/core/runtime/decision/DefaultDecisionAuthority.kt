package com.devil.core.runtime.decision

import com.devil.core.model.context.ContextEnvelope
import com.devil.core.runtime.authorization.AuthorizationResult
import com.devil.core.runtime.identity.IdentityResult
import com.devil.core.runtime.trust.TrustResult
import com.devil.core.runtime.understanding.UnderstandingAuthorityResult

/**
 * Default Stage 7 constitutional Decision Authority coordinator.
 *
 * This authority obtains a bounded decision-evaluation request, delegates
 * evaluation to the resolver, and maps the resulting DecisionRecord into the
 * stable operational result contract.
 *
 * It does not resolve identity, evaluate trust, grant authorization, produce
 * understanding, create memory, create tasks, plan work, authorize
 * capabilities, execute actions, observe results, or verify outcomes.
 */
class DefaultDecisionAuthority(
    private val requestProvider:
        DecisionEvaluationRequestProvider =
        DefaultDecisionEvaluationRequestProvider(),
    private val resolver: DecisionEvaluationResolver =
        DefaultDecisionEvaluationResolver(),
    private val resultMapper:
        DecisionEvaluationResultMapper =
        DefaultDecisionEvaluationResultMapper(),
) : DecisionAuthority {

    override fun decide(
        context: ContextEnvelope,
        identity: IdentityResult,
        trust: TrustResult,
        authorization: AuthorizationResult,
        understanding: UnderstandingAuthorityResult,
    ): DecisionAuthorityResult {
        require(identity.traceId == context.traceId) {
            "Context and identity result must use the same trace identity."
        }

        require(trust.traceId == context.traceId) {
            "Context and trust result must use the same trace identity."
        }

        require(authorization.traceId == context.traceId) {
            "Context and authorization result must use the same trace identity."
        }

        require(understanding.traceId == context.traceId) {
            "Context and understanding result must use the same trace identity."
        }

        val requestResult =
            requestProvider.provide(understanding)

        require(requestResult.traceId == context.traceId) {
            "Context and decision-evaluation request result must use the same trace identity."
        }

        return when (requestResult.status) {
            DecisionEvaluationRequestStatus.AVAILABLE -> {
                val request =
                    requireNotNull(requestResult.request)

                val decision =
                    resolver.evaluate(request)

                val result = resultMapper.map(
                    traceId = context.traceId,
                    decision = decision,
                )

                require(result.traceId == context.traceId) {
                    "Context and mapped decision result must use the same trace identity."
                }

                result
            }

            DecisionEvaluationRequestStatus.UNAVAILABLE ->
                DecisionAuthorityResult.create(
                    traceId = context.traceId,
                    status = DecisionAuthorityStatus.DEFERRED,
                )

            DecisionEvaluationRequestStatus.FAILED ->
                DecisionAuthorityResult.create(
                    traceId = context.traceId,
                    status = DecisionAuthorityStatus.FAILED,
                    error = requireNotNull(
                        requestResult.error,
                    ),
                )
        }
    }
}
