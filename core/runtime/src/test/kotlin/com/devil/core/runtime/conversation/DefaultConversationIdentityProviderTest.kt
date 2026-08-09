package com.devil.core.runtime.conversation

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
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class DefaultConversationIdentityProviderTest {

    @Test
    fun `provide returns unavailable without fabricating conversation identity`() {
        val traceId =
            TraceId.from(
                "trace-default-conversation-identity-provider-001",
            )
        val provider: ConversationIdentityProvider =
            DefaultConversationIdentityProvider()

        val result =
            provider.provide(
                traceId = traceId,
                input = createInput(traceId),
            )

        assertEquals(traceId, result.traceId)
        assertEquals(
            ConversationIdentityProvisionStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.conversationId)
        assertNull(result.error)
    }

    @Test
    fun `provide remains unavailable for valid conversation input`() {
        val traceId =
            TraceId.from(
                "trace-default-conversation-identity-provider-002",
            )

        val result =
            DefaultConversationIdentityProvider().provide(
                traceId = traceId,
                input = createInput(traceId),
            )

        assertEquals(
            ConversationIdentityProvisionStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.conversationId)
    }

    @Test
    fun `provide rejects input from a different trace`() {
        assertFailsWith<IllegalArgumentException> {
            DefaultConversationIdentityProvider().provide(
                traceId =
                    TraceId.from(
                        "trace-default-conversation-identity-provider-003",
                    ),
                input =
                    createInput(
                        TraceId.from(
                            "trace-default-conversation-identity-input-other",
                        ),
                    ),
            )
        }
    }

    private fun createInput(
        traceId: TraceId,
    ): ConversationInput {
        return ConversationInput.create(
            context =
                ContextEnvelope.create(
                    traceId = traceId,
                    schemaVersion = SchemaVersion.from(1),
                    source = ContextSource.TEXT,
                    trustLevel =
                        ContextTrustLevel.VERIFIED,
                    securityLevel =
                        ContextSecurityLevel.RESTRICTED,
                    observedAt =
                        DevilTimestamp.fromEpochMilliseconds(
                            1_754_000_200_500L,
                        ),
                ),
            content =
                "Bounded conversation identity test input.",
        )
    }
}
