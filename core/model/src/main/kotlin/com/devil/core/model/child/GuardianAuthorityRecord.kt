package com.devil.core.model.child

import com.devil.core.model.identity.IdentityId

/**
 * Immutable Stage 44 record preserving one bounded guardian-authority state
 * supplied by an approved upstream source.
 *
 * The guardian and child identities must be distinct.
 *
 * This record deliberately does not derive guardian authority from
 * OwnerRelationship, OwnerRelationshipType.FAMILY, matching names, profile
 * information, device possession, age appearance, voice, or camera perception.
 *
 * The record does not authenticate either identity, establish subject trust,
 * enter Owner Mode, grant general authorization, persist memory, or permit
 * execution.
 */
@ConsistentCopyVisibility
data class GuardianAuthorityRecord private constructor(
    val childIdentityId: IdentityId,
    val guardianIdentityId: IdentityId,
    val status: GuardianAuthorityStatus,
) {
    companion object {

        fun create(
            childIdentityId: IdentityId,
            guardianIdentityId: IdentityId,
            status: GuardianAuthorityStatus,
        ): GuardianAuthorityRecord {
            require(childIdentityId != guardianIdentityId) {
                "Guardian and child identities must be distinct."
            }

            return GuardianAuthorityRecord(
                childIdentityId = childIdentityId,
                guardianIdentityId = guardianIdentityId,
                status = status,
            )
        }
    }
}
