package com.devil.core.model.common

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class TraceIdTest {

    @Test
    fun `from trims surrounding whitespace`() {
        val traceId = TraceId.from("  trace-001  ")

        assertEquals("trace-001", traceId.value)
    }

    @Test
    fun `from preserves a valid trace identity`() {
        val traceId = TraceId.from("trace-002")

        assertEquals("trace-002", traceId.value)
    }

    @Test
    fun `from rejects a blank trace identity`() {
        assertFailsWith<IllegalArgumentException> {
            TraceId.from("   ")
        }
    }
}
