package com.devil.core.model.owner

import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class OwnerProfileTest {

    @Test
    fun `profile preserves owner identity and normalizes presentation fields`() {
        val ownerIdentityId =
            IdentityId.from(
                "identity-stage-43-owner-001",
            )

        val profile =
            OwnerProfile.create(
                ownerIdentityId = ownerIdentityId,
                displayName = "  Sachin  ",
                preferredFormOfAddress = "  buddy  ",
            )

        assertEquals(
            ownerIdentityId,
            profile.ownerIdentityId,
        )
        assertEquals(
            "Sachin",
            profile.displayName,
        )
        assertEquals(
            "buddy",
            profile.preferredFormOfAddress,
        )
    }

    @Test
    fun `blank optional profile fields normalize to absent`() {
        val profile =
            OwnerProfile.create(
                ownerIdentityId =
                    IdentityId.from(
                        "identity-stage-43-owner-002",
                    ),
                displayName = "   ",
                preferredFormOfAddress = "",
            )

        assertNull(profile.displayName)
        assertNull(profile.preferredFormOfAddress)
    }
}
