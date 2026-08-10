package com.devil.core.model.owner

import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class OwnerRelationshipTest {

    @Test
    fun `relationship preserves bounded descriptive context`() {
        val ownerIdentityId =
            IdentityId.from(
                "identity-stage-43-owner-003",
            )

        val subjectIdentityId =
            IdentityId.from(
                "identity-stage-43-subject-001",
            )

        val relationship =
            OwnerRelationship.create(
                ownerIdentityId = ownerIdentityId,
                subjectIdentityId = subjectIdentityId,
                type = OwnerRelationshipType.FAMILY,
                label = "  family member  ",
            )

        assertEquals(
            ownerIdentityId,
            relationship.ownerIdentityId,
        )
        assertEquals(
            subjectIdentityId,
            relationship.subjectIdentityId,
        )
        assertEquals(
            OwnerRelationshipType.FAMILY,
            relationship.type,
        )
        assertEquals(
            "family member",
            relationship.label,
        )
    }

    @Test
    fun `self relationship requires identical owner and subject identity`() {
        val ownerIdentityId =
            IdentityId.from(
                "identity-stage-43-owner-004",
            )

        assertFailsWith<IllegalArgumentException> {
            OwnerRelationship.create(
                ownerIdentityId = ownerIdentityId,
                subjectIdentityId =
                    IdentityId.from(
                        "identity-stage-43-subject-002",
                    ),
                type = OwnerRelationshipType.SELF,
            )
        }
    }

    @Test
    fun `non self relationship rejects owner identity as subject`() {
        val ownerIdentityId =
            IdentityId.from(
                "identity-stage-43-owner-005",
            )

        assertFailsWith<IllegalArgumentException> {
            OwnerRelationship.create(
                ownerIdentityId = ownerIdentityId,
                subjectIdentityId = ownerIdentityId,
                type = OwnerRelationshipType.FRIEND,
            )
        }
    }
}
