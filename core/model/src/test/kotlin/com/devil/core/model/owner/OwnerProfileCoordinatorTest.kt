package com.devil.core.model.owner

import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals

class OwnerProfileCoordinatorTest {

    @Test
    fun `coordinator returns supplied owner profile snapshot unchanged`() {
        val snapshot =
            OwnerProfileSnapshot.create(
                profile =
                    OwnerProfile.create(
                        ownerIdentityId =
                            IdentityId.from(
                                "identity-stage-43-owner-010",
                            ),
                        displayName = "Owner",
                    ),
            )

        val coordinator =
            OwnerProfileCoordinator(
                source =
                    OwnerProfileSource {
                        snapshot
                    },
            )

        assertEquals(
            snapshot,
            coordinator.snapshot(),
        )
    }
}
