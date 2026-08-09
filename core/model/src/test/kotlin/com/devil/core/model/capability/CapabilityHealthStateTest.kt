package com.devil.core.model.capability

import kotlin.test.Test
import kotlin.test.assertEquals

class CapabilityHealthStateTest {

    @Test
    fun `health vocabulary remains constitutionally bounded`() {
        assertEquals(
            listOf(
                "INITIALIZING",
                "READY",
                "BUSY",
                "PAUSED",
                "DEGRADED",
                "UNAVAILABLE",
                "RECOVERING",
                "RETIRED",
            ),
            CapabilityHealthState.entries.map { it.name },
        )
    }
}
