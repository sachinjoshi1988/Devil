package com.devil.app.device

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

/**
 * Stage 296 direct unit coverage for the existing Stage 40
 * AndroidDeviceKnowledgeResult contract.
 *
 * This test surface validates existing factory invariants only.
 *
 * Stage 296 does not modify Device Knowledge production behavior,
 * establish authentication or authorization, create Memory,
 * perform execution, establish Verification or Outcome,
 * or implement Stage 297 Integration Test Completion.
 */
class Stage296AndroidDeviceKnowledgeResultTest {

    @Test
    fun `result preserves exact query type and snapshot while normalizing presentation`() {
        val snapshot = snapshot()

        val result =
            AndroidDeviceKnowledgeResult.create(
                queryType = AndroidDeviceKnowledgeQueryType.DEVICE_SUMMARY,
                snapshot = snapshot,
                presentation = "  Android 14 test device.  ",
            )

        assertEquals(
            AndroidDeviceKnowledgeQueryType.DEVICE_SUMMARY,
            result.queryType,
        )
        assertSame(
            snapshot,
            result.snapshot,
        )
        assertEquals(
            "Android 14 test device.",
            result.presentation,
        )
    }

    @Test
    fun `result rejects blank presentation`() {
        val snapshot = snapshot()

        assertFailsWith<IllegalArgumentException> {
            AndroidDeviceKnowledgeResult.create(
                queryType = AndroidDeviceKnowledgeQueryType.ANDROID_VERSION,
                snapshot = snapshot,
                presentation = "   ",
            )
        }
    }

    private fun snapshot(): AndroidDeviceKnowledgeSnapshot =
        AndroidDeviceKnowledgeSnapshot.create(
            sdkInt = 34,
            androidRelease = "14",
            manufacturer = "Test Manufacturer",
            model = "Test Model",
            device = "test-device",
            product = "test-product",
        )
}
