package com.devil.core.model.owner

/**
 * Immutable Stage 43 bounded snapshot of owner-profile and relationship data.
 *
 * Every relationship in the snapshot must belong to the same owner identity as
 * the supplied OwnerProfile.
 *
 * This snapshot is descriptive state only.
 *
 * It does not authenticate the current subject, establish trust, enter Owner
 * Mode, grant authorization, persist logical memory, or permit execution.
 */
@ConsistentCopyVisibility
data class OwnerProfileSnapshot private constructor(
    val profile: OwnerProfile,
    val relationships: List<OwnerRelationship>,
) {
    companion object {

        fun create(
            profile: OwnerProfile,
            relationships: List<OwnerRelationship> = emptyList(),
        ): OwnerProfileSnapshot {
            require(
                relationships.all {
                    it.ownerIdentityId == profile.ownerIdentityId
                },
            ) {
                "Every relationship must belong to the supplied owner profile."
            }

            val distinctSubjects =
                relationships
                    .map {
                        it.subjectIdentityId
                    }
                    .distinct()

            require(distinctSubjects.size == relationships.size) {
                "Owner profile snapshot must not contain duplicate subject relationships."
            }

            return OwnerProfileSnapshot(
                profile = profile,
                relationships = relationships.toList(),
            )
        }
    }
}
