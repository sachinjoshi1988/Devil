package com.devil.core.model.owner

import com.devil.core.model.identity.IdentityId

/**
 * Represents the owner and subject identities associated with one bounded
 * owner context.
 *
 * The owner and current subject may be the same identity or different
 * identities. This record does not prove authentication, ownership, trust,
 * relationship, authorization, or permission to act.
 */
@ConsistentCopyVisibility
data class OwnerContext private constructor(
    val ownerIdentityId: IdentityId,
    val subjectIdentityId: IdentityId,
) {
    companion object {
        fun create(
            ownerIdentityId: IdentityId,
            subjectIdentityId: IdentityId,
        ): OwnerContext {
            return OwnerContext(
                ownerIdentityId = ownerIdentityId,
                subjectIdentityId = subjectIdentityId,
            )
        }
    }
}
