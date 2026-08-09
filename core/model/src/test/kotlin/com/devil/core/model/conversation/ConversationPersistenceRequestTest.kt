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

class ConversationPersistenceRequestTest {

    @Test
    fun `create preserves one bounded conversation record`() {
        val record = createRecord(
            conversationValue =
                "conversation-persistence-request-001",
            traceValue =
                "trace-conversation-persistence-request-001",
            state = ConversationIntakeState.ACCEPTED,
        )

        val request =
            ConversationPersistenceRequest.create(
                record = record,
            )

        assertEquals(
            record,
            request.record,
        )
        assertEquals(
            record.conversationId,
            request.record.conversationId,
        )
        assertEquals(
            record.intake,
            request.record.intake,
        )
    }

    @Test
    fun `create preserves conversation identity independently from runtime trace identity`() {
        val record = createRecord(
            conversationValue =
                "conversation-persistence-request-002",
            traceValue =
                "trace-conversation-persistence-request-002",
            state = ConversationIntakeState.ACCEPTED,
        )

        val request =
            ConversationPersistenceRequest.create(
                record = record,
            )

        assertEquals(
            "conversation-persistence-request-002",
            request.record.conversationId.value,
        )
        assertEquals(
            "trace-conversation-persistence-request-002",
            request.record.intake.record.input.context.traceId.value,
        )

        check(
            request.record.conversationId.value !=
                request.record.intake.record.input.context.traceId.value,
        )
    }

    @Test
    fun `create preserves deferred intake without converting it to persistence failure`() {
        val request =
            ConversationPersistenceRequest.create(
                record =
                    createRecord(
                        conversationValue =
                            "conversation-persistence-request-003",
                        traceValue =
                            "trace-conversation-persistence-request-003",
                        state = ConversationIntakeState.DEFERRED,
                    ),
            )

        assertEquals(
            ConversationIntakeState.DEFERRED,
            request.record.intake.record.state,
        )
    }

    @Test
    fun `create preserves rejected intake without granting constitutional continuation`() {
        val request =
            ConversationPersistenceRequest.create(
                record =
                    createRecord(
                        conversationValue =
                            "conversation-persistence-request-004",
                        traceValue =
                            "trace-conversation-persistence-request-004",
                        state = ConversationIntakeState.REJECTED,
                    ),
            )

        assertEquals(
            ConversationIntakeState.REJECTED,
            request.record.intake.record.state,
        )
    }

    private fun createRecord(
        conversationValue: String,
        traceValue: String,
        state: ConversationIntakeState,
    ): ConversationRecord {
        return ConversationRecord.create(
            conversationId =
                ConversationId.from(
                    conversationValue,
                ),
            intake =
                ConversationIntakeResult.create(
                    record =
                        ConversationIntakeRecord.create(
                            input =
                                ConversationInput.create(
                                    context =
                                        ContextEnvelope.create(
                                            traceId =
                                                TraceId.from(
                                                    traceValue,
                                                ),
                                            schemaVersion =
                                                SchemaVersion.from(1),
                                            source =
                                                ContextSource.TEXT,
                                            trustLevel =
                                                ContextTrustLevel.VERIFIED,
                                            securityLevel =
                                                ContextSecurityLevel.RESTRICTED,
                                            observedAt =
                                                DevilTimestamp
                                                    .fromEpochMilliseconds(
                                                        1_754_000_207_000L,
                                                    ),
                                        ),
                                    content =
                                        "Bounded conversation persistence request test input.",
                                ),
                            state = state,
                            rationale =
                                "Preserve the established conversation intake state.",
                        ),
                ),
        )
    }
}
