package com.devil.app.device

import com.devil.core.model.capability.CapabilityCategory
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AndroidDeviceKnowledgeCapabilityTest {

    @Test
    fun `device knowledge is registered conceptually as knowledge`() {
        val capability =
            AndroidDeviceKnowledgeCapability.contract

        assertEquals(
            CapabilityCategory.KNOWLEDGE,
            capability.category,
        )

        assertEquals(
            "android-device-knowledge",
            capability.capabilityId.value,
        )

        assertTrue(
            AndroidDeviceKnowledgeCapability.matches(
                capability,
            ),
        )
    }
}
