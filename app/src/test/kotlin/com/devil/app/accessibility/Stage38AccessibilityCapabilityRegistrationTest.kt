package com.devil.app.accessibility

import com.devil.app.capability.DefaultAndroidCapabilityRegistrationSource
import kotlin.test.Test
import kotlin.test.assertEquals

class Stage38AccessibilityCapabilityRegistrationTest {

    @Test
    fun `default Android registration source exposes accessibility action capability`() {
        val registrations =
            DefaultAndroidCapabilityRegistrationSource()
                .registrations()

        assertEquals(
            1,
            registrations.size,
        )

        assertEquals(
            AndroidAccessibilityCapability.capabilityId,
            registrations.single().capabilityId,
        )
    }
}
