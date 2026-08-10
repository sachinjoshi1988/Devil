package com.devil.core.model.reliability

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class RecoveryAttemptResultTest {

    @Test
    fun `exhausted result contains no attempt record`() {
        val result =
            RecoveryAttemptResult.exhausted(
                reason = "  Finite budget exhausted.  ",
            )

        assertEquals(
            RecoveryAttemptStatus.EXHAUSTED,
            result.status,
        )
        assertNull(
            result.record,
        )
        assertEquals(
            "Finite budget exhausted.",
            result.reason,
        )
    }

    @Test
    fun `unavailable result contains no attempt record`() {
        val result =
            RecoveryAttemptResult.unavailable(
                reason = "Recovery request unavailable.",
            )

        assertEquals(
            RecoveryAttemptStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(
            result.record,
        )
    }
}
