package com.devil.core.runtime.identity

import com.devil.core.model.context.ContextEnvelope

/**
 * Supplies genuine structured evidence for identity resolution when available.
 *
 * This provider must not fabricate identities, evidence, or ownership claims.
 * It does not resolve identity, authenticate a subject, evaluate trust, or
 * grant authorization.
 */
interface IdentityResolutionRequestProvider {

    fun provide(
        context: ContextEnvelope,
    ): IdentityResolutionRequestResult
}
