package com.devil.app.accessibility

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class AndroidAccessibilityTargetTest {

    @Test
    fun `target preserves display text and normalizes matching text`() {
        val target =
            AndroidAccessibilityTarget.fromText(
                "  Chaitu's Kitchen  ",
            )

        assertEquals(
            "Chaitu's Kitchen",
            target.text,
        )

        assertEquals(
            "chaitu's kitchen",
            target.normalizedText,
        )
    }

    @Test
    fun `target normalization is case insensitive and whitespace stable`() {
        val first =
            AndroidAccessibilityTarget.fromText(
                "Open   Settings",
            )

        val second =
            AndroidAccessibilityTarget.fromText(
                " open settings ",
            )

        assertEquals(
            first.normalizedText,
            second.normalizedText,
        )
    }

    @Test
    fun `blank target is rejected`() {
        assertFailsWith<IllegalArgumentException> {
            AndroidAccessibilityTarget.fromText(
                "   ",
            )
        }
    }
}
