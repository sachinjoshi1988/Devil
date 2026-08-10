package com.devil.app.device

import com.devil.app.capability.DefaultAndroidCapabilityHealthSource
import com.devil.core.model.capability.CapabilityHealthState
import kotlin.test.Test
import kotlin.test.assertEquals

class Stage40DeviceKnowledgeCapabilityHealthTest {

    @Test
    fun `device knowledge capability health is ready for approved Build facts`() {
        val result =
            DefaultAndroidCapabilityHealthSource()
                .health(
                    AndroidDeviceKnowledgeCapability.contract,
                )

        assertEquals(
            CapabilityHealthState.READY,
            result,
        )
    }
}
