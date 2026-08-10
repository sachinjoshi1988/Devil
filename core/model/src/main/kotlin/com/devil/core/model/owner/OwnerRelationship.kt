package com.devil.core.model.owner

import com.devil.core.model.identity.IdentityId

/**
 * Immutable Stage 43 descriptive relationship between one owner identity and
 * one subject identity.
 *
 * The record preserves supplied relationship context only.
 *
 * A relationship record does not prove that the supplied relationship is
 * authentic, current, reciprocal, trusted, security-relevant, or authorized.
 *
 * Relationship
 * != authentication
 * != trust
 * != guardian authority
 * != authorization
 * != Owner Mode
 * != execution authority.
 */
@ConsistentCopyVisibility
data class OwnerRelationship private constructor(
    val ownerIdentityId: IdentityId,
    val subjectIdentityId: IdentityId,
    val type: OwnerRelationshipType,
    val label: String?,
) {
    companion object {

        fun create(
            ownerIdentityId: IdentityId,
            subjectIdentityId: IdentityId,
            type: OwnerRelationshipType,
            label: String? = null,
        ): OwnerRelationship {
            val normalizedLabel =
                label
                    ?.trim()
                    ?.takeIf {
                        it.isNotEmpty()
                    }

            require(
                type != OwnerRelationshipType.SELF ||
                    ownerIdentityId == subjectIdentityId,
            ) {
                "SELF relationship requires matching owner and subject identities."
            }

            require(
                type == OwnerRelationshipType.SELF ||
                    ownerIdentityId != subjectIdentityId,
            ) {
                "A non-SELF relationship requires distinct owner and subject identities."
            }

            return OwnerRelationship(
                ownerIdentityId = ownerIdentityId,
                subjectIdentityId = subjectIdentityId,
                type = type,
                label = normalizedLabel,
            )
        }
    }
}
