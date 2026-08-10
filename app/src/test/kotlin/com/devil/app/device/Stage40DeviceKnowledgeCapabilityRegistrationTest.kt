package com.devil.app.device

import com.devil.app.capability.DefaultAndroidCapabilityRegistrationSource
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class Stage40DeviceKnowledgeCapabilityRegistrationTest {

    @Test
    fun `default Android registration contains device knowledge capability`() {
        val registrations =
            DefaultAndroidCapabilityRegistrationSource()
                .registrations()

        assertTrue(
            registrations.contains(
                AndroidDeviceKnowledgeCapability.contract,
            ),
        )

        assertEquals(
            1,
            registrations.count {
                AndroidDeviceKnowledgeCapability.matches(it)
            },
        )
    }
}
