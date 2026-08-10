package com.devil.core.model.owner

import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class OwnerProfileQueryResultTest {

    private val ownerIdentityId =
        IdentityId.from(
            "identity-stage-43-query-result-owner-001",
        )

    @Test
    fun `preferred address is unavailable when profile contains no value`() {
        val snapshot =
            OwnerProfileSnapshot.create(
                profile =
                    OwnerProfile.create(
                        ownerIdentityId = ownerIdentityId,
                    ),
            )

        val result =
            OwnerProfileQueryResult.preferredFormOfAddress(
                snapshot = snapshot,
            )

        assertEquals(
            OwnerProfileQueryStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.preferredFormOfAddress)
        assertNull(result.profile)
        assertNull(result.relationship)
    }

    @Test
    fun `relationship result exposes only supplied relationship`() {
        val relationship =
            OwnerRelationship.create(
                ownerIdentityId = ownerIdentityId,
                subjectIdentityId =
                    IdentityId.from(
                        "identity-stage-43-query-result-subject-001",
                    ),
                type = OwnerRelationshipType.FRIEND,
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
            OwnerProfileQueryResult.relationship(
                snapshot = snapshot,
                relationship = relationship,
            )

        assertEquals(
            OwnerProfileQueryStatus.AVAILABLE,
            result.status,
        )
        assertEquals(
            relationship,
            result.relationship,
        )
        assertNull(result.profile)
        assertNull(result.preferredFormOfAddress)
    }
}
