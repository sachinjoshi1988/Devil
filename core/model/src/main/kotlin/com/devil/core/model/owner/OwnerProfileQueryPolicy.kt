package com.devil.core.model.owner

/**
 * Stage 43 pure policy for reading bounded descriptive owner-profile state.
 *
 * It performs no identity resolution, authentication, trust evaluation,
 * relationship verification, guardian-policy evaluation, authorization,
 * memory operation, persistence, runtime submission, or execution.
 */
class OwnerProfileQueryPolicy {

    fun evaluate(
        query: OwnerProfileQuery,
        snapshot: OwnerProfileSnapshot,
    ): OwnerProfileQueryResult {
        return when (query.type) {
            OwnerProfileQueryType.PROFILE ->
                OwnerProfileQueryResult.profile(
                    snapshot = snapshot,
                )

            OwnerProfileQueryType.PREFERRED_FORM_OF_ADDRESS ->
                OwnerProfileQueryResult.preferredFormOfAddress(
                    snapshot = snapshot,
                )

            OwnerProfileQueryType.RELATIONSHIP_FOR_SUBJECT -> {
                val subjectIdentityId =
                    requireNotNull(query.subjectIdentityId) {
                        "Relationship query requires a subject identity."
                    }

                OwnerProfileQueryResult.relationship(
                    snapshot = snapshot,
                    relationship =
                        snapshot.relationships.firstOrNull {
                            it.subjectIdentityId ==
                                subjectIdentityId
                        },
                )
            }
        }
    }
}
