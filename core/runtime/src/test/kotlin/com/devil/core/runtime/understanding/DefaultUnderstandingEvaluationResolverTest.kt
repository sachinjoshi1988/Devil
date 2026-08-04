package com.devil.core.runtime.understanding

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
import com.devil.core.model.understanding.UnderstandingEvaluationRequest
import com.devil.core.model.understanding.UnderstandingState
import kotlin.test.Test
import kotlin.test.assertEquals

class DefaultUnderstandingEvaluationResolverTest {

    @Test
    fun `evaluate preserves context and returns unsupported without policy`() {
        val request = createRequest(
            content = "Please open the camera.",
        )
        val resolver: UnderstandingEvaluationResolver =
            DefaultUnderstandingEvaluationResolver()

        val understanding = resolver.evaluate(request)

        assertEquals(
            request.conversationIntake.record.input.context,
            understanding.context,
        )
        assertEquals(
            UnderstandingState.UNSUPPORTED,
            understanding.state,
        )
        assertEquals(
            "No structured language-understanding policy is available.",
            understanding.summary,
        )
    }

    @Test
    fun `evaluate does not infer intent from textual content`() {
        val first = createRequest(
            content = "Please open the camera.",
        )
        val second = createRequest(
            content = "Please call my friend.",
        )
        val resolver: UnderstandingEvaluationResolver =
            DefaultUnderstandingEvaluationResolver()

        val firstUnderstanding = resolver.evaluate(first)
        val secondUnderstanding = resolver.evaluate(second)

        assertEquals(
            UnderstandingState.UNSUPPORTED,
            firstUnderstanding.state,
        )
        assertEquals(
            UnderstandingState.UNSUPPORTED,
            secondUnderstanding.state,
        )
        assertEquals(
            firstUnderstanding.summary,
            secondUnderstanding.summary,
        )
    }

    private fun createRequest(
        content: String,
    ): UnderstandingEvaluationRequest {
        return UnderstandingEvaluationRequest.create(
            conversationIntake =
                ConversationIntakeResult.create(
                    record = ConversationIntakeRecord.create(
                        input = ConversationInput.create(
                            context = ContextEnvelope.create(
                                traceId = TraceId.from(
                                    "trace-default-understanding-resolver-001",
                                ),
                                schemaVersion =
                                    SchemaVersion.from(1),
                                source = ContextSource.TEXT,
                                trustLevel =
                                    ContextTrustLevel.VERIFIED,
                                securityLevel =
                                    ContextSecurityLevel.RESTRICTED,
                                observedAt =
                                    DevilTimestamp
                                        .fromEpochMilliseconds(
                                            1_754_000_068_000L,
                                        ),
                            ),
                            content = content,
                        ),
                        state =
                            ConversationIntakeState.ACCEPTED,
                        rationale =
                            "Conversation intake was accepted.",
                    ),
                ),
        )
    }
}
