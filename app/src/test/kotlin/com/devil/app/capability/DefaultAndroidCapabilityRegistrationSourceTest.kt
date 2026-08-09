package com.devil.app.capability

import kotlin.test.Test
import kotlin.test.assertEquals

class DefaultAndroidCapabilityRegistrationSourceTest {

    @Test
    fun `default source does not fabricate Android capability registrations`() {
        val source: AndroidCapabilityRegistrationSource =
            DefaultAndroidCapabilityRegistrationSource()

        assertEquals(
            emptyList(),
            source.registrations(),
        )
    }

    @Test
    fun `default source remains empty across repeated reads`() {
        val source =
            DefaultAndroidCapabilityRegistrationSource()

        assertEquals(
            emptyList(),
            source.registrations(),
        )
        assertEquals(
            emptyList(),
            source.registrations(),
        )
    }
}
