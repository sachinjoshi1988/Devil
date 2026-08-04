package com.devil.core.model.conversation

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.common.TraceId
import com.devil.core.model.context.ContextEnvelope
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ConversationInputTest {

    @Test
    fun `create preserves context and normalizes textual content`() {
        val context = createContext()

        val input = ConversationInput.create(
            context = context,
            content = "  Please call my friend.  ",
        )

        assertEquals(context, input.context)
        assertEquals(
            "Please call my friend.",
            input.content,
        )
    }

    @Test
    fun `create rejects blank textual content`() {
        assertFailsWith<IllegalArgumentException> {
            ConversationInput.create(
                context = createContext(),
                content = "   ",
            )
        }
    }

    @Test
    fun `create does not alter constitutional context classification`() {
        val context = createContext(
            source = ContextSource.SYSTEM,
            trustLevel = ContextTrustLevel.UNVERIFIED,
            securityLevel = ContextSecurityLevel.RESTRICTED,
        )

        val input = ConversationInput.create(
            context = context,
            content = "System supplied conversation input.",
        )

        assertEquals(ContextSource.SYSTEM, input.context.source)
        assertEquals(
            ContextTrustLevel.UNVERIFIED,
            input.context.trustLevel,
        )
        assertEquals(
            ContextSecurityLevel.RESTRICTED,
            input.context.securityLevel,
        )
    }

    private fun createContext(
        source: ContextSource = ContextSource.TEXT,
        trustLevel: ContextTrustLevel =
            ContextTrustLevel.VERIFIED,
        securityLevel: ContextSecurityLevel =
            ContextSecurityLevel.RESTRICTED,
    ): ContextEnvelope {
        return ContextEnvelope.create(
            traceId = TraceId.from(
                "trace-conversation-input-001",
            ),
            schemaVersion = SchemaVersion.from(1),
            source = source,
            trustLevel = trustLevel,
            securityLevel = securityLevel,
            observedAt = DevilTimestamp.fromEpochMilliseconds(
                1_754_000_058_000L,
            ),
        )
    }
}
