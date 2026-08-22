package com.devil.app.background

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage193AndroidBackgroundOperationTest {

    @Test
    fun `operation identity is normalized`() {
        val request =
            AndroidBackgroundOperationRequest.create(
                "  sync-example  ",
            )

        assertEquals(
            "sync-example",
            request.operationId,
        )
    }

    @Test
    fun `blank operation identity is rejected`() {
        assertFailsWith<IllegalArgumentException> {
            AndroidBackgroundOperationRequest.create("   ")
        }
    }

    @Test
    fun `explicit request becomes ready with exact provenance`() {
        val request =
            AndroidBackgroundOperationRequest.create(
                "sync-example",
            )

        val result =
            AndroidBackgroundOperationCoordinator()
                .prepare(request)

        assertEquals(
            AndroidBackgroundOperationStatus.READY,
            result.status,
        )
        assertSame(request, result.request)
    }

    @Test
    fun `absent request remains deferred`() {
        val result =
            AndroidBackgroundOperationCoordinator()
                .prepare(null)

        assertEquals(
            AndroidBackgroundOperationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.request)
    }

    @Test
    fun `result state invariants are enforced`() {
        assertFailsWith<IllegalArgumentException> {
            AndroidBackgroundOperationResult.create(
                status = AndroidBackgroundOperationStatus.READY,
            )
        }

        val request =
            AndroidBackgroundOperationRequest.create(
                "sync-example",
            )

        assertFailsWith<IllegalArgumentException> {
            AndroidBackgroundOperationResult.create(
                status = AndroidBackgroundOperationStatus.DEFERRED,
                request = request,
            )
        }
    }
}
