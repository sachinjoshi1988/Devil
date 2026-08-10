package com.devil.core.model.owner

/**
 * Stage 43 pure structural policy for deriving owner-profile snapshots.
 *
 * The policy does not determine factual truth, identity authenticity,
 * relationship authenticity, subject trust, guardian authority, security
 * position, authorization, or memory eligibility.
 *
 * It performs no I/O and no persistence.
 */
class OwnerProfileUpdatePolicy {

    fun evaluate(
        request: OwnerProfileUpdateRequest,
    ): OwnerProfileUpdateResult {
        return when (request.type) {
            OwnerProfileUpdateType.REPLACE_PROFILE ->
                replaceProfile(
                    request = request,
                )

            OwnerProfileUpdateType.UPSERT_RELATIONSHIP ->
                upsertRelationship(
                    request = request,
                )

            OwnerProfileUpdateType.REMOVE_RELATIONSHIP ->
                removeRelationship(
                    request = request,
                )
        }
    }

    private fun replaceProfile(
        request: OwnerProfileUpdateRequest,
    ): OwnerProfileUpdateResult {
        val proposedProfile =
            request.proposedProfile
                ?: return OwnerProfileUpdateResult.rejected(
                    snapshot = request.currentSnapshot,
                    reason =
                        "Profile replacement requires a proposed owner profile.",
                )

        if (
            proposedProfile.ownerIdentityId !=
                request.currentSnapshot.profile.ownerIdentityId
        ) {
            return OwnerProfileUpdateResult.rejected(
                snapshot = request.currentSnapshot,
                reason =
                    "Profile replacement must preserve owner identity.",
            )
        }

        return OwnerProfileUpdateResult.accepted(
            snapshot =
                OwnerProfileSnapshot.create(
                    profile = proposedProfile,
                    relationships =
                        request.currentSnapshot.relationships,
                ),
        )
    }

    private fun upsertRelationship(
        request: OwnerProfileUpdateRequest,
    ): OwnerProfileUpdateResult {
        val proposedRelationship =
            request.proposedRelationship
                ?: return OwnerProfileUpdateResult.rejected(
                    snapshot = request.currentSnapshot,
                    reason =
                        "Relationship update requires a proposed relationship.",
                )

        if (
            proposedRelationship.ownerIdentityId !=
                request.currentSnapshot.profile.ownerIdentityId
        ) {
            return OwnerProfileUpdateResult.rejected(
                snapshot = request.currentSnapshot,
                reason =
                    "Relationship update must preserve owner identity.",
            )
        }

        val updatedRelationships =
            request.currentSnapshot.relationships
                .filterNot {
                    it.subjectIdentityId ==
                        proposedRelationship.subjectIdentityId
                } +
                proposedRelationship

        return OwnerProfileUpdateResult.accepted(
            snapshot =
                OwnerProfileSnapshot.create(
                    profile = request.currentSnapshot.profile,
                    relationships = updatedRelationships,
                ),
        )
    }

    private fun removeRelationship(
        request: OwnerProfileUpdateRequest,
    ): OwnerProfileUpdateResult {
        val subjectIdentityId =
            request.subjectIdentityId
                ?: return OwnerProfileUpdateResult.rejected(
                    snapshot = request.currentSnapshot,
                    reason =
                        "Relationship removal requires a subject identity.",
                )

        val relationshipExists =
            request.currentSnapshot.relationships.any {
                it.subjectIdentityId == subjectIdentityId
            }

        if (!relationshipExists) {
            return OwnerProfileUpdateResult.rejected(
                snapshot = request.currentSnapshot,
                reason =
                    "No relationship exists for the requested subject identity.",
            )
        }

        return OwnerProfileUpdateResult.accepted(
            snapshot =
                OwnerProfileSnapshot.create(
                    profile = request.currentSnapshot.profile,
                    relationships =
                        request.currentSnapshot.relationships.filterNot {
                            it.subjectIdentityId == subjectIdentityId
                        },
                ),
        )
    }
}
