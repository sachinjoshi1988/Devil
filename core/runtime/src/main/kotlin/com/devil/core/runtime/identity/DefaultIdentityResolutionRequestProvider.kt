package com.devil.core.runtime.identity

import com.devil.core.model.context.ContextEnvelope

/**
 * Default Stage 3 identity-resolution request provider.
 *
 * ContextEnvelope currently contains constitutional metadata but no genuine
 * subject identity evidence. This implementation therefore reports that no
 * request is available rather than fabricating an identity or evidence.
 *
 * It performs no resolution, authentication, ownership determination, trust
 * evaluation, authorization, planning, or execution.
 */
class DefaultIdentityResolutionRequestProvider :
    IdentityResolutionRequestProvider {

    override fun provide(
        context: ContextEnvelope,
    ): IdentityResolutionRequestResult {
        return IdentityResolutionRequestResult.create(
            traceId = context.traceId,
            status = IdentityResolutionRequestStatus.UNAVAILABLE,
        )
    }
}
