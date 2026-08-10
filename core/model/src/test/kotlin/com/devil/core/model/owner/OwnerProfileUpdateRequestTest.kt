package com.devil.core.model.owner

import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class OwnerProfileUpdateRequestTest {

    @Test
    fun `profile replacement preserves one owner identity`() {
        val ownerIdentityId =
            IdentityId.from(
                "identity-stage-43-update-owner-001",
            )

        val currentSnapshot =
            OwnerProfileSnapshot.create(
                profile =
                    OwnerProfile.create(
                        ownerIdentityId = ownerIdentityId,
                    ),
            )

        val proposedProfile =
            OwnerProfile.create(
                ownerIdentityId = ownerIdentityId,
                displayName = "Updated Owner",
            )

        val request =
            OwnerProfileUpdateRequest.replaceProfile(
                currentSnapshot = currentSnapshot,
                proposedProfile = proposedProfile,
            )

        assertEquals(
            OwnerProfileUpdateType.REPLACE_PROFILE,
            request.type,
        )
        assertEquals(
            proposedProfile,
            request.proposedProfile,
        )
        assertNull(request.proposedRelationship)
        assertNull(request.subjectIdentityId)
    }

    @Test
    fun `profile replacement rejects another owner identity`() {
        val currentSnapshot =
            OwnerProfileSnapshot.create(
                profile =
                    OwnerProfile.create(
                        ownerIdentityId =
                            IdentityId.from(
                                "identity-stage-43-update-owner-002",
                            ),
                    ),
            )

        assertFailsWith<IllegalArgumentException> {
            OwnerProfileUpdateRequest.replaceProfile(
                currentSnapshot = currentSnapshot,
                proposedProfile =
                    OwnerProfile.create(
                        ownerIdentityId =
                            IdentityId.from(
                                "identity-stage-43-update-owner-003",
                            ),
                    ),
            )
        }
    }
}
