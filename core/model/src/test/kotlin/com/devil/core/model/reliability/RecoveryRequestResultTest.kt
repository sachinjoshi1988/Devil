package com.devil.core.model.reliability

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class RecoveryRequestResultTest {

    @Test
    fun `unavailable result contains reason and no request`() {
        val result =
            RecoveryRequestResult.unavailable(
                reason = "  Recovery is unavailable.  ",
            )

        assertEquals(
            RecoveryRequestStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(
            result.request,
        )
        assertEquals(
            "Recovery is unavailable.",
            result.reason,
        )
    }

    @Test
    fun `exhausted result contains reason and no request`() {
        val result =
            RecoveryRequestResult.exhausted(
                reason = "Attempt budget exhausted.",
            )

        assertEquals(
            RecoveryRequestStatus.EXHAUSTED,
            result.status,
        )
        assertNull(
            result.request,
        )
    }
}
