package com.devil.core.runtime.trust

import com.devil.core.model.context.ContextEnvelope
import com.devil.core.runtime.identity.IdentityResult

/**
 * Evaluates trust after identity resolution.
 *
 * This authority does not resolve identity, authenticate a subject, grant
 * authorization, perform reasoning, plan, or execute capabilities.
 */
interface TrustAuthority {

    fun evaluate(
        context: ContextEnvelope,
        identity: IdentityResult,
    ): TrustResult
}
