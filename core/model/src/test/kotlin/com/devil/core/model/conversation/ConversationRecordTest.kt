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

class ConversationRecordTest {

    @Test
    fun `create preserves conversation identity and intake result`() {
        val conversationId =
            ConversationId.from(
                "conversation-record-001",
            )
        val intake = createIntake(
            traceId =
                TraceId.from(
                    "trace-conversation-record-001",
                ),
            state = ConversationIntakeState.ACCEPTED,
        )

        val record =
            ConversationRecord.create(
                conversationId = conversationId,
                intake = intake,
            )

        assertEquals(
            conversationId,
            record.conversationId,
        )
        assertEquals(
            intake,
            record.intake,
        )
    }

    @Test
    fun `conversation identity remains distinct from runtime trace identity`() {
        val conversationId =
            ConversationId.from(
                "conversation-record-002",
            )
        val traceId =
            TraceId.from(
                "trace-conversation-record-002",
            )

        val record =
            ConversationRecord.create(
                conversationId = conversationId,
                intake =
                    createIntake(
                        traceId = traceId,
                        state = ConversationIntakeState.DEFERRED,
                    ),
            )

        assertEquals(
            "conversation-record-002",
            record.conversationId.value,
        )
        assertEquals(
            traceId,
            record.intake.record.input.context.traceId,
        )

        check(
            record.conversationId.value !=
                record.intake.record.input.context.traceId.value,
        )
    }

    @Test
    fun `record preserves rejected intake without converting it to persistence failure`() {
        val intake =
            createIntake(
                traceId =
                    TraceId.from(
                        "trace-conversation-record-003",
                    ),
                state = ConversationIntakeState.REJECTED,
            )

        val record =
            ConversationRecord.create(
                conversationId =
                    ConversationId.from(
                        "conversation-record-003",
                    ),
                intake = intake,
            )

        assertEquals(
            ConversationIntakeState.REJECTED,
            record.intake.record.state,
        )
    }

    private fun createIntake(
        traceId: TraceId,
        state: ConversationIntakeState,
    ): ConversationIntakeResult {
        return ConversationIntakeResult.create(
            record =
                ConversationIntakeRecord.create(
                    input =
                        ConversationInput.create(
                            context =
                                ContextEnvelope.create(
                                    traceId = traceId,
                                    schemaVersion =
                                        SchemaVersion.from(1),
                                    source = ContextSource.TEXT,
                                    trustLevel =
                                        ContextTrustLevel.VERIFIED,
                                    securityLevel =
                                        ContextSecurityLevel.RESTRICTED,
                                    observedAt =
                                        DevilTimestamp.fromEpochMilliseconds(
                                            1_754_000_201_000L,
                                        ),
                                ),
                            content =
                                "Bounded conversation record test input.",
                        ),
                    state = state,
                    rationale =
                        "Preserve the supplied conversation intake state.",
                ),
        )
    }
}
