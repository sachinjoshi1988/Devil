package com.devil.core.runtime.identity

import com.devil.core.model.identity.IdentityResolutionRecord
import com.devil.core.model.identity.IdentityResolutionRequest

/**
 * Produces an identity-resolution record from structured identity evidence.
 *
 * This resolver is bounded to identity resolution. It does not authenticate
 * the subject, prove ownership, evaluate trust, grant authorization, reason
 * about goals, plan work, or execute capabilities.
 */
interface IdentityResolutionResolver {

    fun resolve(
        request: IdentityResolutionRequest,
    ): IdentityResolutionRecord
}
