package com.devil.app.device

import com.devil.app.capability.DefaultAndroidCapabilityAvailabilitySource
import com.devil.core.model.capability.CapabilityAvailabilityState
import kotlin.test.Test
import kotlin.test.assertEquals

class Stage40DeviceKnowledgeCapabilityAvailabilityTest {

    @Test
    fun `device knowledge capability is available from bounded Android platform source`() {
        val result =
            DefaultAndroidCapabilityAvailabilitySource()
                .availability(
                    AndroidDeviceKnowledgeCapability.contract,
                )

        assertEquals(
            CapabilityAvailabilityState.AVAILABLE,
            result,
        )
    }
}
