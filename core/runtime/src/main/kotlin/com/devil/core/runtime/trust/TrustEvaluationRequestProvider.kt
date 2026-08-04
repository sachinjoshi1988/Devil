package com.devil.core.runtime.trust

import com.devil.core.model.context.ContextEnvelope
import com.devil.core.runtime.identity.IdentityResult

/**
 * Constructs a structured trust-evaluation request after identity resolution.
 *
 * This provider does not evaluate trust, authenticate a subject, prove
 * ownership, grant authorization, enter Owner Mode, or permit execution.
 */
interface TrustEvaluationRequestProvider {

    fun provide(
        context: ContextEnvelope,
        identity: IdentityResult,
    ): TrustEvaluationRequestResult
}
