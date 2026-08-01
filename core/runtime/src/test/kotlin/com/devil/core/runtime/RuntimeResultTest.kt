package com.devil.core.runtime

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.TraceId
import com.devil.core.model.error.ErrorCode
import com.devil.core.model.error.UniversalErrorRecord
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class RuntimeResultTest {

    @Test
    fun `create preserves accepted result without error`() {
        val traceId = TraceId.from("trace-runtime-001")

        val result = RuntimeResult.create(
            traceId = traceId,
            status = RuntimeStatus.ACCEPTED,
        )

        assertEquals(traceId, result.traceId)
        assertEquals(RuntimeStatus.ACCEPTED, result.status)
        assertNull(result.error)
    }

    @Test
    fun `create preserves rejected result with matching error`() {
        val traceId = TraceId.from("trace-runtime-002")
        val error = createError(traceId)

        val result = RuntimeResult.create(
            traceId = traceId,
            status = RuntimeStatus.REJECTED,
            error = error,
        )

        assertEquals(traceId, result.traceId)
        assertEquals(RuntimeStatus.REJECTED, result.status)
        assertEquals(error, result.error)
    }

    @Test
    fun `create rejects rejected result without error`() {
        assertFailsWith<IllegalArgumentException> {
            RuntimeResult.create(
                traceId = TraceId.from("trace-runtime-003"),
                status = RuntimeStatus.REJECTED,
            )
        }
    }

    @Test
    fun `create rejects accepted result with error`() {
        val traceId = TraceId.from("trace-runtime-004")

        assertFailsWith<IllegalArgumentException> {
            RuntimeResult.create(
                traceId = traceId,
                status = RuntimeStatus.ACCEPTED,
                error = createError(traceId),
            )
        }
    }

    @Test
    fun `create rejects error from a different trace`() {
        assertFailsWith<IllegalArgumentException> {
            RuntimeResult.create(
                traceId = TraceId.from("trace-runtime-005"),
                status = RuntimeStatus.REJECTED,
                error = createError(
                    TraceId.from("trace-runtime-other"),
                ),
            )
        }
    }

    private fun createError(traceId: TraceId): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode = ErrorCode.from("RUNTIME_REJECTED"),
            traceId = traceId,
            occurredAt = DevilTimestamp.fromEpochMilliseconds(1_754_000_003_000L),
            summary = "The runtime rejected the context.",
        )
    }
}
