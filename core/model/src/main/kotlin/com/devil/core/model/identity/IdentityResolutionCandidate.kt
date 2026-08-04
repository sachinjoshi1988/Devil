package com.devil.core.model.identity

/**
 * Represents one candidate identity considered during a future identity
 * resolution process.
 *
 * The candidate binds one claimed identity to its coherent evidence set. It
 * does not authenticate the subject, establish evidence sufficiency, resolve
 * identity, prove ownership, evaluate trust, or grant authorization.
 */
@ConsistentCopyVisibility
data class IdentityResolutionCandidate private constructor(
    val identityId: IdentityId,
    val evidenceSet: IdentityEvidenceSet,
) {
    companion object {
        fun create(
            identityId: IdentityId,
            evidenceSet: IdentityEvidenceSet,
        ): IdentityResolutionCandidate {
            require(evidenceSet.claimedIdentityId == identityId) {
                "Identity resolution candidate and evidence set must use the same claimed identity."
            }

            return IdentityResolutionCandidate(
                identityId = identityId,
                evidenceSet = evidenceSet,
            )
        }
    }
}
