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

class ConversationRecordRequestTest {

    @Test
    fun `create preserves accepted conversation intake`() {
        val intake =
            createIntake(
                state = ConversationIntakeState.ACCEPTED,
            )

        val request =
            ConversationRecordRequest.create(
                intake = intake,
            )

        assertEquals(
            intake,
            request.intake,
        )
        assertEquals(
            ConversationIntakeState.ACCEPTED,
            request.intake.record.state,
        )
    }

    @Test
    fun `create preserves deferred conversation intake without granting continuation`() {
        val request =
            ConversationRecordRequest.create(
                intake =
                    createIntake(
                        state = ConversationIntakeState.DEFERRED,
                    ),
            )

        assertEquals(
            ConversationIntakeState.DEFERRED,
            request.intake.record.state,
        )
    }

    @Test
    fun `create preserves rejected conversation intake without converting it to persistence failure`() {
        val request =
            ConversationRecordRequest.create(
                intake =
                    createIntake(
                        state = ConversationIntakeState.REJECTED,
                    ),
            )

        assertEquals(
            ConversationIntakeState.REJECTED,
            request.intake.record.state,
        )
    }

    @Test
    fun `request preserves constitutional trace without becoming conversation identity`() {
        val traceId =
            TraceId.from(
                "trace-conversation-record-request-001",
            )

        val request =
            ConversationRecordRequest.create(
                intake =
                    createIntake(
                        traceId = traceId,
                        state = ConversationIntakeState.ACCEPTED,
                    ),
            )

        assertEquals(
            traceId,
            request.intake.record.input.context.traceId,
        )
    }

    private fun createIntake(
        traceId: TraceId =
            TraceId.from(
                "trace-conversation-record-request-default",
            ),
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
                                            1_754_000_204_000L,
                                        ),
                                ),
                            content =
                                "Bounded conversation record request test input.",
                        ),
                    state = state,
                    rationale =
                        "Preserve the established conversation intake state.",
                ),
        )
    }
}
