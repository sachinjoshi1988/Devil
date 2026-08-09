package com.devil.core.model.conversation

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ConversationIdTest {

    @Test
    fun `from preserves a valid conversation identity`() {
        val conversationId =
            ConversationId.from(
                "conversation-001",
            )

        assertEquals(
            "conversation-001",
            conversationId.value,
        )
    }

    @Test
    fun `from trims surrounding whitespace`() {
        val conversationId =
            ConversationId.from(
                "  conversation-002  ",
            )

        assertEquals(
            "conversation-002",
            conversationId.value,
        )
    }

    @Test
    fun `from rejects a blank conversation identity`() {
        assertFailsWith<IllegalArgumentException> {
            ConversationId.from("   ")
        }
    }

    @Test
    fun `different conversation identities remain distinct`() {
        val first =
            ConversationId.from(
                "conversation-003",
            )
        val second =
            ConversationId.from(
                "conversation-004",
            )

        check(first != second)
    }
}
