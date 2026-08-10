package com.devil.app.vision

import com.devil.core.model.capability.CapabilityCategory
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AndroidVisionCapabilityTest {

    @Test
    fun `Stage 41 vision capability is bounded input capability`() {
        assertEquals(
            "android-vision-camera-perception",
            AndroidVisionCapability.capabilityId.value,
        )

        assertEquals(
            CapabilityCategory.INPUT,
            AndroidVisionCapability.contract.category,
        )

        assertTrue(
            AndroidVisionCapability.matches(
                AndroidVisionCapability.contract,
            ),
        )
    }
}
