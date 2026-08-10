package com.devil.core.model.owner

import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class OwnerProfileSnapshotTest {

    @Test
    fun `snapshot preserves one owner profile and its relationships`() {
        val ownerIdentityId =
            IdentityId.from(
                "identity-stage-43-owner-006",
            )

        val profile =
            OwnerProfile.create(
                ownerIdentityId = ownerIdentityId,
                displayName = "Owner",
            )

        val relationship =
            OwnerRelationship.create(
                ownerIdentityId = ownerIdentityId,
                subjectIdentityId =
                    IdentityId.from(
                        "identity-stage-43-subject-003",
                    ),
                type = OwnerRelationshipType.FRIEND,
            )

        val snapshot =
            OwnerProfileSnapshot.create(
                profile = profile,
                relationships =
                    listOf(
                        relationship,
                    ),
            )

        assertEquals(
            profile,
            snapshot.profile,
        )
        assertEquals(
            listOf(
                relationship,
            ),
            snapshot.relationships,
        )
    }

    @Test
    fun `snapshot rejects relationship belonging to another owner`() {
        val profile =
            OwnerProfile.create(
                ownerIdentityId =
                    IdentityId.from(
                        "identity-stage-43-owner-007",
                    ),
            )

        val relationship =
            OwnerRelationship.create(
                ownerIdentityId =
                    IdentityId.from(
                        "identity-stage-43-owner-008",
                    ),
                subjectIdentityId =
                    IdentityId.from(
                        "identity-stage-43-subject-004",
                    ),
                type = OwnerRelationshipType.PROFESSIONAL,
            )

        assertFailsWith<IllegalArgumentException> {
            OwnerProfileSnapshot.create(
                profile = profile,
                relationships =
                    listOf(
                        relationship,
                    ),
            )
        }
    }

    @Test
    fun `snapshot rejects duplicate subject relationships`() {
        val ownerIdentityId =
            IdentityId.from(
                "identity-stage-43-owner-009",
            )

        val subjectIdentityId =
            IdentityId.from(
                "identity-stage-43-subject-005",
            )

        val profile =
            OwnerProfile.create(
                ownerIdentityId = ownerIdentityId,
            )

        val first =
            OwnerRelationship.create(
                ownerIdentityId = ownerIdentityId,
                subjectIdentityId = subjectIdentityId,
                type = OwnerRelationshipType.FRIEND,
            )

        val second =
            OwnerRelationship.create(
                ownerIdentityId = ownerIdentityId,
                subjectIdentityId = subjectIdentityId,
                type = OwnerRelationshipType.OTHER,
            )

        assertFailsWith<IllegalArgumentException> {
            OwnerProfileSnapshot.create(
                profile = profile,
                relationships =
                    listOf(
                        first,
                        second,
                    ),
            )
        }
    }
}
