package com.devil.core.model.identity

/**
 * Groups identity evidence submitted for one claimed subject identity.
 *
 * Every evidence item must refer to the same claimed identity. This collection
 * does not authenticate the subject, assess evidence quality, resolve identity,
 * prove ownership, evaluate trust, or grant authorization.
 */
@ConsistentCopyVisibility
data class IdentityEvidenceSet private constructor(
    val claimedIdentityId: IdentityId,
    val evidence: List<IdentityEvidence>,
) {
    companion object {
        fun create(
            claimedIdentityId: IdentityId,
            evidence: List<IdentityEvidence>,
        ): IdentityEvidenceSet {
            require(evidence.isNotEmpty()) {
                "Identity evidence set must contain at least one evidence item."
            }

            require(
                evidence.all { item ->
                    item.claimedIdentityId == claimedIdentityId
                },
            ) {
                "Every evidence item must use the evidence set's claimed identity."
            }

            return IdentityEvidenceSet(
                claimedIdentityId = claimedIdentityId,
                evidence = evidence.toList(),
            )
        }
    }
}
