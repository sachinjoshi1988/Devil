package com.devil.app.capability

import com.devil.app.accessibility.AndroidAccessibilityCapability
import kotlin.test.Test
import kotlin.test.assertEquals

class DefaultAndroidCapabilityRegistrationSourceTest {

    @Test
    fun `default source registers Stage 38 accessibility capability`() {
        val source: AndroidCapabilityRegistrationSource =
            DefaultAndroidCapabilityRegistrationSource()

        val registrations =
            source.registrations()

        assertEquals(
            1,
            registrations.size,
        )
        assertEquals(
            AndroidAccessibilityCapability.contract,
            registrations.single(),
        )
    }

    @Test
    fun `default source preserves stable accessibility registration across repeated reads`() {
        val source =
            DefaultAndroidCapabilityRegistrationSource()

        val first =
            source.registrations()

        val second =
            source.registrations()

        assertEquals(
            listOf(
                AndroidAccessibilityCapability.contract,
            ),
            first,
        )
        assertEquals(
            first,
            second,
        )
    }
}
