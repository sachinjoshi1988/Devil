package com.devil.core.model.security

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class SessionIdTest {

    @Test
    fun `from preserves a valid session identity`() {
        val sessionId = SessionId.from(
            "session-owner-001",
        )

        assertEquals(
            "session-owner-001",
            sessionId.value,
        )
    }

    @Test
    fun `from trims surrounding whitespace`() {
        val sessionId = SessionId.from(
            "  session-owner-002  ",
        )

        assertEquals(
            "session-owner-002",
            sessionId.value,
        )
    }

    @Test
    fun `from rejects a blank session identity`() {
        assertFailsWith<IllegalArgumentException> {
            SessionId.from("   ")
        }
    }

    @Test
    fun `different session identities remain distinct`() {
        val first = SessionId.from(
            "session-owner-003",
        )
        val second = SessionId.from(
            "session-owner-004",
        )

        check(first != second)
    }
}
