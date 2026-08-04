package com.devil.core.runtime.trust

import com.devil.core.model.context.ContextEnvelope
import com.devil.core.runtime.identity.IdentityResult

/**
 * Default Stage 4 trust authority coordinator.
 *
 * This implementation obtains a structured trust-evaluation request, delegates
 * bounded subject trust evaluation to the resolver, and maps the assessment into
 * the stable runtime TrustResult contract.
 *
 * It does not resolve identity, authenticate a subject, prove ownership, grant
 * authorization, enter Owner Mode, plan, execute, observe, or verify outcomes.
 */
class DefaultTrustAuthority(
    private val requestProvider: TrustEvaluationRequestProvider =
        DefaultTrustEvaluationRequestProvider(),
    private val resolver: TrustEvaluationResolver =
        DefaultTrustEvaluationResolver(),
    private val resultMapper: TrustEvaluationResultMapper =
        DefaultTrustEvaluationResultMapper(),
) : TrustAuthority {

    override fun evaluate(
        context: ContextEnvelope,
        identity: IdentityResult,
    ): TrustResult {
        val requestResult = requestProvider.provide(
            context = context,
            identity = identity,
        )

        require(requestResult.traceId == context.traceId) {
            "Context and trust-evaluation request result must use the same trace identity."
        }

        return when (requestResult.status) {
            TrustEvaluationRequestStatus.AVAILABLE -> {
                val request = requireNotNull(requestResult.request)
                val assessment = resolver.evaluate(request)
                val result = resultMapper.map(
                    traceId = context.traceId,
                    assessment = assessment,
                )

                require(result.traceId == context.traceId) {
                    "Context and mapped trust result must use the same trace identity."
                }

                result
            }

            TrustEvaluationRequestStatus.UNAVAILABLE ->
                TrustResult.create(
                    traceId = context.traceId,
                    status = TrustStatus.DEFERRED,
                )

            TrustEvaluationRequestStatus.FAILED ->
                TrustResult.create(
                    traceId = context.traceId,
                    status = TrustStatus.FAILED,
                    error = requireNotNull(requestResult.error),
                )
        }
    }
}
