package com.devil.core.model.owner

import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals

class OwnerProfileUpdateCoordinatorTest {

    @Test
    fun `coordinator preserves bounded policy result`() {
        val ownerIdentityId =
            IdentityId.from(
                "identity-stage-43-coordinator-owner-001",
            )

        val currentSnapshot =
            OwnerProfileSnapshot.create(
                profile =
                    OwnerProfile.create(
                        ownerIdentityId = ownerIdentityId,
                        displayName = "Before",
                    ),
            )

        val proposedProfile =
            OwnerProfile.create(
                ownerIdentityId = ownerIdentityId,
                displayName = "After",
            )

        val result =
            OwnerProfileUpdateCoordinator().update(
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
    }
}
