package com.devil.core.runtime.executive

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.TraceId
import com.devil.core.model.error.ErrorCode
import com.devil.core.model.error.UniversalErrorRecord
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class ExecutiveReadinessResultTest {

    @Test
    fun `create preserves ready result without error`() {
        val traceId = TraceId.from("trace-executive-readiness-001")

        val result = ExecutiveReadinessResult.create(
            traceId = traceId,
            status = ExecutiveReadinessStatus.READY,
        )

        assertEquals(traceId, result.traceId)
        assertEquals(ExecutiveReadinessStatus.READY, result.status)
        assertNull(result.error)
    }

    @Test
    fun `create preserves deferred result without error`() {
        val traceId = TraceId.from("trace-executive-readiness-002")

        val result = ExecutiveReadinessResult.create(
            traceId = traceId,
            status = ExecutiveReadinessStatus.DEFERRED,
        )

        assertEquals(traceId, result.traceId)
        assertEquals(ExecutiveReadinessStatus.DEFERRED, result.status)
        assertNull(result.error)
    }

    @Test
    fun `create preserves failed result with matching error`() {
        val traceId = TraceId.from("trace-executive-readiness-003")
        val error = createError(traceId)

        val result = ExecutiveReadinessResult.create(
            traceId = traceId,
            status = ExecutiveReadinessStatus.FAILED,
            error = error,
        )

        assertEquals(traceId, result.traceId)
        assertEquals(ExecutiveReadinessStatus.FAILED, result.status)
        assertEquals(error, result.error)
    }

    @Test
    fun `create rejects failed result without error`() {
        assertFailsWith<IllegalArgumentException> {
            ExecutiveReadinessResult.create(
                traceId = TraceId.from("trace-executive-readiness-004"),
                status = ExecutiveReadinessStatus.FAILED,
            )
        }
    }

    @Test
    fun `create rejects ready result with error`() {
        val traceId = TraceId.from("trace-executive-readiness-005")

        assertFailsWith<IllegalArgumentException> {
            ExecutiveReadinessResult.create(
                traceId = traceId,
                status = ExecutiveReadinessStatus.READY,
                error = createError(traceId),
            )
        }
    }

    @Test
    fun `create rejects deferred result with error`() {
        val traceId = TraceId.from("trace-executive-readiness-006")

        assertFailsWith<IllegalArgumentException> {
            ExecutiveReadinessResult.create(
                traceId = traceId,
                status = ExecutiveReadinessStatus.DEFERRED,
                error = createError(traceId),
            )
        }
    }

    @Test
    fun `create rejects error from a different trace`() {
        assertFailsWith<IllegalArgumentException> {
            ExecutiveReadinessResult.create(
                traceId = TraceId.from("trace-executive-readiness-007"),
                status = ExecutiveReadinessStatus.FAILED,
                error = createError(
                    TraceId.from("trace-executive-readiness-error-other"),
                ),
            )
        }
    }

    private fun createError(traceId: TraceId): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode = ErrorCode.from("EXECUTIVE_READINESS_FAILED"),
            traceId = traceId,
            occurredAt = DevilTimestamp.fromEpochMilliseconds(
                1_754_000_024_000L,
            ),
            summary = "Executive readiness evaluation failed.",
        )
    }
}
