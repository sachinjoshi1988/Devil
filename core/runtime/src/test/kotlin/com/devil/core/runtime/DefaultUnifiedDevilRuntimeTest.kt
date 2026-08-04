package com.devil.core.runtime

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.common.TraceId
import com.devil.core.model.context.ContextEnvelope
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel
import com.devil.core.model.conversation.ConversationInput
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DefaultUnifiedDevilRuntimeTest {

    @Test
    fun `accept coordinates conversation input through one runtime path`() {
        val input = createInput()
        val runtime: UnifiedDevilRuntime =
            DefaultUnifiedDevilRuntime()

        val result = runtime.accept(input)

        assertEquals(
            input.context.traceId,
            result.traceId,
        )
        assertEquals(
            RuntimeStatus.DEFERRED,
            result.status,
        )
        assertNull(result.error)
    }

    private fun createInput(): ConversationInput {
        return ConversationInput.create(
            context = ContextEnvelope.create(
                traceId = TraceId.from(
                    "trace-runtime-conversation-001",
                ),
                schemaVersion = SchemaVersion.from(1),
                source = ContextSource.TEST,
                trustLevel = ContextTrustLevel.VERIFIED,
                securityLevel =
                    ContextSecurityLevel.RESTRICTED,
                observedAt =
                    DevilTimestamp.fromEpochMilliseconds(
                        1_754_000_064_000L,
                    ),
            ),
            content =
                "Please tell me the current phone status.",
        )
    }
}
