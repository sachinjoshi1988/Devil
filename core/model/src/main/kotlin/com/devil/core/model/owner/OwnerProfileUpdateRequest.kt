package com.devil.core.model.owner

import com.devil.core.model.identity.IdentityId

/**
 * One explicit Stage 43 request to derive a new bounded owner-profile snapshot.
 *
 * The request always preserves the current snapshot and exactly the data needed
 * by the requested structural operation.
 *
 * Creating this request does not establish that the supplied profile or
 * relationship information is factually true.
 *
 * Update request
 * != authentication
 * != ownership proof
 * != trust
 * != guardian authority
 * != authorization
 * != Owner Mode
 * != logical-memory proposal
 * != logical-memory commitment
 * != persistence.
 */
@ConsistentCopyVisibility
data class OwnerProfileUpdateRequest private constructor(
    val type: OwnerProfileUpdateType,
    val currentSnapshot: OwnerProfileSnapshot,
    val proposedProfile: OwnerProfile?,
    val proposedRelationship: OwnerRelationship?,
    val subjectIdentityId: IdentityId?,
) {
    companion object {

        fun replaceProfile(
            currentSnapshot: OwnerProfileSnapshot,
            proposedProfile: OwnerProfile,
        ): OwnerProfileUpdateRequest {
            require(
                proposedProfile.ownerIdentityId ==
                    currentSnapshot.profile.ownerIdentityId,
            ) {
                "Stage 43 profile replacement must preserve owner identity."
            }

            return OwnerProfileUpdateRequest(
                type = OwnerProfileUpdateType.REPLACE_PROFILE,
                currentSnapshot = currentSnapshot,
                proposedProfile = proposedProfile,
                proposedRelationship = null,
                subjectIdentityId = null,
            )
        }

        fun upsertRelationship(
            currentSnapshot: OwnerProfileSnapshot,
            proposedRelationship: OwnerRelationship,
        ): OwnerProfileUpdateRequest {
            require(
                proposedRelationship.ownerIdentityId ==
                    currentSnapshot.profile.ownerIdentityId,
            ) {
                "Stage 43 relationship update must preserve owner identity."
            }

            return OwnerProfileUpdateRequest(
                type = OwnerProfileUpdateType.UPSERT_RELATIONSHIP,
                currentSnapshot = currentSnapshot,
                proposedProfile = null,
                proposedRelationship = proposedRelationship,
                subjectIdentityId = null,
            )
        }

        fun removeRelationship(
            currentSnapshot: OwnerProfileSnapshot,
            subjectIdentityId: IdentityId,
        ): OwnerProfileUpdateRequest {
            return OwnerProfileUpdateRequest(
                type = OwnerProfileUpdateType.REMOVE_RELATIONSHIP,
                currentSnapshot = currentSnapshot,
                proposedProfile = null,
                proposedRelationship = null,
                subjectIdentityId = subjectIdentityId,
            )
        }
    }
}
