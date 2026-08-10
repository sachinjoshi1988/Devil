package com.devil.app.internet

import com.devil.app.capability.DefaultAndroidCapabilityHealthSource
import com.devil.core.model.capability.CapabilityHealthState
import kotlin.test.Test
import kotlin.test.assertEquals

class Stage42InternetKnowledgeCapabilityHealthTest {

    @Test
    fun `Internet Knowledge remains unavailable health before real network source exists`() {
        val health =
            DefaultAndroidCapabilityHealthSource()
                .health(
                    AndroidInternetKnowledgeCapability.contract,
                )

        assertEquals(
            CapabilityHealthState.UNAVAILABLE,
            health,
        )
    }
}
