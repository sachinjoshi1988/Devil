package com.devil.app.internet

import com.devil.app.capability.DefaultAndroidCapabilityAvailabilitySource
import com.devil.core.model.capability.CapabilityAvailabilityState
import kotlin.test.Test
import kotlin.test.assertEquals

class Stage42InternetKnowledgeCapabilityAvailabilityTest {

    @Test
    fun `Internet Knowledge remains unavailable before real production network embodiment exists`() {
        val availability =
            DefaultAndroidCapabilityAvailabilitySource()
                .availability(
                    AndroidInternetKnowledgeCapability.contract,
                )

        assertEquals(
            CapabilityAvailabilityState.UNAVAILABLE,
            availability,
        )
    }
}
