package com.devil.core.model.owner

import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class OwnerProfileUpdateResultTest {

    private val snapshot =
        OwnerProfileSnapshot.create(
            profile =
                OwnerProfile.create(
                    ownerIdentityId =
                        IdentityId.from(
                            "identity-stage-43-result-owner-001",
                        ),
                ),
        )

    @Test
    fun `accepted result contains snapshot without rejection reason`() {
        val result =
            OwnerProfileUpdateResult.accepted(
                snapshot = snapshot,
            )

        assertEquals(
            OwnerProfileUpdateStatus.ACCEPTED,
            result.status,
        )
        assertEquals(
            snapshot,
            result.snapshot,
        )
        assertNull(result.reason)
    }

    @Test
    fun `rejected result requires bounded reason`() {
        assertFailsWith<IllegalArgumentException> {
            OwnerProfileUpdateResult.rejected(
                snapshot = snapshot,
                reason = "   ",
            )
        }
    }
}
