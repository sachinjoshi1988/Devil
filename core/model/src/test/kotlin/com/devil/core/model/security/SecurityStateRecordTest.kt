package com.devil.core.model.security

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class SecurityStateRecordTest {

    @Test
    fun `create preserves locked security stage and normalizes rationale`() {
        val record = SecurityStateRecord.create(
            stage = SecurityStage.LOCKED,
            rationale = "  Android process has no authenticated session.  ",
        )

        assertEquals(
            SecurityStage.LOCKED,
            record.stage,
        )
        assertEquals(
            "Android process has no authenticated session.",
            record.rationale,
        )
    }

    @Test
    fun `create preserves wake without converting it to authentication`() {
        val record = SecurityStateRecord.create(
            stage = SecurityStage.WAKE,
            rationale = "Wake attention was established.",
        )

        assertEquals(
            SecurityStage.WAKE,
            record.stage,
        )

        check(record.stage != SecurityStage.AUTHENTICATION)
    }

    @Test
    fun `create preserves high security confirmation as a distinct stage`() {
        val record = SecurityStateRecord.create(
            stage = SecurityStage.HIGH_SECURITY_CONFIRMATION,
            rationale = "High-security confirmation stage is represented.",
        )

        assertEquals(
            SecurityStage.HIGH_SECURITY_CONFIRMATION,
            record.stage,
        )

        check(record.stage != SecurityStage.OWNER_MODE)
    }

    @Test
    fun `create rejects blank rationale`() {
        assertFailsWith<IllegalArgumentException> {
            SecurityStateRecord.create(
                stage = SecurityStage.LOCKED,
                rationale = "   ",
            )
        }
    }
}
