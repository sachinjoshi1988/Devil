package com.devil.app.internet

import com.devil.core.model.capability.CapabilityCategory
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AndroidInternetKnowledgeCapabilityTest {

    @Test
    fun `Stage 42 Internet Knowledge is one bounded knowledge capability`() {
        val capability =
            AndroidInternetKnowledgeCapability.contract

        assertEquals(
            "android-internet-knowledge",
            capability.capabilityId.value,
        )

        assertEquals(
            CapabilityCategory.KNOWLEDGE,
            capability.category,
        )

        assertTrue(
            AndroidInternetKnowledgeCapability.matches(
                capability,
            ),
        )
    }
}
