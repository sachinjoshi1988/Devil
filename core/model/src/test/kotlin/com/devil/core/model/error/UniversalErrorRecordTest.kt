package com.devil.core.model.error

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.TraceId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class UniversalErrorRecordTest {

    @Test
    fun `create preserves and normalizes error`() {
        val occurredAt = DevilTimestamp.fromEpochMilliseconds(1_754_000_002_000L)

        val record = UniversalErrorRecord.create(
            errorCode = ErrorCode.from("CONTEXT_INVALID"),
            traceId = TraceId.from("trace-error-001"),
            occurredAt = occurredAt,
            summary = "  Context validation failed.  ",
        )

        assertEquals("CONTEXT_INVALID", record.errorCode.value)
        assertEquals("trace-error-001", record.traceId.value)
        assertEquals(occurredAt, record.occurredAt)
        assertEquals("Context validation failed.", record.summary)
    }

    @Test
    fun `create rejects blank summary`() {
        assertFailsWith<IllegalArgumentException> {
            UniversalErrorRecord.create(
                errorCode = ErrorCode.from("CONTEXT_INVALID"),
                traceId = TraceId.from("trace-error-002"),
                occurredAt = DevilTimestamp.fromEpochMilliseconds(1_754_000_002_000L),
                summary = "   ",
            )
        }
    }
}
