package com.devil.app.sharing

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class Stage189ClipboardSharingTest {

    @Test
    fun `explicit text becomes normalized ready payload`() {
        val result =
            AndroidSharingAssistanceCoordinator()
                .prepare("  Hello Alice  ")

        assertEquals(AndroidSharingAssistanceStatus.READY, result.status)
        assertEquals("Hello Alice", result.payload?.text)
    }

    @Test
    fun `absent text remains deferred`() {
        val result =
            AndroidSharingAssistanceCoordinator()
                .prepare(null)

        assertEquals(AndroidSharingAssistanceStatus.DEFERRED, result.status)
        assertNull(result.payload)
    }

    @Test
    fun `blank explicitly supplied text is rejected`() {
        assertFailsWith<IllegalArgumentException> {
            AndroidSharingAssistanceCoordinator()
                .prepare("   ")
        }
    }

    @Test
    fun `ready result requires payload`() {
        assertFailsWith<IllegalArgumentException> {
            AndroidSharingAssistanceResult.create(
                status = AndroidSharingAssistanceStatus.READY,
            )
        }
    }

    @Test
    fun `deferred result rejects payload`() {
        val payload =
            AndroidSharingPayload.create(
                text = "Hello",
            )

        assertFailsWith<IllegalArgumentException> {
            AndroidSharingAssistanceResult.create(
                status = AndroidSharingAssistanceStatus.DEFERRED,
                payload = payload,
            )
        }
    }
}
