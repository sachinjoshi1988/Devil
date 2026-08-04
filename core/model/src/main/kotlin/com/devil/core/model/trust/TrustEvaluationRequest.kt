package com.devil.core.model.trust

import com.devil.core.model.context.ContextEnvelope
import com.devil.core.model.identity.IdentityId

/**
 * Represents one structured request for subject trust evaluation.
 *
 * The request binds constitutional context to a resolved subject identity. It
 * does not authenticate the subject, prove ownership, evaluate trust, grant
 * authorization, enter Owner Mode, or permit execution.
 */
@ConsistentCopyVisibility
data class TrustEvaluationRequest private constructor(
    val context: ContextEnvelope,
    val subjectIdentityId: IdentityId,
) {
    companion object {
        fun create(
            context: ContextEnvelope,
            subjectIdentityId: IdentityId,
        ): TrustEvaluationRequest {
            return TrustEvaluationRequest(
                context = context,
                subjectIdentityId = subjectIdentityId,
            )
        }
    }
}
