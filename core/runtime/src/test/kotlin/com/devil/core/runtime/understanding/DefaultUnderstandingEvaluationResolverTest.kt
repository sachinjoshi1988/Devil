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
    fun `evaluate understands natural polite open target wording`() {
        val understanding =
            evaluate(
                "Can you open my settings?",
            )

        assertEquals(
            UnderstandingIntent.OPEN_TARGET,
            understanding.semantics?.intent,
        )
        assertEquals(
            "settings",
            understanding.semantics?.target,
        )
    }

    @Test
    fun `evaluate understands lower volume as action request`() {
        val understanding =
            evaluate(
                "Lower the volume",
            )

        assertEquals(
            UnderstandingState.COMPLETE,
            understanding.state,
        )
        assertEquals(
            UnderstandingIntent.ACTION_REQUEST,
            understanding.semantics?.intent,
        )
        assertEquals(
            UnderstandingActionability.ACTIONABLE,
            understanding.semantics?.actionability,
        )
        assertEquals(
            "volume",
            understanding.semantics?.target,
        )
        assertEquals(
            "decrease",
            understanding.semantics?.predicate,
        )
    }

    @Test
    fun `evaluate understands set volume percentage with structured value`() {
        val understanding =
            evaluate(
                "Set volume to 30 percent",
            )

        assertEquals(
            UnderstandingIntent.ACTION_REQUEST,
            understanding.semantics?.intent,
        )
        assertEquals(
            "set",
            understanding.semantics?.predicate,
        )
        assertEquals(
            "volume",
            understanding.semantics?.target,
        )
        assertEquals(
            listOf("value", "unit"),
            understanding.semantics
                ?.arguments
                ?.map { argument -> argument.name },
        )
        assertEquals(
            listOf("30", "percent"),
            understanding.semantics
                ?.arguments
                ?.map { argument -> argument.value },
        )
    }

    @Test
    fun `evaluate understands bounded Stage337M device knowledge questions`() {
        listOf(
            Triple(
                "What is my device model?",
                "device model",
                "query device model",
            ),
            Triple(
                "What phone is this?",
                "device model",
                "query device model",
            ),
            Triple(
                "What Android version am I using?",
                "android version",
                "query android version",
            ),
            Triple(
                "Tell me about this device.",
                "device summary",
                "query device summary",
            ),
        ).forEach { (content, target, meaning) ->
            val understanding =
                evaluate(
                    content,
                )

            assertEquals(
                UnderstandingIntent.INFORMATION_QUERY,
                understanding.semantics?.intent,
            )
            assertEquals(
                UnderstandingActionability.ACTIONABLE,
                understanding.semantics?.actionability,
            )
            assertEquals(
                target,
                understanding.semantics?.target,
            )
            assertEquals(
                meaning,
                understanding.semantics?.meaning,
            )
            assertEquals(
                "query",
                understanding.semantics?.predicate,
            )
        }
    }

    @Test
    fun `evaluate understands battery level question as information query`() {
        val understanding =
            evaluate(
                "What's my battery level?",
            )

        assertEquals(
            UnderstandingIntent.INFORMATION_QUERY,
            understanding.semantics?.intent,
        )
        assertEquals(
            UnderstandingActionability.ACTIONABLE,
            understanding.semantics?.actionability,
        )
        assertEquals(
            "battery level",
            understanding.semantics?.target,
        )
        assertEquals(
            "query",
            understanding.semantics?.predicate,
        )
    }

    @Test
    fun `evaluate understands notification request as information query`() {
        val understanding =
            evaluate(
                "Show me my notifications",
            )

        assertEquals(
            UnderstandingIntent.INFORMATION_QUERY,
            understanding.semantics?.intent,
        )
        assertEquals(
            "notifications",
            understanding.semantics?.target,
        )
        assertEquals(
            "query",
            understanding.semantics?.predicate,
        )
    }

    @Test
    fun `evaluate preserves alarm time expression without resolving it`() {
        val understanding =
            evaluate(
                "Set an alarm for seven tomorrow",
            )

        assertEquals(
            UnderstandingIntent.ACTION_REQUEST,
            understanding.semantics?.intent,
        )
        assertEquals(
            "alarm",
            understanding.semantics?.target,
        )
        assertEquals(
            "set",
            understanding.semantics?.predicate,
        )
        assertEquals(
            "time_expression",
            understanding.semantics?.arguments?.single()?.name,
        )
        assertEquals(
            "seven tomorrow",
            understanding.semantics?.arguments?.single()?.value,
        )
    }

    @Test
    fun `evaluate preserves reply recipient and content without resolving reference`() {
        val understanding =
            evaluate(
                "Reply to him: wait, I'm coming",
            )

        assertEquals(
            UnderstandingIntent.ACTION_REQUEST,
            understanding.semantics?.intent,
        )
        assertEquals(
            "message",
            understanding.semantics?.target,
        )
        assertEquals(
            "reply",
            understanding.semantics?.predicate,
        )
        assertEquals(
            listOf("recipient_reference", "content"),
            understanding.semantics
                ?.arguments
                ?.map { argument -> argument.name },
        )
        assertEquals(
            listOf("him", "wait, I'm coming"),
            understanding.semantics
                ?.arguments
                ?.map { argument -> argument.value },
        )
    }

    @Test
    fun `evaluate preserves media object reference without guessing`() {
        val understanding =
            evaluate(
                "Play that song",
            )

        assertEquals(
            UnderstandingIntent.ACTION_REQUEST,
            understanding.semantics?.intent,
        )
        assertEquals(
            "media",
            understanding.semantics?.target,
        )
        assertEquals(
            "play",
            understanding.semantics?.predicate,
        )
        assertEquals(
            "object_reference",
            understanding.semantics?.arguments?.single()?.name,
        )
        assertEquals(
            "that song",
            understanding.semantics?.arguments?.single()?.value,
        )
    }

    @Test
    fun `evaluate understands general factual English question without answering it`() {
        val understanding =
            evaluate(
                "Who is Ada Lovelace?",
            )

        assertEquals(
            UnderstandingIntent.INFORMATION_QUERY,
            understanding.semantics?.intent,
        )
        assertEquals(
            "Ada Lovelace",
            understanding.semantics?.target,
        )
        assertEquals(
            "query",
            understanding.semantics?.predicate,
        )
    }


    @Test
    fun `evaluate understands raise volume as action request`() {
        val understanding =
            evaluate(
                "Raise the volume",
            )

        assertEquals(
            UnderstandingIntent.ACTION_REQUEST,
            understanding.semantics?.intent,
        )
        assertEquals(
            "volume",
            understanding.semantics?.target,
        )
        assertEquals(
            "increase",
            understanding.semantics?.predicate,
        )
    }

    @Test
    fun `evaluate distinguishes latest notification query`() {
        val understanding =
            evaluate(
                "Read my latest notification",
            )

        assertEquals(
            UnderstandingIntent.INFORMATION_QUERY,
            understanding.semantics?.intent,
        )
        assertEquals(
            "latest notification",
            understanding.semantics?.target,
        )
        assertEquals(
            "query",
            understanding.semantics?.predicate,
        )
    }

    @Test
    fun `evaluate preserves explicit send message recipient and content`() {
        val understanding =
            evaluate(
                "Send a message to Rahul: wait there",
            )

        assertEquals(
            UnderstandingIntent.ACTION_REQUEST,
            understanding.semantics?.intent,
        )
        assertEquals(
            "message",
            understanding.semantics?.target,
        )
        assertEquals(
            "send",
            understanding.semantics?.predicate,
        )
        assertEquals(
            listOf(
                "recipient_reference",
                "content",
            ),
            understanding.semantics
                ?.arguments
                ?.map { argument -> argument.name },
        )
        assertEquals(
            listOf(
                "Rahul",
                "wait there",
            ),
            understanding.semantics
                ?.arguments
                ?.map { argument -> argument.value },
        )
    }

    @Test
    fun `evaluate preserves call recipient without resolving contact`() {
        val understanding =
            evaluate(
                "Call Rahul",
            )

        assertEquals(
            UnderstandingIntent.ACTION_REQUEST,
            understanding.semantics?.intent,
        )
        assertEquals(
            "contact",
            understanding.semantics?.target,
        )
        assertEquals(
            "call",
            understanding.semantics?.predicate,
        )
        assertEquals(
            "recipient_reference",
            understanding.semantics?.arguments?.single()?.name,
        )
        assertEquals(
            "Rahul",
            understanding.semantics?.arguments?.single()?.value,
        )
    }

    @Test
    fun `evaluate understands explicit pause media request`() {
        val understanding =
            evaluate(
                "Pause the music",
            )

        assertEquals(
            UnderstandingIntent.ACTION_REQUEST,
            understanding.semantics?.intent,
        )
        assertEquals(
            "media",
            understanding.semantics?.target,
        )
        assertEquals(
            "pause",
            understanding.semantics?.predicate,
        )
    }

    @Test
    fun `evaluate understands tell me about as information query without answering`() {
        val understanding =
            evaluate(
                "Tell me about quantum computing",
            )

        assertEquals(
            UnderstandingIntent.INFORMATION_QUERY,
            understanding.semantics?.intent,
        )
        assertEquals(
            "quantum computing",
            understanding.semantics?.target,
        )
        assertEquals(
            "query",
            understanding.semantics?.predicate,
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
    fun `evaluate remains unsupported for ambiguous request rather than guessing`() {
        val understanding =
            evaluate(
                "Can you help me with this?",
            )

        assertEquals(
            UnderstandingState.UNSUPPORTED,
            understanding.state,
        )
        assertNull(
            understanding.semantics,
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

    private fun evaluate(
        content: String,
    ) =
        DefaultUnderstandingEvaluationResolver()
            .evaluate(
                createRequest(
                    content = content,
                ),
            )

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
                                                    "trace-stage337c-understanding-001",
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
