package com.devil.core.model.owner

import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class OwnerProfileQueryPolicyTest {

    @Test
    fun `policy exposes bounded preferred form of address`() {
        val snapshot =
            OwnerProfileSnapshot.create(
                profile =
                    OwnerProfile.create(
                        ownerIdentityId =
                            IdentityId.from(
                                "identity-stage-43-query-policy-owner-001",
                            ),
                        preferredFormOfAddress = "Boss",
                    ),
            )

        val result =
            OwnerProfileQueryPolicy().evaluate(
                query =
                    OwnerProfileQuery.preferredFormOfAddress(),
                snapshot = snapshot,
            )

        assertEquals(
            OwnerProfileQueryStatus.AVAILABLE,
            result.status,
        )
        assertEquals(
            "Boss",
            result.preferredFormOfAddress,
        )
    }

    @Test
    fun `policy finds exact descriptive relationship by subject identity`() {
        val ownerIdentityId =
            IdentityId.from(
                "identity-stage-43-query-policy-owner-002",
            )

        val targetSubjectIdentityId =
            IdentityId.from(
                "identity-stage-43-query-policy-subject-001",
            )

        val relationship =
            OwnerRelationship.create(
                ownerIdentityId = ownerIdentityId,
                subjectIdentityId = targetSubjectIdentityId,
                type = OwnerRelationshipType.FAMILY,
                label = "Family member",
            )

        val snapshot =
            OwnerProfileSnapshot.create(
                profile =
                    OwnerProfile.create(
                        ownerIdentityId = ownerIdentityId,
                    ),
                relationships =
                    listOf(
                        relationship,
                    ),
            )

        val result =
            OwnerProfileQueryPolicy().evaluate(
                query =
                    OwnerProfileQuery.relationshipForSubject(
                        subjectIdentityId =
                            targetSubjectIdentityId,
                    ),
                snapshot = snapshot,
            )

        assertEquals(
            OwnerProfileQueryStatus.AVAILABLE,
            result.status,
        )
        assertEquals(
            relationship,
            result.relationship,
        )
    }

    @Test
    fun `unknown relationship remains unavailable rather than invented`() {
        val snapshot =
            OwnerProfileSnapshot.create(
                profile =
                    OwnerProfile.create(
                        ownerIdentityId =
                            IdentityId.from(
                                "identity-stage-43-query-policy-owner-003",
                            ),
                    ),
            )

        val result =
            OwnerProfileQueryPolicy().evaluate(
                query =
                    OwnerProfileQuery.relationshipForSubject(
                        subjectIdentityId =
                            IdentityId.from(
                                "identity-stage-43-query-policy-unknown-001",
                            ),
                    ),
                snapshot = snapshot,
            )

        assertEquals(
            OwnerProfileQueryStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.relationship)
    }
}
