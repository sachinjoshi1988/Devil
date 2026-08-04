package com.devil.core.runtime.identity

import com.devil.core.model.context.ContextEnvelope

/**
 * Default Stage 3 identity authority coordinator.
 *
 * This implementation obtains a genuine identity-resolution request when one is
 * available, delegates identity resolution to the bounded resolver, and maps
 * the established record into the stable runtime identity result contract.
 *
 * When no genuine identity evidence is available, it returns an honest
 * unresolved result. It performs no authentication, ownership determination,
 * trust evaluation, authorization, planning, or execution.
 */
class DefaultIdentityAuthority(
    private val requestProvider: IdentityResolutionRequestProvider =
        DefaultIdentityResolutionRequestProvider(),
    private val resolver: IdentityResolutionResolver =
        DefaultIdentityResolutionResolver(),
    private val resultMapper: IdentityResolutionResultMapper =
        DefaultIdentityResolutionResultMapper(),
) : IdentityAuthority {

    override fun resolve(
        context: ContextEnvelope,
    ): IdentityResult {
        val requestResult = requestProvider.provide(context)

        require(requestResult.traceId == context.traceId) {
            "Context and identity resolution request result must use the same trace identity."
        }

        return when (requestResult.status) {
            IdentityResolutionRequestStatus.AVAILABLE -> {
                val request = requireNotNull(requestResult.request)
                val record = resolver.resolve(request)
                val result = resultMapper.map(
                    traceId = context.traceId,
                    record = record,
                )

                require(result.traceId == context.traceId) {
                    "Context and mapped identity result must use the same trace identity."
                }

                result
            }

            IdentityResolutionRequestStatus.UNAVAILABLE ->
                IdentityResult.create(
                    traceId = context.traceId,
                    status = IdentityStatus.UNRESOLVED,
                )

            IdentityResolutionRequestStatus.FAILED ->
                IdentityResult.create(
                    traceId = context.traceId,
                    status = IdentityStatus.FAILED,
                    error = requireNotNull(requestResult.error),
                )
        }
    }
}
