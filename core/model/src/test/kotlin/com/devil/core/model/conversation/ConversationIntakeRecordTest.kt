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

class ConversationIntakeRecordTest {

    @Test
    fun `create preserves input state and normalized rationale`() {
        val input = createInput()

        val record = ConversationIntakeRecord.create(
            input = input,
            state = ConversationIntakeState.ACCEPTED,
            rationale = "  Input satisfied bounded intake requirements.  ",
        )

        assertEquals(input, record.input)
        assertEquals(
            ConversationIntakeState.ACCEPTED,
            record.state,
        )
        assertEquals(
            "Input satisfied bounded intake requirements.",
            record.rationale,
        )
    }

    @Test
    fun `create preserves deferred intake without implying understanding`() {
        val record = ConversationIntakeRecord.create(
            input = createInput(),
            state = ConversationIntakeState.DEFERRED,
            rationale = "Conversation intake cannot continue yet.",
        )

        assertEquals(
            ConversationIntakeState.DEFERRED,
            record.state,
        )
    }

    @Test
    fun `create preserves rejected intake`() {
        val record = ConversationIntakeRecord.create(
            input = createInput(),
            state = ConversationIntakeState.REJECTED,
            rationale = "Input was rejected by bounded intake policy.",
        )

        assertEquals(
            ConversationIntakeState.REJECTED,
            record.state,
        )
    }

    @Test
    fun `create rejects blank intake rationale`() {
        assertFailsWith<IllegalArgumentException> {
            ConversationIntakeRecord.create(
                input = createInput(),
                state = ConversationIntakeState.DEFERRED,
                rationale = "   ",
            )
        }
    }

    private fun createInput(): ConversationInput {
        return ConversationInput.create(
            context = ContextEnvelope.create(
                traceId = TraceId.from(
                    "trace-conversation-intake-record-001",
                ),
                schemaVersion = SchemaVersion.from(1),
                source = ContextSource.TEXT,
                trustLevel = ContextTrustLevel.VERIFIED,
                securityLevel = ContextSecurityLevel.RESTRICTED,
                observedAt = DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_059_000L,
                ),
            ),
            content = "Please read my latest message.",
        )
    }
}
