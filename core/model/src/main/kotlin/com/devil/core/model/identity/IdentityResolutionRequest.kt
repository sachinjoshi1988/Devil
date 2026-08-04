package com.devil.core.model.identity

import com.devil.core.model.context.ContextEnvelope

/**
 * Represents one structured request for identity resolution.
 *
 * The request binds constitutional context to a coherent set of identity
 * evidence. It does not authenticate the subject, determine evidence
 * sufficiency, resolve identity, prove ownership, evaluate trust, or grant
 * authorization.
 */
@ConsistentCopyVisibility
data class IdentityResolutionRequest private constructor(
    val context: ContextEnvelope,
    val evidenceSet: IdentityEvidenceSet,
) {
    companion object {
        fun create(
            context: ContextEnvelope,
            evidenceSet: IdentityEvidenceSet,
        ): IdentityResolutionRequest {
            return IdentityResolutionRequest(
                context = context,
                evidenceSet = evidenceSet,
            )
        }
    }
}
