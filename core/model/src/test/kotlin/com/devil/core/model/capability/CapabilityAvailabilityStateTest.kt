package com.devil.core.model.capability

import kotlin.test.Test
import kotlin.test.assertEquals

class CapabilityAvailabilityStateTest {

    @Test
    fun `availability vocabulary remains bounded`() {
        assertEquals(
            listOf(
                "AVAILABLE",
                "UNAVAILABLE",
            ),
            CapabilityAvailabilityState.entries.map { it.name },
        )
    }
}
