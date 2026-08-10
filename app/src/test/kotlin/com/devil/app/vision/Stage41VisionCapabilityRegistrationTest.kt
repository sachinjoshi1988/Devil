package com.devil.app.vision

import com.devil.app.capability.DefaultAndroidCapabilityRegistrationSource
import com.devil.core.model.capability.CapabilityCategory
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class Stage41VisionCapabilityRegistrationTest {

    @Test
    fun `default Android registration source exposes Stage 41 Vision capability`() {
        val registrations =
            DefaultAndroidCapabilityRegistrationSource()
                .registrations()

        assertTrue(
            registrations.contains(
                AndroidVisionCapability.contract,
            ),
        )

        assertEquals(
            1,
            registrations.count {
                it.capabilityId ==
                    AndroidVisionCapability.capabilityId
            },
        )

        assertEquals(
            CapabilityCategory.INPUT,
            AndroidVisionCapability.contract.category,
        )
    }
}
