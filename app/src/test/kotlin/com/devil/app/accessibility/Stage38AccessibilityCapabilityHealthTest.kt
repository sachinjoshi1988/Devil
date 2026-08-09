package com.devil.app.accessibility

import com.devil.app.capability.DefaultAndroidCapabilityHealthSource
import com.devil.core.model.capability.CapabilityHealthState
import kotlin.test.Test
import kotlin.test.assertEquals

class Stage38AccessibilityCapabilityHealthTest {

    @Test
    fun `accessibility capability health is unavailable without connected service`() {
        val result =
            DefaultAndroidCapabilityHealthSource()
                .health(
                    AndroidAccessibilityCapability.contract,
                )

        assertEquals(
            CapabilityHealthState.UNAVAILABLE,
            result,
        )
    }
}
