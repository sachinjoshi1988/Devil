package com.devil.core.model.owner

/**
 * Result of one bounded Stage 43 owner-profile query.
 *
 * Exactly one result payload may be exposed according to the requested query.
 *
 * The complete source snapshot is retained so provenance remains explicit.
 *
 * This result is descriptive owner-domain data only.
 */
@ConsistentCopyVisibility
data class OwnerProfileQueryResult private constructor(
    val queryType: OwnerProfileQueryType,
    val status: OwnerProfileQueryStatus,
    val snapshot: OwnerProfileSnapshot,
    val profile: OwnerProfile?,
    val preferredFormOfAddress: String?,
    val relationship: OwnerRelationship?,
) {
    companion object {

        fun profile(
            snapshot: OwnerProfileSnapshot,
        ): OwnerProfileQueryResult {
            return OwnerProfileQueryResult(
                queryType = OwnerProfileQueryType.PROFILE,
                status = OwnerProfileQueryStatus.AVAILABLE,
                snapshot = snapshot,
                profile = snapshot.profile,
                preferredFormOfAddress = null,
                relationship = null,
            )
        }

        fun preferredFormOfAddress(
            snapshot: OwnerProfileSnapshot,
        ): OwnerProfileQueryResult {
            val value =
                snapshot.profile.preferredFormOfAddress

            return OwnerProfileQueryResult(
                queryType =
                    OwnerProfileQueryType.PREFERRED_FORM_OF_ADDRESS,
                status =
                    if (value != null) {
                        OwnerProfileQueryStatus.AVAILABLE
                    } else {
                        OwnerProfileQueryStatus.UNAVAILABLE
                    },
                snapshot = snapshot,
                profile = null,
                preferredFormOfAddress = value,
                relationship = null,
            )
        }

        fun relationship(
            snapshot: OwnerProfileSnapshot,
            relationship: OwnerRelationship?,
        ): OwnerProfileQueryResult {
            return OwnerProfileQueryResult(
                queryType =
                    OwnerProfileQueryType.RELATIONSHIP_FOR_SUBJECT,
                status =
                    if (relationship != null) {
                        OwnerProfileQueryStatus.AVAILABLE
                    } else {
                        OwnerProfileQueryStatus.UNAVAILABLE
                    },
                snapshot = snapshot,
                profile = null,
                preferredFormOfAddress = null,
                relationship = relationship,
            )
        }
    }
}
