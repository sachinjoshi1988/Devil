package com.devil.app.runtime

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

class DefaultAndroidConversationInputAdapterTest {

    @Test
    fun `adapt preserves authoritative context and normalizes textual content`() {
        val context = createContext()
        val adapter: AndroidConversationInputAdapter =
            DefaultAndroidConversationInputAdapter()

        val input = adapter.adapt(
            context = context,
            content = "   Hello Devil   ",
        )

        assertEquals(context, input.context)
        assertEquals("Hello Devil", input.content)
    }

    @Test
    fun `adapt does not alter constitutional context classification`() {
        val context = createContext()

        val input = DefaultAndroidConversationInputAdapter().adapt(
            context = context,
            content = "Show current status",
        )

        assertEquals(
            ContextSource.TEXT,
            input.context.source,
        )
        assertEquals(
            ContextTrustLevel.UNVERIFIED,
            input.context.trustLevel,
        )
        assertEquals(
            ContextSecurityLevel.RESTRICTED,
            input.context.securityLevel,
        )
        assertEquals(
            "trace-android-conversation-input-001",
            input.context.traceId.value,
        )
        assertEquals(
            1_754_000_187_000L,
            input.context.observedAt.epochMilliseconds,
        )
    }

    @Test
    fun `adapt preserves existing conversation input validation`() {
        assertFailsWith<IllegalArgumentException> {
            DefaultAndroidConversationInputAdapter().adapt(
                context = createContext(),
                content = "   ",
            )
        }
    }

    private fun createContext(): ContextEnvelope {
        return ContextEnvelope.create(
            traceId = TraceId.from(
                "trace-android-conversation-input-001",
            ),
            schemaVersion = SchemaVersion.from(1),
            source = ContextSource.TEXT,
            trustLevel = ContextTrustLevel.UNVERIFIED,
            securityLevel = ContextSecurityLevel.RESTRICTED,
            observedAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_187_000L,
                ),
        )
    }
}
