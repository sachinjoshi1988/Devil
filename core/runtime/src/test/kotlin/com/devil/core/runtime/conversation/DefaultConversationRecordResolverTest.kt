package com.devil.core.runtime.conversation

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.common.TraceId
import com.devil.core.model.context.ContextEnvelope
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel
import com.devil.core.model.conversation.ConversationId
import com.devil.core.model.conversation.ConversationInput
import com.devil.core.model.conversation.ConversationIntakeRecord
import com.devil.core.model.conversation.ConversationIntakeResult
import com.devil.core.model.conversation.ConversationIntakeState
import kotlin.test.Test
import kotlin.test.assertEquals

class DefaultConversationRecordResolverTest {

    @Test
    fun `create preserves conversation identity and intake result`() {
        val conversationId =
            ConversationId.from(
                "conversation-record-resolver-001",
            )
        val intake =
            createIntake(
                traceId =
                    TraceId.from(
                        "trace-conversation-record-resolver-001",
                    ),
                state = ConversationIntakeState.ACCEPTED,
            )
        val resolver: ConversationRecordResolver =
            DefaultConversationRecordResolver()

        val record =
            resolver.create(
                intake = intake,
                conversationId = conversationId,
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
    fun `create preserves deferred intake state without reinterpretation`() {
        val intake =
            createIntake(
                traceId =
                    TraceId.from(
                        "trace-conversation-record-resolver-002",
                    ),
                state = ConversationIntakeState.DEFERRED,
            )

        val record =
            DefaultConversationRecordResolver().create(
                intake = intake,
                conversationId =
                    ConversationId.from(
                        "conversation-record-resolver-002",
                    ),
            )

        assertEquals(
            ConversationIntakeState.DEFERRED,
            record.intake.record.state,
        )
    }

    @Test
    fun `create preserves rejected intake state without reinterpretation`() {
        val intake =
            createIntake(
                traceId =
                    TraceId.from(
                        "trace-conversation-record-resolver-003",
                    ),
                state = ConversationIntakeState.REJECTED,
            )

        val record =
            DefaultConversationRecordResolver().create(
                intake = intake,
                conversationId =
                    ConversationId.from(
                        "conversation-record-resolver-003",
                    ),
            )

        assertEquals(
            ConversationIntakeState.REJECTED,
            record.intake.record.state,
        )
    }

    @Test
    fun `conversation identity remains independent from intake trace identity`() {
        val traceId =
            TraceId.from(
                "trace-conversation-record-resolver-004",
            )
        val conversationId =
            ConversationId.from(
                "conversation-record-resolver-004",
            )

        val record =
            DefaultConversationRecordResolver().create(
                intake =
                    createIntake(
                        traceId = traceId,
                        state = ConversationIntakeState.ACCEPTED,
                    ),
                conversationId = conversationId,
            )

        assertEquals(
            traceId,
            record.intake.record.input.context.traceId,
        )
        assertEquals(
            conversationId,
            record.conversationId,
        )

        check(
            record.conversationId.value !=
                record.intake.record.input.context.traceId.value,
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
                                            1_754_000_202_000L,
                                        ),
                                ),
                            content =
                                "Bounded conversation record resolver test input.",
                        ),
                    state = state,
                    rationale =
                        "Preserve the supplied conversation intake state.",
                ),
        )
    }
}
