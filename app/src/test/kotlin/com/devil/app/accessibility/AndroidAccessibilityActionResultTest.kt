package com.devil.app.accessibility

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class AndroidAccessibilityActionResultTest {

    @Test
    fun `attempted contains no success evidence`() {
        val result =
            AndroidAccessibilityActionResult.attempted()

        assertEquals(
            AndroidAccessibilityActionStatus.ATTEMPTED,
            result.status,
        )

        assertNull(result.errorCode)
    }

    @Test
    fun `target not found contains no fabricated error`() {
        val result =
            AndroidAccessibilityActionResult.targetNotFound()

        assertEquals(
            AndroidAccessibilityActionStatus.TARGET_NOT_FOUND,
            result.status,
        )

        assertNull(result.errorCode)
    }

    @Test
    fun `service unavailable remains distinct from target not found`() {
        val result =
            AndroidAccessibilityActionResult.serviceUnavailable()

        assertEquals(
            AndroidAccessibilityActionStatus.SERVICE_UNAVAILABLE,
            result.status,
        )

        assertNull(result.errorCode)
    }

    @Test
    fun `failed result preserves normalized operational error`() {
        val result =
            AndroidAccessibilityActionResult.failed(
                "  ANDROID_ACCESSIBILITY_FAILURE  ",
            )

        assertEquals(
            AndroidAccessibilityActionStatus.FAILED,
            result.status,
        )

        assertEquals(
            "ANDROID_ACCESSIBILITY_FAILURE",
            result.errorCode,
        )
    }

    @Test
    fun `blank failure code is rejected`() {
        assertFailsWith<IllegalArgumentException> {
            AndroidAccessibilityActionResult.failed(
                "   ",
            )
        }
    }
}
