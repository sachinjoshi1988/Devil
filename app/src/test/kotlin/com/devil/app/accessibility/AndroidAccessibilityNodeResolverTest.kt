package com.devil.app.accessibility

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AndroidAccessibilityNodeResolverTest {

    private val resolver =
        AndroidAccessibilityNodeResolver()

    @Test
    fun `text matching is case insensitive`() {
        val target =
            AndroidAccessibilityTarget.fromText(
                "Settings",
            )

        assertTrue(
            resolver.matchesText(
                candidateText = "SETTINGS",
                target = target,
            ),
        )
    }

    @Test
    fun `text matching normalizes repeated whitespace`() {
        val target =
            AndroidAccessibilityTarget.fromText(
                "Chaitu's Kitchen",
            )

        assertTrue(
            resolver.matchesText(
                candidateText =
                    "  Chaitu's   Kitchen ",
                target = target,
            ),
        )
    }

    @Test
    fun `different text does not match`() {
        val target =
            AndroidAccessibilityTarget.fromText(
                "Settings",
            )

        assertFalse(
            resolver.matchesText(
                candidateText = "Recent",
                target = target,
            ),
        )
    }

    @Test
    fun `blank candidate never matches`() {
        val target =
            AndroidAccessibilityTarget.fromText(
                "Settings",
            )

        assertFalse(
            resolver.matchesText(
                candidateText = "   ",
                target = target,
            ),
        )
    }
}
