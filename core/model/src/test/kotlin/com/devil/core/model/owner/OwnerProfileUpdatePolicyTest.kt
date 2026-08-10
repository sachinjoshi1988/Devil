package com.devil.core.model.owner

import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class OwnerProfileUpdatePolicyTest {

    @Test
    fun `policy replaces presentation profile while preserving relationships`() {
        val ownerIdentityId =
            IdentityId.from(
                "identity-stage-43-policy-owner-001",
            )

        val relationship =
            OwnerRelationship.create(
                ownerIdentityId = ownerIdentityId,
                subjectIdentityId =
                    IdentityId.from(
                        "identity-stage-43-policy-subject-001",
                    ),
                type = OwnerRelationshipType.FAMILY,
            )

        val currentSnapshot =
            OwnerProfileSnapshot.create(
                profile =
                    OwnerProfile.create(
                        ownerIdentityId = ownerIdentityId,
                        displayName = "Old Name",
                    ),
                relationships =
                    listOf(
                        relationship,
                    ),
            )

        val proposedProfile =
            OwnerProfile.create(
                ownerIdentityId = ownerIdentityId,
                displayName = "New Name",
            )

        val result =
            OwnerProfileUpdatePolicy().evaluate(
                request =
                    OwnerProfileUpdateRequest.replaceProfile(
                        currentSnapshot = currentSnapshot,
                        proposedProfile = proposedProfile,
                    ),
            )

        assertEquals(
            OwnerProfileUpdateStatus.ACCEPTED,
            result.status,
        )
        assertEquals(
            proposedProfile,
            result.snapshot.profile,
        )
        assertEquals(
            listOf(
                relationship,
            ),
            result.snapshot.relationships,
        )
        assertNull(result.reason)
    }

    @Test
    fun `policy upserts relationship for one subject`() {
        val ownerIdentityId =
            IdentityId.from(
                "identity-stage-43-policy-owner-002",
            )

        val subjectIdentityId =
            IdentityId.from(
                "identity-stage-43-policy-subject-002",
            )

        val currentRelationship =
            OwnerRelationship.create(
                ownerIdentityId = ownerIdentityId,
                subjectIdentityId = subjectIdentityId,
                type = OwnerRelationshipType.FRIEND,
            )

        val currentSnapshot =
            OwnerProfileSnapshot.create(
                profile =
                    OwnerProfile.create(
                        ownerIdentityId = ownerIdentityId,
                    ),
                relationships =
                    listOf(
                        currentRelationship,
                    ),
            )

        val proposedRelationship =
            OwnerRelationship.create(
                ownerIdentityId = ownerIdentityId,
                subjectIdentityId = subjectIdentityId,
                type = OwnerRelationshipType.PROFESSIONAL,
                label = "Colleague",
            )

        val result =
            OwnerProfileUpdatePolicy().evaluate(
                request =
                    OwnerProfileUpdateRequest.upsertRelationship(
                        currentSnapshot = currentSnapshot,
                        proposedRelationship = proposedRelationship,
                    ),
            )

        assertEquals(
            OwnerProfileUpdateStatus.ACCEPTED,
            result.status,
        )
        assertEquals(
            listOf(
                proposedRelationship,
            ),
            result.snapshot.relationships,
        )
    }

    @Test
    fun `policy removes existing relationship`() {
        val ownerIdentityId =
            IdentityId.from(
                "identity-stage-43-policy-owner-003",
            )

        val subjectIdentityId =
            IdentityId.from(
                "identity-stage-43-policy-subject-003",
            )

        val currentSnapshot =
            OwnerProfileSnapshot.create(
                profile =
                    OwnerProfile.create(
                        ownerIdentityId = ownerIdentityId,
                    ),
                relationships =
                    listOf(
                        OwnerRelationship.create(
                            ownerIdentityId = ownerIdentityId,
                            subjectIdentityId = subjectIdentityId,
                            type = OwnerRelationshipType.OTHER,
                        ),
                    ),
            )

        val result =
            OwnerProfileUpdatePolicy().evaluate(
                request =
                    OwnerProfileUpdateRequest.removeRelationship(
                        currentSnapshot = currentSnapshot,
                        subjectIdentityId = subjectIdentityId,
                    ),
            )

        assertEquals(
            OwnerProfileUpdateStatus.ACCEPTED,
            result.status,
        )
        assertEquals(
            emptyList(),
            result.snapshot.relationships,
        )
    }

    @Test
    fun `policy rejects removal when relationship does not exist`() {
        val currentSnapshot =
            OwnerProfileSnapshot.create(
                profile =
                    OwnerProfile.create(
                        ownerIdentityId =
                            IdentityId.from(
                                "identity-stage-43-policy-owner-004",
                            ),
                    ),
            )

        val result =
            OwnerProfileUpdatePolicy().evaluate(
                request =
                    OwnerProfileUpdateRequest.removeRelationship(
                        currentSnapshot = currentSnapshot,
                        subjectIdentityId =
                            IdentityId.from(
                                "identity-stage-43-policy-subject-004",
                            ),
                    ),
            )

        assertEquals(
            OwnerProfileUpdateStatus.REJECTED,
            result.status,
        )
        assertEquals(
            currentSnapshot,
            result.snapshot,
        )
    }
}
