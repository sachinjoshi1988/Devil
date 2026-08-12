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
import com.devil.core.model.understanding.UnderstandingActionability
import com.devil.core.model.understanding.UnderstandingEvaluationRequest
import com.devil.core.model.understanding.UnderstandingIntent
import com.devil.core.model.understanding.UnderstandingState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DefaultUnderstandingEvaluationResolverTest {

    @Test
    fun `evaluate understands Devil greeting as complete non-actionable meaning`() {
        val request =
            createRequest(
                content = "Hello Devil",
            )

        val understanding =
            DefaultUnderstandingEvaluationResolver()
                .evaluate(request)

        assertEquals(
            request.conversationIntake.record.input.context,
            understanding.context,
        )
        assertEquals(
            UnderstandingState.COMPLETE,
            understanding.state,
        )
        assertEquals(
            "User expressed a greeting.",
            understanding.summary,
        )
        assertEquals(
            UnderstandingIntent.GREETING,
            understanding.semantics?.intent,
        )
        assertEquals(
            UnderstandingActionability.NON_ACTIONABLE,
            understanding.semantics?.actionability,
        )
        assertEquals(
            "greeting",
            understanding.semantics?.meaning,
        )
        assertNull(
            understanding.semantics?.target,
        )
    }

    @Test
    fun `evaluate understands open target request without authorizing action`() {
        val understanding =
            DefaultUnderstandingEvaluationResolver()
                .evaluate(
                    createRequest(
                        content = "Open YouTube",
                    ),
                )

        assertEquals(
            UnderstandingState.COMPLETE,
            understanding.state,
        )
        assertEquals(
            UnderstandingIntent.OPEN_TARGET,
            understanding.semantics?.intent,
        )
        assertEquals(
            UnderstandingActionability.ACTIONABLE,
            understanding.semantics?.actionability,
        )
        assertEquals(
            "open target",
            understanding.semantics?.meaning,
        )
        assertEquals(
            "YouTube",
            understanding.semantics?.target,
        )
        assertEquals(
            "User requested opening the target: YouTube.",
            understanding.summary,
        )
    }

    @Test
    fun `evaluate accepts polite open target wording`() {
        val understanding =
            DefaultUnderstandingEvaluationResolver()
                .evaluate(
                    createRequest(
                        content = "Please open Maps.",
                    ),
                )

        assertEquals(
            UnderstandingState.COMPLETE,
            understanding.state,
        )
        assertEquals(
            UnderstandingIntent.OPEN_TARGET,
            understanding.semantics?.intent,
        )
        assertEquals(
            "Maps",
            understanding.semantics?.target,
        )
    }

    @Test
    fun `evaluate does not reinterpret informational YouTube statement as action`() {
        val understanding =
            DefaultUnderstandingEvaluationResolver()
                .evaluate(
                    createRequest(
                        content = "I watched YouTube yesterday",
                    ),
                )

        assertEquals(
            UnderstandingState.COMPLETE,
            understanding.state,
        )
        assertEquals(
            UnderstandingIntent.INFORMATIONAL,
            understanding.semantics?.intent,
        )
        assertEquals(
            UnderstandingActionability.NON_ACTIONABLE,
            understanding.semantics?.actionability,
        )
        assertNull(
            understanding.semantics?.target,
        )
    }

    @Test
    fun `evaluate remains unsupported when bounded policy does not match`() {
        val understanding =
            DefaultUnderstandingEvaluationResolver()
                .evaluate(
                    createRequest(
                        content =
                            "The weather around my old office was strange.",
                    ),
                )

        assertEquals(
            UnderstandingState.UNSUPPORTED,
            understanding.state,
        )
        assertEquals(
            "No bounded language-understanding policy matched the supplied input.",
            understanding.summary,
        )
        assertNull(
            understanding.semantics,
        )
    }

    private fun createRequest(
        content: String,
    ): UnderstandingEvaluationRequest {
        return UnderstandingEvaluationRequest.create(
            conversationIntake =
                ConversationIntakeResult.create(
                    record =
                        ConversationIntakeRecord.create(
                            input =
                                ConversationInput.create(
                                    context =
                                        ContextEnvelope.create(
                                            traceId =
                                                TraceId.from(
                                                    "trace-stage56-understanding-001",
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
