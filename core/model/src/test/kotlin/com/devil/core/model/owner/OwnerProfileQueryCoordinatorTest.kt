package com.devil.core.model.owner

import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals

class OwnerProfileQueryCoordinatorTest {

    @Test
    fun `coordinator obtains one source snapshot and preserves bounded query result`() {
        val snapshot =
            OwnerProfileSnapshot.create(
                profile =
                    OwnerProfile.create(
                        ownerIdentityId =
                            IdentityId.from(
                                "identity-stage-43-query-coordinator-owner-001",
                            ),
                        displayName = "Owner",
                    ),
            )

        var reads = 0

        val coordinator =
            OwnerProfileQueryCoordinator(
                source =
                    OwnerProfileSource {
                        reads += 1
                        snapshot
                    },
            )

        val result =
            coordinator.query(
                request =
                    OwnerProfileQuery.profile(),
            )

        assertEquals(
            1,
            reads,
        )
        assertEquals(
            OwnerProfileQueryStatus.AVAILABLE,
            result.status,
        )
        assertEquals(
            snapshot.profile,
            result.profile,
        )
    }
}
