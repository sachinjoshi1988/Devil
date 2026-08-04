package com.devil.core.model.understanding

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.common.TraceId
import com.devil.core.model.context.ContextEnvelope
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel
import com.devil.core.model.conversation.ConversationInput
import com.devil.core.model.conversation.ConversationIntakeRecord
import com.devil.core.model.conversation.ConversationIntakeResult
import com.devil.core.model.conversation.ConversationIntakeState
import kotlin.test.Test
import kotlin.test.assertEquals

class UnderstandingEvaluationRequestTest {

    @Test
    fun `create preserves completed conversation intake`() {
        val intake = createIntake(
            state = ConversationIntakeState.ACCEPTED,
        )

        val request = UnderstandingEvaluationRequest.create(
            conversationIntake = intake,
        )

        assertEquals(intake, request.conversationIntake)
        assertEquals(
            intake.record.input.context,
            request.conversationIntake.record.input.context,
        )
        assertEquals(
            intake.record.input.content,
            request.conversationIntake.record.input.content,
        )
    }

    @Test
    fun `create preserves deferred intake without inventing understanding`() {
        val request = UnderstandingEvaluationRequest.create(
            conversationIntake = createIntake(
                state = ConversationIntakeState.DEFERRED,
            ),
        )

        assertEquals(
            ConversationIntakeState.DEFERRED,
            request.conversationIntake.record.state,
        )
    }

    @Test
    fun `create preserves rejected intake without granting continuation`() {
        val request = UnderstandingEvaluationRequest.create(
            conversationIntake = createIntake(
                state = ConversationIntakeState.REJECTED,
            ),
        )

        assertEquals(
            ConversationIntakeState.REJECTED,
            request.conversationIntake.record.state,
        )
    }

    private fun createIntake(
        state: ConversationIntakeState,
    ): ConversationIntakeResult {
        return ConversationIntakeResult.create(
            record = ConversationIntakeRecord.create(
                input = ConversationInput.create(
                    context = ContextEnvelope.create(
                        traceId = TraceId.from(
                            "trace-understanding-evaluation-request-001",
                        ),
                        schemaVersion = SchemaVersion.from(1),
                        source = ContextSource.TEXT,
                        trustLevel = ContextTrustLevel.VERIFIED,
                        securityLevel =
                            ContextSecurityLevel.RESTRICTED,
                        observedAt =
                            DevilTimestamp.fromEpochMilliseconds(
                                1_754_000_065_000L,
                            ),
                    ),
                    content = "Please open the camera.",
                ),
                state = state,
                rationale =
                    "Bounded conversation-intake state was established.",
            ),
        )
    }
}
