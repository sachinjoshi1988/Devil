package com.devil.core.model.plan

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class PlanIdTest {

    @Test
    fun `from trims surrounding whitespace`() {
        val planId = PlanId.from("  plan-001  ")

        assertEquals("plan-001", planId.value)
    }

    @Test
    fun `from preserves a valid plan identity`() {
        val planId = PlanId.from("plan-002")

        assertEquals("plan-002", planId.value)
    }

    @Test
    fun `from rejects a blank plan identity`() {
        assertFailsWith<IllegalArgumentException> {
            PlanId.from("   ")
        }
    }
}
