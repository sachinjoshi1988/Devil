package com.devil.app.accessibility

import com.devil.app.capability.DefaultAndroidCapabilityAvailabilitySource
import com.devil.core.model.capability.CapabilityAvailabilityState
import kotlin.test.Test
import kotlin.test.assertEquals

class Stage38AccessibilityCapabilityAvailabilityTest {

    @Test
    fun `accessibility capability is unavailable without connected service`() {
        val result =
            DefaultAndroidCapabilityAvailabilitySource()
                .availability(
                    AndroidAccessibilityCapability.contract,
                )

        assertEquals(
            CapabilityAvailabilityState.UNAVAILABLE,
            result,
        )
    }
}
