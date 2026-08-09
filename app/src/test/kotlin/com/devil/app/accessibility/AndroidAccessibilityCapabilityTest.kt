package com.devil.app.accessibility

import com.devil.core.model.capability.CapabilityCategory
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AndroidAccessibilityCapabilityTest {

    @Test
    fun `accessibility capability has stable action identity`() {
        val capability =
            AndroidAccessibilityCapability.contract

        assertEquals(
            "android-accessibility-click-visible-text",
            capability.capabilityId.value,
        )

        assertEquals(
            CapabilityCategory.ACTION,
            capability.category,
        )

        assertTrue(
            AndroidAccessibilityCapability.matches(
                capability,
            ),
        )
    }

    @Test
    fun `capability contract does not contain a dynamic accessibility target`() {
        val capability =
            AndroidAccessibilityCapability.contract

        assertEquals(
            "Android Accessibility Click Visible Text",
            capability.name,
        )

        assertTrue(
            !capability.description.contains(
                "Settings",
                ignoreCase = true,
            ),
        )
    }
}
