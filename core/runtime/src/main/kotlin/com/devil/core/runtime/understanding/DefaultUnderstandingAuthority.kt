package com.devil.core.runtime.understanding

import com.devil.core.model.context.ContextEnvelope
import com.devil.core.runtime.authorization.AuthorizationResult
import com.devil.core.runtime.conversation.ConversationIntakeAuthorityResult
import com.devil.core.runtime.identity.IdentityResult
import com.devil.core.runtime.trust.TrustResult

/**
 * Default Stage 6 structured-understanding authority coordinator.
 *
 * This authority obtains a bounded understanding-evaluation request, delegates
 * evaluation to the resolver, and maps the resulting UnderstandingRecord into
 * the stable operational result contract.
 *
 * It does not resolve identity, evaluate trust, grant authorization, perform
 * conversation intake, create memory, select decisions, create tasks, plan
 * work, authorize capabilities, execute actions, observe results, or verify
 * outcomes.
 */
class DefaultUnderstandingAuthority(
    private val requestProvider:
        UnderstandingEvaluationRequestProvider =
        DefaultUnderstandingEvaluationRequestProvider(),
    private val resolver: UnderstandingEvaluationResolver =
        DefaultUnderstandingEvaluationResolver(),
    private val resultMapper:
        UnderstandingEvaluationResultMapper =
        DefaultUnderstandingEvaluationResultMapper(),
) : UnderstandingAuthority {

    override fun understand(
        context: ContextEnvelope,
        identity: IdentityResult,
        trust: TrustResult,
        authorization: AuthorizationResult,
        conversationIntake: ConversationIntakeAuthorityResult,
    ): UnderstandingAuthorityResult {
        require(identity.traceId == context.traceId) {
            "Context and identity result must use the same trace identity."
        }

        require(trust.traceId == context.traceId) {
            "Context and trust result must use the same trace identity."
        }

        require(authorization.traceId == context.traceId) {
            "Context and authorization result must use the same trace identity."
        }

        require(conversationIntake.traceId == context.traceId) {
            "Context and conversation-intake result must use the same trace identity."
        }

        val requestResult =
            requestProvider.provide(conversationIntake)

        require(requestResult.traceId == context.traceId) {
            "Context and understanding-evaluation request result must use the same trace identity."
        }

        return when (requestResult.status) {
            UnderstandingEvaluationRequestStatus.AVAILABLE -> {
                val request =
                    requireNotNull(requestResult.request)
                val understanding =
                    resolver.evaluate(request)
                val result = resultMapper.map(
                    traceId = context.traceId,
                    understanding = understanding,
                )

                require(result.traceId == context.traceId) {
                    "Context and mapped understanding result must use the same trace identity."
                }

                result
            }

            UnderstandingEvaluationRequestStatus.UNAVAILABLE ->
                UnderstandingAuthorityResult.create(
                    traceId = context.traceId,
                    status =
                        UnderstandingAuthorityStatus.DEFERRED,
                )

            UnderstandingEvaluationRequestStatus.FAILED ->
                UnderstandingAuthorityResult.create(
                    traceId = context.traceId,
                    status =
                        UnderstandingAuthorityStatus.FAILED,
                    error = requireNotNull(
                        requestResult.error,
                    ),
                )
        }
    }
}
