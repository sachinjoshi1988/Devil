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

class ConversationIntakeResultTest {

    @Test
    fun `create preserves supplied intake record`() {
        val record = createRecord(
            state = ConversationIntakeState.ACCEPTED,
        )

        val result = ConversationIntakeResult.create(
            record = record,
        )

        assertEquals(record, result.record)
    }

    @Test
    fun `create preserves accepted intake state`() {
        val result = ConversationIntakeResult.create(
            record = createRecord(
                state = ConversationIntakeState.ACCEPTED,
            ),
        )

        assertEquals(
            ConversationIntakeState.ACCEPTED,
            result.record.state,
        )
    }

    @Test
    fun `create preserves deferred intake state`() {
        val result = ConversationIntakeResult.create(
            record = createRecord(
                state = ConversationIntakeState.DEFERRED,
            ),
        )

        assertEquals(
            ConversationIntakeState.DEFERRED,
            result.record.state,
        )
    }

    @Test
    fun `create preserves rejected intake state`() {
        val result = ConversationIntakeResult.create(
            record = createRecord(
                state = ConversationIntakeState.REJECTED,
            ),
        )

        assertEquals(
            ConversationIntakeState.REJECTED,
            result.record.state,
        )
    }

    private fun createRecord(
        state: ConversationIntakeState,
    ): ConversationIntakeRecord {
        return ConversationIntakeRecord.create(
            input = ConversationInput.create(
                context = ContextEnvelope.create(
                    traceId = TraceId.from(
                        "trace-conversation-intake-result-001",
                    ),
                    schemaVersion = SchemaVersion.from(1),
                    source = ContextSource.TEXT,
                    trustLevel = ContextTrustLevel.VERIFIED,
                    securityLevel = ContextSecurityLevel.RESTRICTED,
                    observedAt = DevilTimestamp.fromEpochMilliseconds(
                        1_754_000_060_000L,
                    ),
                ),
                content = "Please tell me the current phone status.",
            ),
            state = state,
            rationale = "Bounded conversation intake result.",
        )
    }
}
