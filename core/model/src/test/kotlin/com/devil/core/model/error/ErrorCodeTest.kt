package com.devil.core.model.error

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ErrorCodeTest {

    @Test
    fun `from trims surrounding whitespace`() {
        val errorCode = ErrorCode.from("  CONTEXT_INVALID  ")

        assertEquals("CONTEXT_INVALID", errorCode.value)
    }

    @Test
    fun `from preserves a valid error code`() {
        val errorCode = ErrorCode.from("RUNTIME_NOT_EXECUTED")

        assertEquals("RUNTIME_NOT_EXECUTED", errorCode.value)
    }

    @Test
    fun `from rejects a blank error code`() {
        assertFailsWith<IllegalArgumentException> {
            ErrorCode.from("   ")
        }
    }
}
