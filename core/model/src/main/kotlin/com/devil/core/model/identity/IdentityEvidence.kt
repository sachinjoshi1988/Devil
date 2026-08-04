package com.devil.core.model.identity

import com.devil.core.model.common.DevilTimestamp

/**
 * Represents one bounded observation associated with a possible identity.
 *
 * Evidence may support later identity resolution, but this record does not
 * authenticate the subject, prove ownership, evaluate trust, establish a
 * relationship, or grant authorization.
 */
@ConsistentCopyVisibility
data class IdentityEvidence private constructor(
    val claimedIdentityId: IdentityId,
    val source: IdentityEvidenceSource,
    val observedAt: DevilTimestamp,
    val reference: String,
) {
    companion object {
        fun create(
            claimedIdentityId: IdentityId,
            source: IdentityEvidenceSource,
            observedAt: DevilTimestamp,
            reference: String,
        ): IdentityEvidence {
            val normalizedReference = reference.trim()

            require(normalizedReference.isNotEmpty()) {
                "Identity evidence reference must not be blank."
            }

            return IdentityEvidence(
                claimedIdentityId = claimedIdentityId,
                source = source,
                observedAt = observedAt,
                reference = normalizedReference,
            )
        }
    }
}
