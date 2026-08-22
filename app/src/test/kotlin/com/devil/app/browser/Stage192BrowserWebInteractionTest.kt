package com.devil.app.browser

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage192BrowserWebInteractionTest {

    @Test
    fun `explicit URI is normalized`() {
        val request =
            AndroidBrowserInteractionRequest.create(
                "  https://example.com/path  ",
            )

        assertEquals(
            "https://example.com/path",
            request.uri,
        )
    }

    @Test
    fun `blank URI is rejected`() {
        assertFailsWith<IllegalArgumentException> {
            AndroidBrowserInteractionRequest.create("   ")
        }
    }

    @Test
    fun `explicit browser request becomes ready with exact provenance`() {
        val request =
            AndroidBrowserInteractionRequest.create(
                "https://example.com",
            )

        val result =
            AndroidBrowserInteractionCoordinator()
                .prepare(request)

        assertEquals(
            AndroidBrowserInteractionStatus.READY,
            result.status,
        )
        assertSame(request, result.request)
    }

    @Test
    fun `absent browser request remains deferred`() {
        val result =
            AndroidBrowserInteractionCoordinator()
                .prepare(null)

        assertEquals(
            AndroidBrowserInteractionStatus.DEFERRED,
            result.status,
        )
        assertNull(result.request)
    }

    @Test
    fun `result state invariants are enforced`() {
        assertFailsWith<IllegalArgumentException> {
            AndroidBrowserInteractionResult.create(
                status = AndroidBrowserInteractionStatus.READY,
            )
        }

        val request =
            AndroidBrowserInteractionRequest.create(
                "https://example.com",
            )

        assertFailsWith<IllegalArgumentException> {
            AndroidBrowserInteractionResult.create(
                status = AndroidBrowserInteractionStatus.DEFERRED,
                request = request,
            )
        }
    }
}
