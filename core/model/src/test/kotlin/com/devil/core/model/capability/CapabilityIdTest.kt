package com.devil.core.model.capability

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class CapabilityIdTest {

    @Test
    fun `from trims surrounding whitespace`() {
        val capabilityId = CapabilityId.from("  capability-camera  ")

        assertEquals("capability-camera", capabilityId.value)
    }

    @Test
    fun `from preserves a valid capability identity`() {
        val capabilityId = CapabilityId.from("capability-settings")

        assertEquals("capability-settings", capabilityId.value)
    }

    @Test
    fun `from rejects a blank capability identity`() {
        assertFailsWith<IllegalArgumentException> {
            CapabilityId.from("   ")
        }
    }
}
