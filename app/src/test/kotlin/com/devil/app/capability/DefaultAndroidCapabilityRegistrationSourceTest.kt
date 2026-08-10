package com.devil.app.capability

import com.devil.app.accessibility.AndroidAccessibilityCapability
import com.devil.app.device.AndroidDeviceKnowledgeCapability
import com.devil.app.vision.AndroidVisionCapability
import kotlin.test.Test
import kotlin.test.assertEquals

class DefaultAndroidCapabilityRegistrationSourceTest {

    @Test
    fun `default source registers genuine Android capabilities`() {
        val source: AndroidCapabilityRegistrationSource =
            DefaultAndroidCapabilityRegistrationSource()

        val registrations =
            source.registrations()

        assertEquals(
            3,
            registrations.size,
        )

        assertEquals(
            listOf(
                AndroidAccessibilityCapability.contract,
                AndroidDeviceKnowledgeCapability.contract,
                AndroidVisionCapability.contract,
            ),
            registrations,
        )
    }

    @Test
    fun `default source preserves stable registrations across repeated reads`() {
        val source =
            DefaultAndroidCapabilityRegistrationSource()

        val first =
            source.registrations()

        val second =
            source.registrations()

        assertEquals(
            listOf(
                AndroidAccessibilityCapability.contract,
                AndroidDeviceKnowledgeCapability.contract,
                AndroidVisionCapability.contract,
            ),
            first,
        )

        assertEquals(
            first,
            second,
        )
    }
}
