package com.devil.core.runtime.authorization

import com.devil.core.model.context.ContextEnvelope
import com.devil.core.runtime.identity.IdentityResult
import com.devil.core.runtime.trust.TrustResult

/**
 * Default Stage 4 authorization authority coordinator.
 *
 * This implementation obtains a structured authorization-evaluation request,
 * delegates bounded constitutional authorization evaluation to the resolver,
 * and maps the assessment into the stable AuthorizationResult contract.
 *
 * It does not resolve identity, evaluate trust, authorize an individual
 * capability, grant operating-system permission, enter Owner Mode, execute
 * actions, observe results, or verify outcomes.
 */
class DefaultAuthorizationAuthority(
    private val requestProvider: AuthorizationEvaluationRequestProvider =
        DefaultAuthorizationEvaluationRequestProvider(),
    private val resolver: AuthorizationEvaluationResolver =
        DefaultAuthorizationEvaluationResolver(),
    private val resultMapper: AuthorizationEvaluationResultMapper =
        DefaultAuthorizationEvaluationResultMapper(),
) : AuthorizationAuthority {

    override fun authorize(
        context: ContextEnvelope,
        identity: IdentityResult,
        trust: TrustResult,
    ): AuthorizationResult {
        val requestResult = requestProvider.provide(
            context = context,
            identity = identity,
            trust = trust,
        )

        require(requestResult.traceId == context.traceId) {
            "Context and authorization-evaluation request result must use the same trace identity."
        }

        return when (requestResult.status) {
            AuthorizationEvaluationRequestStatus.AVAILABLE -> {
                val request = requireNotNull(requestResult.request)
                val assessment = resolver.evaluate(request)
                val result = resultMapper.map(
                    traceId = context.traceId,
                    assessment = assessment,
                )

                require(result.traceId == context.traceId) {
                    "Context and mapped authorization result must use the same trace identity."
                }

                result
            }

            AuthorizationEvaluationRequestStatus.UNAVAILABLE ->
                AuthorizationResult.create(
                    traceId = context.traceId,
                    status = AuthorizationStatus.DEFERRED,
                )

            AuthorizationEvaluationRequestStatus.FAILED ->
                AuthorizationResult.create(
                    traceId = context.traceId,
                    status = AuthorizationStatus.FAILED,
                    error = requireNotNull(requestResult.error),
                )
        }
    }
}
