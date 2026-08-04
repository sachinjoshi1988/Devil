package com.devil.core.model.identity

/**
 * Groups the candidate identities considered during one future identity
 * resolution process.
 *
 * Every candidate must represent a unique identity. This collection does not
 * rank candidates, select an identity, authenticate a subject, prove ownership,
 * evaluate trust, or grant authorization.
 */
@ConsistentCopyVisibility
data class IdentityResolutionCandidateSet private constructor(
    val candidates: List<IdentityResolutionCandidate>,
) {
    companion object {
        fun create(
            candidates: List<IdentityResolutionCandidate>,
        ): IdentityResolutionCandidateSet {
            require(candidates.isNotEmpty()) {
                "Identity resolution candidate set must contain at least one candidate."
            }

            require(
                candidates
                    .map { candidate -> candidate.identityId }
                    .distinct()
                    .size == candidates.size,
            ) {
                "Identity resolution candidate identities must be unique."
            }

            return IdentityResolutionCandidateSet(
                candidates = candidates.toList(),
            )
        }
    }
}
