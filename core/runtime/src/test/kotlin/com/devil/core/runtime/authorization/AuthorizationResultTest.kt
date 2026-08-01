package com.devil.core.runtime.authorization

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.TraceId
import com.devil.core.model.error.ErrorCode
import com.devil.core.model.error.UniversalErrorRecord
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class AuthorizationResultTest {

    @Test
    fun `create preserves authorized result without error`() {
        val traceId = TraceId.from("trace-authorization-001")

        val result = AuthorizationResult.create(
            traceId = traceId,
            status = AuthorizationStatus.AUTHORIZED,
        )

        assertEquals(traceId, result.traceId)
        assertEquals(AuthorizationStatus.AUTHORIZED, result.status)
        assertNull(result.error)
    }

    @Test
    fun `create preserves denied result without error`() {
        val traceId = TraceId.from("trace-authorization-002")

        val result = AuthorizationResult.create(
            traceId = traceId,
            status = AuthorizationStatus.DENIED,
        )

        assertEquals(traceId, result.traceId)
        assertEquals(AuthorizationStatus.DENIED, result.status)
        assertNull(result.error)
    }

    @Test
    fun `create preserves deferred result without error`() {
        val traceId = TraceId.from("trace-authorization-003")

        val result = AuthorizationResult.create(
            traceId = traceId,
            status = AuthorizationStatus.DEFERRED,
        )

        assertEquals(traceId, result.traceId)
        assertEquals(AuthorizationStatus.DEFERRED, result.status)
        assertNull(result.error)
    }

    @Test
    fun `create preserves failed result with matching error`() {
        val traceId = TraceId.from("trace-authorization-004")
        val error = createError(traceId)

        val result = AuthorizationResult.create(
            traceId = traceId,
            status = AuthorizationStatus.FAILED,
            error = error,
        )

        assertEquals(traceId, result.traceId)
        assertEquals(AuthorizationStatus.FAILED, result.status)
        assertEquals(error, result.error)
    }

    @Test
    fun `create rejects failed result without error`() {
        assertFailsWith<IllegalArgumentException> {
            AuthorizationResult.create(
                traceId = TraceId.from("trace-authorization-005"),
                status = AuthorizationStatus.FAILED,
            )
        }
    }

    @Test
    fun `create rejects non-failed result with error`() {
        val traceId = TraceId.from("trace-authorization-006")

        assertFailsWith<IllegalArgumentException> {
            AuthorizationResult.create(
                traceId = traceId,
                status = AuthorizationStatus.AUTHORIZED,
                error = createError(traceId),
            )
        }
    }

    @Test
    fun `create rejects error from a different trace`() {
        assertFailsWith<IllegalArgumentException> {
            AuthorizationResult.create(
                traceId = TraceId.from("trace-authorization-007"),
                status = AuthorizationStatus.FAILED,
                error = createError(
                    TraceId.from("trace-authorization-other"),
                ),
            )
        }
    }

    private fun createError(traceId: TraceId): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode = ErrorCode.from("AUTHORIZATION_EVALUATION_FAILED"),
            traceId = traceId,
            occurredAt = DevilTimestamp.fromEpochMilliseconds(1_754_000_011_000L),
            summary = "Authorization evaluation failed.",
        )
    }
}
